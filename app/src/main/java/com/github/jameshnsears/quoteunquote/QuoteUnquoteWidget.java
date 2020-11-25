package com.github.jameshnsears.quoteunquote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.github.jameshnsears.quoteunquote.cloud.CloudFavouritesHelper;
import com.github.jameshnsears.quoteunquote.cloud.CloudServiceSend;
import com.github.jameshnsears.quoteunquote.configure.ActivityConfigure;
import com.github.jameshnsears.quoteunquote.configure.fragment.appearance.PreferenceAppearance;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.github.jameshnsears.quoteunquote.configure.fragment.event.DailyAlarm;
import com.github.jameshnsears.quoteunquote.configure.fragment.event.PreferenceEvent;
import com.github.jameshnsears.quoteunquote.database.NoNextQuotationAvailableException;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.github.jameshnsears.quoteunquote.listview.ListViewService;
import com.github.jameshnsears.quoteunquote.logging.MethodLineLoggingTree;
import com.github.jameshnsears.quoteunquote.preference.PreferenceFacade;
import com.github.jameshnsears.quoteunquote.report.ActivityReport;
import com.github.jameshnsears.quoteunquote.ui.ToastHelper;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;
import com.github.jameshnsears.quoteunquote.utils.IntentFactoryHelper;

import timber.log.Timber;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_DISABLED;

public final class QuoteUnquoteWidget extends AppWidgetProvider {
    private static volatile boolean receiversRegistered = false;
    private QuoteUnquoteModel quoteUnquoteModel;

    private static void registerReceivers(Context contextIn) {
        if (receiversRegistered) {
            return;
        }

        Context context = contextIn.getApplicationContext();
        QuoteUnquoteWidget receiver = new QuoteUnquoteWidget();

        IntentFilter userPresent = new IntentFilter();
        userPresent.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(receiver, userPresent);

        IntentFilter bootCompleted = new IntentFilter();
        bootCompleted.addAction("android.intent.action.BOOT_COMPLETED");
        context.registerReceiver(receiver, bootCompleted);

        IntentFilter qyuickbootPoweron = new IntentFilter();
        qyuickbootPoweron.addAction("android.intent.action.QUICKBOOT_POWERON");
        context.registerReceiver(receiver, qyuickbootPoweron);

        receiversRegistered = true;
    }

    public synchronized QuoteUnquoteModel getQuoteUnquoteModelInstance(final Context context) {
        if (quoteUnquoteModel == null) {
            quoteUnquoteModel = new QuoteUnquoteModel(context);
        }

        return quoteUnquoteModel;
    }

    public void shutdownQuoteUnquoteModel(final Context context) {
        if (quoteUnquoteModel != null) {
            getQuoteUnquoteModelInstance(context).shutdown();
            quoteUnquoteModel = null;
        }
    }

    @Override
    public void onEnabled(final Context context) {
        CloudFavouritesHelper.setLocalCode(context);

        if (BuildConfig.DEBUG) {
            if (Timber.treeCount() == 0) {
                Timber.plant(new MethodLineLoggingTree());
            }
        }

        Timber.d("onEnabled");
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] widgetIds) {
        registerReceivers(context);

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quote_unquote_widget);

        for (final int widgetId : widgetIds) {

            remoteViews.setRemoteAdapter(
                    R.id.listViewQuotation,
                    IntentFactoryHelper.createIntent(context, ListViewService.class, widgetId));

            remoteViews.setPendingIntentTemplate(
                    R.id.listViewQuotation,
                    PendingIntent.getActivity(
                            context,
                            widgetId,
                            IntentFactoryHelper.createIntent(context, ActivityConfigure.class, widgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonFirst,
                    IntentFactoryHelper.createIntentPending(context, widgetId, IntentFactoryHelper.TOOLBAR_PRESSED_FIRST));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonPrevious,
                    IntentFactoryHelper.createIntentPending(context, widgetId, IntentFactoryHelper.TOOLBAR_PRESSED_PREVIOUS));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonReport,
                    PendingIntent.getActivity(
                            context,
                            widgetId,
                            IntentFactoryHelper.createIntent(context, ActivityReport.class, widgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonFavourite,
                    IntentFactoryHelper.createIntentPending(context, widgetId, IntentFactoryHelper.TOOLBAR_PRESSED_FAVOURITE));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonShare,
                    IntentFactoryHelper.createIntentPending(context, widgetId, IntentFactoryHelper.TOOLBAR_PRESSED_SHARE));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonNextRandom,
                    IntentFactoryHelper.createIntentPending(context, widgetId, IntentFactoryHelper.TOOLBAR_PRESSED_NEXT_RANDOM));

            remoteViews.setOnClickPendingIntent(
                    R.id.imageButtonNextSequential,
                    IntentFactoryHelper.createIntentPending(context, widgetId, IntentFactoryHelper.TOOLBAR_PRESSED_NEXT_SEQUENTIAL));

            setTransparency(context, widgetId, remoteViews);

            setToolbarButtonsVisibility(context, widgetId, remoteViews);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);

        final int widgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Timber.d("%d: action=%s", widgetId, intent.getAction());

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        final DailyAlarm dailyAlarm = new DailyAlarm(context, widgetId);

        try {
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    onReceiveDeviceUnlock(context, appWidgetManager);
                    break;

                case Intent.ACTION_BOOT_COMPLETED:
                case Intent.ACTION_REBOOT:
                /*
                adb shell
                am broadcast -a android.intent.action.BOOT_COMPLETED
                 */
                case IntentFactoryHelper.ACTIVITY_FINISHED_CONFIGURATION:
                    onReceiveActivityFinishedConfiguration(widgetId, appWidgetManager, dailyAlarm);
                    break;

                case IntentFactoryHelper.ACTIVITY_FINISHED_REPORT:
                    onReceiveActivityFinishedReport(widgetId, appWidgetManager);
                    break;

                case IntentFactoryHelper.DAILY_ALARM:
                    onReceiveDailyAlarm(context, widgetId, appWidgetManager, dailyAlarm);
                    break;

                case IntentFactoryHelper.TOOLBAR_PRESSED_FIRST:
                    onReceiveToolbarPressedFirst(context, widgetId, appWidgetManager);
                    break;

                case IntentFactoryHelper.TOOLBAR_PRESSED_PREVIOUS:
                    onReceiveToolbarPressedPrevious(context, widgetId, appWidgetManager);
                    break;

                case IntentFactoryHelper.TOOLBAR_PRESSED_FAVOURITE:
                    onReceiveToolbarPressedFavourite(context, widgetId, appWidgetManager);
                    break;

                case IntentFactoryHelper.TOOLBAR_PRESSED_SHARE:
                    onReceiveToolbarPressedShare(context, widgetId);
                    break;

                case IntentFactoryHelper.TOOLBAR_PRESSED_NEXT_RANDOM:
                    onReceiveToolbarPressedNextRandom(context, widgetId, appWidgetManager);
                    break;

                case IntentFactoryHelper.TOOLBAR_PRESSED_NEXT_SEQUENTIAL:
                    onReceiveToolbarPressedNextSequential(context, widgetId, appWidgetManager);
                    break;

                default:
                    break;
            }
        } finally {
            // mainly for screen rotation!
            if (!intent.getAction().equals(ACTION_APPWIDGET_DISABLED)) {
                onUpdate(context, appWidgetManager, new int[]{widgetId});
            }
        }
    }

    private void onReceiveActivityFinishedReport(final int widgetId, final AppWidgetManager appWidgetManager) {
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.listViewQuotation);
    }

    private void onReceiveDeviceUnlock(final Context context, final AppWidgetManager appWidgetManager) {
        unlockDevice(context, appWidgetManager);
    }

    private void onReceiveToolbarPressedShare(final Context context, final int widgetId) {
        context.startActivity(IntentFactoryHelper.createIntentShare(
                context.getResources().getString(R.string.app_name),
                getQuoteUnquoteModelInstance(context).getNext(widgetId, new PreferenceContent(widgetId, context).getContentSelection()).theQuotation()));
    }

    private void onReceiveToolbarPressedFavourite(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        final PreferenceContent preferenceContent = new PreferenceContent(widgetId, context);

        getQuoteUnquoteModelInstance(context).toggleFavourite(widgetId, getQuoteUnquoteModelInstance(context).getNext(
                widgetId, preferenceContent.getContentSelection()).digest);

        toggleFavouriteColour(context, widgetId, appWidgetManager);

        if (preferenceContent.getContentSelection().equals(ContentSelection.FAVOURITES)
                || preferenceContent.getContentSelection().equals(ContentSelection.ALL)) {
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.listViewQuotation);
        }
    }

    private void onReceiveToolbarPressedFirst(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        ToastHelper.makeToast(context, context.getString(R.string.widget_button_first_toast), Toast.LENGTH_LONG);

        getQuoteUnquoteModelInstance(context).deletePrevious(widgetId, new PreferenceContent(widgetId, context).getContentSelection());

        updateWidgetView(context, widgetId, appWidgetManager);
    }

    private void onReceiveToolbarPressedPrevious(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        // TODO

        updateWidgetView(context, widgetId, appWidgetManager);
    }

    private void onReceiveToolbarPressedNextRandom(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        onReceiveToolbarPressedNext(context, widgetId, appWidgetManager, true);
    }

    private void onReceiveToolbarPressedNextSequential(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        onReceiveToolbarPressedNext(context, widgetId, appWidgetManager, false);
    }

    private void onReceiveToolbarPressedNext(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager, final boolean randomNext) {
        final PreferenceContent preferenceContent = new PreferenceContent(widgetId, context);

        try {
            getQuoteUnquoteModelInstance(context).setNext(widgetId, preferenceContent.getContentSelection(), randomNext);

            updateWidgetView(context, widgetId, appWidgetManager);
        } catch (NoNextQuotationAvailableException e) {
            ToastHelper.makeToast(context, context.getString(R.string.widget_button_next_toast), Toast.LENGTH_LONG);
        }
    }

    private void onReceiveDailyAlarm(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager, final DailyAlarm dailyAlarm) {
        dailyAlarm.setDailyAlarm();
        try {
            getQuoteUnquoteModelInstance(context)
                    .setNext(widgetId, new PreferenceContent(widgetId, context).getContentSelection(), true);

            updateWidgetView(context, widgetId, appWidgetManager);
        } catch (NoNextQuotationAvailableException e) {
            Timber.d(e);
        }
    }

    private void updateWidgetView(final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        toggleFavouriteColour(context, widgetId, appWidgetManager);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.listViewQuotation);
    }

    private void onReceiveActivityFinishedConfiguration(final int widgetId, final AppWidgetManager appWidgetManager, final DailyAlarm dailyAlarm) {
        dailyAlarm.setDailyAlarm();
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.listViewQuotation);
    }

    private void setTransparency(final Context context, final int widgetId, final RemoteViews remoteViews) {
        logWidgetId(widgetId);

        final PreferenceAppearance preferenceAppearance = new PreferenceAppearance(widgetId, context);
        final int seekBarValue = preferenceAppearance.getAppearanceTransparency();
        Timber.d("seekBarValue=%d", seekBarValue);

        float transparency = 1;
        if (seekBarValue != -1) {
            transparency -= seekBarValue * .1f;
        }

        final String setBackgroundColor = "setBackgroundColor";
        remoteViews.setInt(R.id.listViewQuotation, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonFirst, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonPrevious, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonReport, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonFavourite, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonShare, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonNextRandom, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
        remoteViews.setInt(R.id.imageButtonNextSequential, setBackgroundColor, (int) (transparency * 0xFF) << 24 | 0xFFFFFF);
    }

    private void logWidgetId(final int widgetId) {
        Timber.d("widgetId=%d", widgetId);
    }

    private void setToolbarButtonsVisibility(final Context context, final int widgetId, final RemoteViews remoteViews) {
        logWidgetId(widgetId);

        setToolbarButtonVisibilityFirst(context, widgetId, remoteViews);
        setToolbarButtonVisibilityPrevious(context, widgetId, remoteViews);
        setToolbarButtonVisibilityReport(context, widgetId, remoteViews);
        setToolbarButtonVisibilityFavourite(context, widgetId, remoteViews);
        setToolbarButtonVisibilityShare(context, widgetId, remoteViews);
        setToolbarButtonVisibilityRandom(context, widgetId, remoteViews);
        setToolbarButtonVisibilitySequntial(context, widgetId, remoteViews);

        setHeartColour(context, widgetId, remoteViews);
    }

    private void setToolbarButtonVisibilityFirst(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarFirst()) {
            remoteViews.setViewVisibility(R.id.imageButtonFirst, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonFirst, View.GONE);
        }
    }

    private void setToolbarButtonVisibilityPrevious(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarPrevious()) {
            remoteViews.setViewVisibility(R.id.imageButtonPrevious, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonPrevious, View.GONE);
        }
    }

    private void setToolbarButtonVisibilityReport(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarReport()) {
            remoteViews.setViewVisibility(R.id.imageButtonReport, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonReport, View.GONE);
        }
    }

    private void setToolbarButtonVisibilityFavourite(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarFavourite()) {
            remoteViews.setViewVisibility(R.id.imageButtonFavourite, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonFavourite, View.GONE);
        }
    }

    private void setToolbarButtonVisibilityShare(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarShare()) {
            remoteViews.setViewVisibility(R.id.imageButtonShare, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonShare, View.GONE);
        }
    }

    private void setToolbarButtonVisibilityRandom(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarRandom()) {
            remoteViews.setViewVisibility(R.id.imageButtonNextRandom, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonNextRandom, View.GONE);
        }
    }

    private void setToolbarButtonVisibilitySequntial(final Context context, final int widgetId, final RemoteViews remoteViews) {
        if (new PreferenceAppearance(widgetId, context).getAppearanceToolbarSequential()) {
            remoteViews.setViewVisibility(R.id.imageButtonNextSequential, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.imageButtonNextSequential, View.GONE);
        }
    }


    private void toggleFavouriteColour(
            final Context context, final int widgetId, final AppWidgetManager appWidgetManager) {
        logWidgetId(widgetId);

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quote_unquote_widget);

        final PreferenceFacade preferenceFacade = new PreferenceFacade(widgetId, context);

        setHeartColour(context, widgetId, remoteViews);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    private void setHeartColour(final Context context, final int widgetId, final RemoteViews remoteViews) {

        final QuotationEntity quotationEntity
                = getQuoteUnquoteModelInstance(context).getNext(widgetId, new PreferenceContent(widgetId, context).getContentSelection());

        if (quotationEntity != null) {
            if (getQuoteUnquoteModelInstance(context).isFavourite(widgetId, quotationEntity.digest)) {
                remoteViews.setImageViewResource(R.id.imageButtonFavourite, R.drawable.ic_favorite_red_24dp);
            } else {
                remoteViews.setImageViewResource(R.id.imageButtonFavourite, R.drawable.ic_favorite_border_black_24dp);
            }
        }
    }

    private void unlockDevice(final Context context, final AppWidgetManager appWidgetManager) {
        for (final int widgetId : appWidgetManager.getAppWidgetIds(new ComponentName(context, QuoteUnquoteWidget.class))) {

            if (new PreferenceEvent(widgetId, context).getEventDeviceUnlock()) {
                try {
                    getQuoteUnquoteModelInstance(context)
                            .setNext(widgetId, new PreferenceContent(widgetId, context).getContentSelection(), true);

                    updateWidgetView(context, widgetId, appWidgetManager);
                } catch (NoNextQuotationAvailableException e) {
                    Timber.d(e);
                }
            }
        }
    }

    @Override
    public void onDeleted(final Context context, final int[] widgetIds) {
        // a widget instance is removed from the home screen
        super.onDeleted(context, widgetIds);

        for (final int widgetId : widgetIds) {
            logWidgetId(widgetId);

            getQuoteUnquoteModelInstance(context).removeDatabaseEntriesForInstance(widgetId);
            PreferenceFacade.empty(context, widgetId);

            final DailyAlarm dailyAlarm = new DailyAlarm(context, widgetId);
            dailyAlarm.resetAnyExistingDailyAlarm();
        }
    }

    @Override
    public void onDisabled(final Context context) {
        // last widget instance deleted
        super.onDisabled(context);

        try {
            getQuoteUnquoteModelInstance(context).removeDatabaseEntriesForAllInstances();
            PreferenceFacade.empty(context);

            if (CloudServiceSend.isRunning(context)) {
                context.stopService(new Intent(context, CloudServiceSend.class));
            }
        } finally {
            shutdownQuoteUnquoteModel(context);
        }
    }
}
