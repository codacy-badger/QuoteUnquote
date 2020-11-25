package com.github.jameshnsears.quoteunquote.configure.fragment.event

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.configure.fragment.ShadowLoggingBase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentEventTest : ShadowLoggingBase() {
    class FragmentEventDouble : FragmentEvent {
        constructor() : super(1)
    }

    @Test
    fun checkDefaultPreferences() {
        with(launchFragment<FragmentEventDouble>()) {
            onFragment { fragment ->
                assertFalse(fragment.preferenceEvent.eventDeviceUnlock)

                assertFalse(fragment.preferenceEvent.eventDaily)
                assertThat(fragment.preferenceEvent.eventDailyTimeHour, IsEqual.equalTo(6))
                assertThat(fragment.preferenceEvent.eventDailyTimeMinute, IsEqual.equalTo(0))
            }
        }
    }

    @Test
    fun checkAlteredPreferences() {
        with(launchFragment<FragmentEventDouble>()) {
            onFragment { fragment ->
                fragment.preferenceEvent.eventDaily = true
                fragment.preferenceEvent.eventDailyTimeHour = 16
                fragment.preferenceEvent.eventDailyTimeMinute = 30

                fragment.setDailyTime()

                assertThat(fragment.preferenceEvent.eventDailyTimeHour, IsEqual.equalTo(16))
                assertThat(fragment.preferenceEvent.eventDailyTimeMinute, IsEqual.equalTo(30))
            }
        }
    }
}