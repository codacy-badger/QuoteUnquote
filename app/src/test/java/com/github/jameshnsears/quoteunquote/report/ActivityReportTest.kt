package com.github.jameshnsears.quoteunquote.report

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.widget.Spinner
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.DatabaseTestHelper
import com.github.jameshnsears.quoteunquote.R
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Ignore
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ActivityReportTest : DatabaseTestHelper() {
    lateinit var scenario: ActivityScenario<ActivityReport>

    @After
    fun cleanup() {
        if (this::scenario.isInitialized)
            scenario.close()
    }

    @Test
    fun reportQuotation() {
        insertTestDataSet01()

        setDefaultQuotation()

        quoteUnquoteModel.markAsReported(WIDGET_ID)

        Assert.assertTrue("", quoteUnquoteModel.isReported(WIDGET_ID))
    }

    private fun getIntent(): Intent {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ActivityReport::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        return intent
    }

    @Test
    fun itemsInSpinner() {
        scenario = launchActivity(getIntent())

        scenario.onActivity { activity ->
            val spinnerReason: Spinner = activity.findViewById(R.id.spinnerReason)
            MatcherAssert.assertThat("", spinnerReason.adapter.count, Is.`is`(6))
        }
    }

    @Test
    fun hasQuotationAlreadyBeenReported() {
        scenario = launchActivity(getIntent())

        scenario.onActivity { activity ->
            assertFalse("", activity.hasQuotationAlreadyBeenReported())
        }
    }
}