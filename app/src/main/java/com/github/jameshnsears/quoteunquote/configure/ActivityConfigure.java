package com.github.jameshnsears.quoteunquote.configure;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.appearance.FragmentAppearance;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.FragmentContent;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.github.jameshnsears.quoteunquote.configure.fragment.event.FragmentEvent;
import com.github.jameshnsears.quoteunquote.configure.fragment.footer.FragmentFooter;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;
import com.github.jameshnsears.quoteunquote.utils.IntentFactoryHelper;
import com.github.jameshnsears.quoteunquote.utils.audit.AuditEventHelper;
import com.github.jameshnsears.quoteunquote.utils.ui.ToastHelper;

import timber.log.Timber;


public class ActivityConfigure extends AppCompatActivity {
    public int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onPause() {
        Timber.d("widgetId=%d", widgetId);

        finish();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        ToastHelper.toast = null;
        super.onDestroy();
    }

    @Override
    public void finish() {
        ensureFragmentContentSearchConsistency();

        broadcastFinishIntent();

        super.finish();
    }

    public void ensureFragmentContentSearchConsistency() {
        final FragmentContent fragmentContent = getFragmentContent();

        if (isSearchSelectedButWithoutResults(fragmentContent)) {
            warnUserAboutSearchResults();
            fragmentContent.fragmentContentBinding.radioButtonAll.setChecked(true);
            resetContentSelection();
        }

        fragmentContent.shutdown();
    }

    public void broadcastFinishIntent() {
        sendBroadcast(IntentFactoryHelper.createIntentAction(
                this, widgetId, IntentFactoryHelper.ACTIVITY_FINISHED_CONFIGURATION));

        setResult(RESULT_OK, IntentFactoryHelper.createIntent(widgetId));
    }

    @Override
    public void onBackPressed() {
        Timber.d("widgetId=%d", widgetId);
        super.onBackPressed();

        finish();
    }

    @NonNull
    public FragmentContent getFragmentContent() {
        return (FragmentContent)
                getSupportFragmentManager().findFragmentById(R.id.fragmentPlaceholderContent);
    }

    private void resetContentSelection() {
        final PreferenceContent preferenceContent = new PreferenceContent(widgetId, getApplicationContext());
        preferenceContent.setContentSelection(ContentSelection.ALL);
    }

    private void warnUserAboutSearchResults() {
        ToastHelper.makeToast(this, this.getString(R.string.fragment_content_text_no_search_results), Toast.LENGTH_LONG);
    }

    private boolean isSearchSelectedButWithoutResults(
            @NonNull final FragmentContent fragmentContent) {
        return fragmentContent.fragmentContentBinding.radioButtonSearch.isChecked()
                && fragmentContent.countSearchResults == 0;
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

        createFragments();
    }

    public void createFragments() {
        AuditEventHelper.createInstance(getApplication());

        this.setTitle(getString(R.string.activity_configure_title));

        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentPlaceholderAppearance, FragmentAppearance.newInstance(widgetId));
        fragmentTransaction.add(R.id.fragmentPlaceholderContent, getFragmentContentNewInstance());
        fragmentTransaction.add(R.id.fragmentPlaceholderEvent, FragmentEvent.newInstance(widgetId));
        fragmentTransaction.add(R.id.fragmentPlaceholderFooter, FragmentFooter.newInstance(widgetId));
        fragmentTransaction.commit();

        setContentView(R.layout.activity_configure);
    }

    @NonNull
    public FragmentContent getFragmentContentNewInstance() {
        return FragmentContent.newInstance(widgetId);
    }
}
