package com.github.jameshnsears.quoteunquote.report;

import android.widget.Spinner;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.DatabaseTestHelper;
import com.github.jameshnsears.quoteunquote.R;

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ActivityReportTest extends DatabaseTestHelper {
    @Rule
    public ActivityScenarioRule activityRule = new ActivityScenarioRule(ActivityReport.class);

    @Test
    public void reportQuotation() {
        insertTestDataSet01();

        setDefaultQuotation();

        quoteUnquoteModel.markAsReported(WIDGET_ID);

        assertTrue("", quoteUnquoteModel.isReported(WIDGET_ID));
    }

    @Test
    public void itemsInSpinner() {
        // TODO move to roboelectric + kt?
        // https://developer.android.com/guide/components/activities/testing

        // https://medium.com/stepstone-tech/better-tests-with-androidxs-activityscenario-in-kotlin-part-1-6a6376b713ea
        final Spinner spinnerReason = activityRule.getActivity().findViewById(R.id.spinnerReason);
        assertThat("", spinnerReason.getAdapter().getCount(), Is.is(6));
    }

    @Test
    public void hasQuotationAlreadyBeenReported() {
        assertFalse("", activityRule.getActivity().hasQuotationAlreadyBeenReported());
    }

    @Test
    public void auditProperties() {
        final ConcurrentHashMap<String, String> auditProperties = activityRule.getActivity().getAuditProperties();
        assertEquals(
                "",
                "digest=1624c314; author=Arthur Balfour; reason=Attribution; notes=", auditProperties.get("Report"));
    }
}
