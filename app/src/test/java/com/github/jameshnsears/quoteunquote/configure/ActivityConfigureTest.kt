package com.github.jameshnsears.quoteunquote.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ActivityConfigureTest {
    lateinit var scenario: ActivityScenario<ActivityConfigure>

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun `search radio selected, back pressed, all radio selected`() {
        val intent = Intent(getApplicationContext(), ActivityConfigure::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)

        scenario = launchActivity(intent)

        scenario.onActivity { activity ->
            activity.fragmentContent.fragmentContentBinding.radioButtonSearch.isChecked = true
            activity.onBackPressed()

            assertTrue(activity.fragmentContent.fragmentContentBinding.radioButtonAll.isChecked)
        }
    }
}