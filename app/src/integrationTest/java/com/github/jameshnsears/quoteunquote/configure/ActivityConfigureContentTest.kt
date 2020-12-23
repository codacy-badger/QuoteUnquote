package com.github.jameshnsears.quoteunquote.configure

import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.utils.sqlite.SqliteLoaderHelper
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ActivityConfigureContentTest : SqliteLoaderHelper() {
    @Test
    fun emptySearchResultsThenBackPressed() {
        // see https://github.com/robolectric/robolectric/pull/4736
        val scenario: ActivityScenario<ActivityConfigureDouble> = launchActivity(IntentHelper.getIntent())

        scenario.onActivity { activity ->
            activity.fragmentContent.fragmentContentBinding?.radioButtonSearch?.isChecked = true
            activity.onBackPressed()

            assertTrue(activity.fragmentContent.fragmentContentBinding?.radioButtonAll?.isChecked == true)
        }

        scenario.close()
    }
}