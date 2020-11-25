package com.github.jameshnsears.quoteunquote.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ActivityConfigureTest {
    @Test
    fun f() {
        val scenario = launchActivity<ActivityConfigure>()

        scenario.onActivity { activity ->
            assertTrue(activity.t() == 1);
        }
    }
}