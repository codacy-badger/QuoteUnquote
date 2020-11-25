package com.github.jameshnsears.quoteunquote.listview;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.jameshnsears.quoteunquote.QuoteUnquoteModel;
import com.github.jameshnsears.quoteunquote.QuoteUnquoteWidget;
import com.github.jameshnsears.quoteunquote.configure.fragment.appearance.PreferenceAppearance;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

class ListViewProvider implements RemoteViewsService.RemoteViewsFactory {
    private final List<String> quotationList = new ArrayList<>();
    private final Context context;
    private final int widgetId;
    private final QuoteUnquoteWidget quoteUnquoteWidget;
    public PreferenceAppearance preferenceAppearance;
    public PreferenceContent preferenceContent;
    private QuotationEntity quotationEntity;

    ListViewProvider(final Context serviceContext, final Intent intent) {
        this.context = serviceContext;
        this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

        preferenceAppearance = new PreferenceAppearance(widgetId, serviceContext);
        preferenceContent = new PreferenceContent(widgetId, serviceContext);

        quoteUnquoteWidget = new QuoteUnquoteWidget();
    }

    @Override
    public void onCreate() {
        Timber.d("widgetId=%d", widgetId);
    }

    @Override
    public void onDataSetChanged() {
        Timber.d("widgetId=%d", widgetId);

        synchronized (this) {
            quotationList.clear();

            quotationEntity = getQuoteUnquoteModel(context).getNext(
                    widgetId,
                    preferenceContent.getContentSelection());

            if (quotationEntity != null) {
                quotationList.add(quotationEntity.theQuotation());
            }
        }
    }

    public QuoteUnquoteModel getQuoteUnquoteModel(final Context listViewContext) {
        return quoteUnquoteWidget.getQuoteUnquoteModelInstance(listViewContext);
    }

    @Override
    public void onDestroy() {
        Timber.d("widgetId=%d", widgetId);
    }

    @Override
    public int getCount() {
        synchronized (this) {
            return quotationList.size();
        }
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final RemoteViews remoteViews = getRemoteViews(position);

        final Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        remoteViews.setOnClickFillInIntent(android.R.id.text1, intent);

        return remoteViews;
    }

    private RemoteViews getRemoteViews(final int position) {
        final RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(),
                android.R.layout.simple_list_item_1);

        synchronized (this) {
            if (!quotationList.isEmpty()) {

                remoteViews.setTextViewText(android.R.id.text1, quotationList.get(position));

                remoteViews.setTextViewTextSize(
                        android.R.id.text1,
                        TypedValue.COMPLEX_UNIT_DIP,
                        (float) this.preferenceAppearance.getAppearanceTextSize());

                remoteViews.setTextColor(
                        android.R.id.text1,
                        Color.parseColor(this.preferenceAppearance.getAppearanceTextColour()));

                Timber.d("%d: digest=%s", widgetId, quotationEntity.digest);

                if (getQuoteUnquoteModel(context).isReported(widgetId)) {
                    remoteViews.setInt(android.R.id.text1, "setPaintFlags",
                            Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                } else {
                    remoteViews.setInt(android.R.id.text1, "setPaintFlags", Paint.ANTI_ALIAS_FLAG);
                }
            }
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return getRemoteViews(0);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
