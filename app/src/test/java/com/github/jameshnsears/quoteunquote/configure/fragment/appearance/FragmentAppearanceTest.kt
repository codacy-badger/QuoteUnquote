package com.github.jameshnsears.quoteunquote.configure.fragment.appearance

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.configure.fragment.FragmentShadowLogging
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentAppearanceTest : FragmentShadowLogging() {
    class FragmentAppearanceDouble : FragmentAppearance {
        constructor() : super(1)
    }

    @Test
    fun checkDefaultPreferences() {
        // https://developer.android.com/training/basics/fragments/testing
        with(launchFragment<FragmentAppearanceDouble>()) {
            onFragment { fragment ->
                // http://hamcrest.org/JavaHamcrest/tutorial
                // https://www.vogella.com/tutorials/Hamcrest/article.html
                fragment.setTransparency()
                assertThat(fragment.preferenceAppearance.appearanceTransparency, equalTo(2))

                fragment.setTextColour()
                assertEquals("#FF000000", fragment.preferenceAppearance.appearanceTextColour)

                fragment.setTextSize()
                assertThat(fragment.preferenceAppearance.appearanceTextSize, equalTo(16))

                assertFalse(fragment.preferenceAppearance.appearanceToolbarFirst)
                assertTrue(fragment.preferenceAppearance.appearanceToolbarPrevious)
                assertTrue(fragment.preferenceAppearance.appearanceToolbarReport)
                assertTrue(fragment.preferenceAppearance.appearanceToolbarFavourite)
                assertTrue(fragment.preferenceAppearance.appearanceToolbarShare)
                assertTrue(fragment.preferenceAppearance.appearanceToolbarRandom)
                assertFalse(fragment.preferenceAppearance.appearanceToolbarSequential)
            }
        }
    }

    @Test
    fun checkAlteredPreferences() {
        with(launchFragment<FragmentAppearanceDouble>()) {
            onFragment { fragment ->
                fragment.preferenceAppearance.appearanceTransparency = 5
                fragment.setTransparency()
                assertEquals(5, fragment.preferenceAppearance.appearanceTransparency)

                fragment.preferenceAppearance.appearanceTextColour = "#FFFFFFFF"
                fragment.setTextColour()
                assertEquals("#FFFFFFFF", fragment.preferenceAppearance.appearanceTextColour)

                fragment.preferenceAppearance.appearanceTextSize = 32
                fragment.setTextSize()
                assertEquals(32, fragment.preferenceAppearance.appearanceTextSize)

                fragment.preferenceAppearance.appearanceToolbarFirst = false
                assertFalse(fragment.preferenceAppearance.appearanceToolbarFirst)

                fragment.preferenceAppearance.appearanceToolbarPrevious = true
                assertTrue(fragment.preferenceAppearance.appearanceToolbarPrevious)

                fragment.preferenceAppearance.appearanceToolbarReport = true
                assertTrue(fragment.preferenceAppearance.appearanceToolbarReport)

                fragment.preferenceAppearance.appearanceToolbarFavourite = true
                assertTrue(fragment.preferenceAppearance.appearanceToolbarFavourite)

                fragment.preferenceAppearance.appearanceToolbarShare = true
                assertTrue(fragment.preferenceAppearance.appearanceToolbarShare)

                fragment.preferenceAppearance.appearanceToolbarRandom = true
                assertTrue(fragment.preferenceAppearance.appearanceToolbarRandom)

                fragment.preferenceAppearance.appearanceToolbarSequential = false
                assertFalse(fragment.preferenceAppearance.appearanceToolbarSequential)
            }
        }
    }
}