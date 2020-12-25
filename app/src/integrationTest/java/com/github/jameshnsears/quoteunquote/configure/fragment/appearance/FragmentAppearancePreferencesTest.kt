package com.github.jameshnsears.quoteunquote.configure.fragment.appearance

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.utils.logging.ShadowLoggingHelper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentAppearancePreferencesTest : ShadowLoggingHelper() {
    @Test
    fun confirmInitialPreferences() {
        // https://developer.android.com/training/basics/fragments/testing
        with(launchFragment<FragmentAppearanceDouble>()) {
            onFragment { fragment ->
                // http://hamcrest.org/JavaHamcrest/tutorial
                // https://www.vogella.com/tutorials/Hamcrest/article.html
                fragment.setTransparency()
                assertThat(fragment.preferenceAppearance?.appearanceTransparency, equalTo(2))

                fragment.setTextColour()
                assertEquals("#FF000000", fragment.preferenceAppearance?.appearanceTextColour)

                fragment.setTextSize()
                assertThat(fragment.preferenceAppearance?.appearanceTextSize, equalTo(16))

                assertTrue(fragment.preferenceAppearance?.appearanceToolbarFirst == false)
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarPrevious == true)
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarReport == true)
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarFavourite == true)
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarShare == true)
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarRandom == true)
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarSequential == false)
            }
        }
    }

    @Test
    fun confirmChangesToPreferences() {
        with(launchFragment<FragmentAppearanceDouble>()) {
            onFragment { fragment ->
                fragment.preferenceAppearance?.appearanceTransparency = 5
                fragment.setTransparency()
                assertEquals(5, fragment.preferenceAppearance?.appearanceTransparency)

                fragment.preferenceAppearance?.appearanceTextColour = "#FFFFFFFF"
                fragment.setTextColour()
                assertEquals("#FFFFFFFF", fragment.preferenceAppearance?.appearanceTextColour)

                fragment.preferenceAppearance?.appearanceTextSize = 32
                fragment.setTextSize()
                assertEquals(32, fragment.preferenceAppearance?.appearanceTextSize)

                fragment.fragmentAppearanceBinding?.toolbarSwitchFirst?.isChecked = true
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarFirst == true)

                fragment.fragmentAppearanceBinding?.toolbarSwitchPrevious?.isChecked = false
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarPrevious == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchReport?.isChecked = false
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarReport == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchToggleFavourite?.isChecked = false
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarFavourite == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchShare?.isChecked = false
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarShare == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchNextRandom?.isChecked = false
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarRandom == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchNextSequential?.isChecked = true
                assertTrue(fragment.preferenceAppearance?.appearanceToolbarSequential == true)
            }
        }
    }
}