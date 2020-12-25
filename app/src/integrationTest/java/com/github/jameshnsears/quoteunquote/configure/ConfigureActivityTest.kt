package com.github.jameshnsears.quoteunquote.configure

import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.R
import com.github.jameshnsears.quoteunquote.configure.fragment.event.EventFragment
import com.github.jameshnsears.quoteunquote.utils.sqlite.SqliteLoaderHelper
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ConfigureActivityTest : SqliteLoaderHelper() {
    // ActivityConfigureDouble: see https://github.com/robolectric/robolectric/pull/4736
    lateinit var scenario: ActivityScenario<ConfigureActivityDouble>

    @Before
    fun before() {
        scenario = launchActivity(IntentHelper.getIntent())
    }

    @After
    fun after() {
        scenario.close()
    }

    @Test
    fun emptySearchResultsThenBackPressed() {
        scenario.onActivity { activity ->
            activity.fragmentContent.fragmentContentBinding?.radioButtonSearch?.isChecked = true
            activity.onBackPressed()

            assertTrue(activity.fragmentContent.fragmentContentBinding?.radioButtonAll?.isChecked == true)
        }
    }

    @Test
    fun fragmentEvent() {
        scenario.onActivity { activity ->
            val fragmentEvent = activity.supportFragmentManager.findFragmentById(R.id.fragmentPlaceholderEvent) as EventFragment
            Assert.assertFalse("", fragmentEvent.fragmentEventBinding!!.checkBoxDailyAt.isChecked)
            Assert.assertFalse("", fragmentEvent.fragmentEventBinding!!.checkBoxDeviceUnlock.isChecked)
        }
    }
}