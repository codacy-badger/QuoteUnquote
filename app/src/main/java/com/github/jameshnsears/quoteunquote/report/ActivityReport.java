package com.github.jameshnsears.quoteunquote.report;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jameshnsears.quoteunquote.QuoteUnquoteWidget;
import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.github.jameshnsears.quoteunquote.databinding.ActivityReportBinding;
import com.github.jameshnsears.quoteunquote.utils.IntentFactoryHelper;
import com.github.jameshnsears.quoteunquote.utils.audit.AuditEventHelper;
import com.github.jameshnsears.quoteunquote.utils.ui.ToastHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import timber.log.Timber;

public final class ActivityReport extends AppCompatActivity {
    @Nullable
    private ActivityReportBinding activityReportBinding;
    @Nullable
    private QuoteUnquoteWidget quoteUnquoteWidget;
    private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Timber.d("widgetId=%d", widgetId);

        activityReportBinding = null;

        sendBroadcast(IntentFactoryHelper.createIntentAction(
                this,
                widgetId,
                IntentFactoryHelper.ACTIVITY_FINISHED_REPORT));

        setResult(RESULT_OK, IntentFactoryHelper.createIntent(widgetId));
        finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quoteUnquoteWidget = new QuoteUnquoteWidget();

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        if (hasQuotationAlreadyBeenReported()) {
            finish();
        }

        activityReportBinding = ActivityReportBinding.inflate(getLayoutInflater());

        final View view = activityReportBinding.getRoot();

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(view);

        activityReportBinding.cancelButton.setOnClickListener(view1 -> finish());

        activityReportBinding.buttonOK.setOnClickListener(view1 -> {
            AuditEventHelper.auditEvent("REPORT", getAuditProperties());

            quoteUnquoteWidget.getQuoteUnquoteModelInstance(getApplicationContext()).markAsReported(widgetId);

            onBackPressed();
        });
    }

    public boolean hasQuotationAlreadyBeenReported() {
        if (quoteUnquoteWidget.getQuoteUnquoteModelInstance(getApplicationContext()).isReported(widgetId)) {
            ToastHelper.makeToast(getApplicationContext(), getApplicationContext().getString(R.string.activity_report_warning), Toast.LENGTH_SHORT);
            return true;
        }

        return false;
    }

    @NonNull
    public ConcurrentHashMap<String, String> getAuditProperties() {
        final ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();

        final PreferenceContent preferenceContent = new PreferenceContent(widgetId, getApplicationContext());
        final QuotationEntity quotationToReport
                = quoteUnquoteWidget.getQuoteUnquoteModelInstance(getApplicationContext()).getNext(widgetId, preferenceContent.getContentSelection());

        properties.put("Report", "digest="
                + quotationToReport.digest
                + "; author="
                + quotationToReport.author
                + "; reason="
                + activityReportBinding.spinnerReason.getSelectedItem().toString()
                + "; notes="
                + activityReportBinding.editTextNotes.getText().toString());

        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            Timber.d(entry.getKey() + ":" + entry.getValue());
        }

        return properties;
    }
}
