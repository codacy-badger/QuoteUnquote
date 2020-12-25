package com.github.jameshnsears.quoteunquote.configure.fragment.event

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.utils.logging.ShadowLoggingHelper
import junit.framework.TestCase.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentEventPreferencesTest : ShadowLoggingHelper() {
    @Test
    fun `confirm initial preferences`() {
        with(launchFragment<FragmentEventDouble>()) {
            onFragment { fragment ->
                assertTrue(fragment.preferenceEvent?.eventDeviceUnlock == false)

                assertTrue(fragment.preferenceEvent?.eventDaily == false)
                assertThat(fragment.preferenceEvent?.eventDailyTimeHour, IsEqual.equalTo(6))
                assertThat(fragment.preferenceEvent?.eventDailyTimeMinute, IsEqual.equalTo(0))
            }
        }
    }

    @Test
    fun `confirm changes to preferences`() {
        with(launchFragment<FragmentEventDouble>()) {
            onFragment { fragment ->
                fragment.preferenceEvent?.eventDaily = true
                fragment.preferenceEvent?.eventDailyTimeHour = 16
                fragment.preferenceEvent?.eventDailyTimeMinute = 30

                fragment.setDailyTime()

                assertThat(fragment.preferenceEvent?.eventDailyTimeHour, IsEqual.equalTo(16))
                assertThat(fragment.preferenceEvent?.eventDailyTimeMinute, IsEqual.equalTo(30))
            }
        }
    }
}