package com.github.jameshnsears.quoteunquote.configure;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.jameshnsears.quoteunquote.BuildConfig;
import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.audit.AuditEventHelper;
import com.github.jameshnsears.quoteunquote.configure.fragment.appearance.FragmentAppearance;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.FragmentContent;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.github.jameshnsears.quoteunquote.configure.fragment.event.FragmentEvent;
import com.github.jameshnsears.quoteunquote.configure.fragment.footer.FragmentFooter;
import com.github.jameshnsears.quoteunquote.ui.ToastHelper;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;
import com.github.jameshnsears.quoteunquote.utils.IntentFactoryHelper;

import timber.log.Timber;


public class ActivityConfigure extends AppCompatActivity {
    public int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onPause() {
        Timber.d("widgetId=%d", widgetId);

        finishActivity();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        ToastHelper.toast = null;
        super.onDestroy();
    }

    private void finishActivity() {
        final FragmentContent fragmentContent = (FragmentContent)
                getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderContent);

        if (isQuotationTextEmpty(fragmentContent)) {
            resetContentSelection();
        }

        sendBroadcast(IntentFactoryHelper.createIntentAction(
                this, widgetId, IntentFactoryHelper.ACTIVITY_FINISHED_CONFIGURATION));

        setResult(RESULT_OK, IntentFactoryHelper.createIntent(widgetId));
        finish();
    }

    @Override
    public void onBackPressed() {
        Timber.d("widgetId=%d", widgetId);

        final FragmentContent fragmentContent = (FragmentContent)
                getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderContent);

        if (isQuotationTextEmpty(fragmentContent)) {
            warnUserAboutQuotationText();
            fragmentContent.fragmentContentBinding.radioButtonAll.setChecked(true);

            resetContentSelection();
        } else {
            finishActivity();
        }
    }

    private void resetContentSelection() {
        final PreferenceContent preferenceContent = new PreferenceContent(widgetId, getApplicationContext());
        preferenceContent.setContentSelection(ContentSelection.ALL);
    }

    private void warnUserAboutQuotationText() {
        ToastHelper.makeToast(this, this.getString(R.string.fragment_content_text_no_search_results), Toast.LENGTH_LONG);
    }

    private boolean isQuotationTextEmpty(final FragmentContent fragmentContent) {
        return fragmentContent.fragmentContentBinding.radioButtonSearch.isChecked()
                && fragmentContent.countKeywords == 0;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Timber.d("AppWidgetManager.INVALID_APPWIDGET_ID");
        }

        createFragments();
    }

    private void createFragments() {
        AuditEventHelper.createInstance(getApplication(), BuildConfig.APPCENTER_KEY);

        this.setTitle(getString(R.string.activity_configure_title));

        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentPlaceholderAppearance, FragmentAppearance.newInstance(widgetId));
        fragmentTransaction.add(R.id.fragmentPlaceholderContent, FragmentContent.newInstance(widgetId));
        fragmentTransaction.add(R.id.fragmentPlaceholderEvent, FragmentEvent.newInstance(widgetId));
        fragmentTransaction.add(R.id.fragmentPlaceholderFooter, FragmentFooter.newInstance(widgetId));
        fragmentTransaction.commit();

        setContentView(R.layout.activity_configure);
    }
}
