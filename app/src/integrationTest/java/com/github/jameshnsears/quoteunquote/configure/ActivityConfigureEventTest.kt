package com.github.jameshnsears.quoteunquote.configure

import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.R
import com.github.jameshnsears.quoteunquote.configure.fragment.event.FragmentEvent
import com.github.jameshnsears.quoteunquote.utils.sqlite.SqliteLoaderHelper
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Ignore

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ActivityConfigureEventTest : SqliteLoaderHelper() {
    @Test
    fun fragmentEvent() {
        val scenario: ActivityScenario<ActivityConfigure> = launchActivity(IntentHelper.getIntent())

        scenario.onActivity { activity ->
            val fragmentEvent = activity.supportFragmentManager.findFragmentById(R.id.fragmentPlaceholderEvent) as FragmentEvent
            Assert.assertFalse("", fragmentEvent.fragmentEventBinding!!.checkBoxDailyAt.isChecked)
            Assert.assertFalse("", fragmentEvent.fragmentEventBinding!!.checkBoxDeviceUnlock.isChecked)
        }

        scenario.close()
    }
}