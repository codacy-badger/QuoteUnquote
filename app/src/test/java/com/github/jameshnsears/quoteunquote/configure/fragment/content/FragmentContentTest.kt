package com.github.jameshnsears.quoteunquote.configure.fragment.content

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.configure.fragment.FragmentShadowLogging
import com.github.jameshnsears.quoteunquote.utils.ContentSelection
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentContentTest : FragmentShadowLogging() {
    class FragmentContentDouble : FragmentContent {
        constructor() : super(1)
    }

    @Test
    fun checkDefaultPreferences() {
        with(launchFragment<FragmentContentDouble>()) {
            onFragment { fragment ->
                assertEquals(ContentSelection.ALL, fragment.preferenceContent.contentSelection)
                assertTrue(fragment.preferenceContent.contentSelection == ContentSelection.ALL)

                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.AUTHOR)
                assertEquals("", fragment.preferenceContent.contentSelectionAuthorName)

                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.FAVOURITES)
                assertTrue(fragment.preferenceContent.contentFavouritesLocalCode.length == 10)

                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.SEARCH)
                assertEquals("", fragment.preferenceContent.contentSelectionSearchText)
            }
        }
    }

    @Test
    fun checkAlteredPreferences() {
        with(launchFragment<FragmentContentDouble>()) {
            onFragment { fragment ->
                fragment.preferenceContent.contentSelection = ContentSelection.ALL
                fragment.setSelection()
                assertTrue(fragment.preferenceContent.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.SEARCH)

                fragment.preferenceContent.contentSelection = ContentSelection.AUTHOR
                fragment.setSelection()
                assertTrue(fragment.preferenceContent.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.SEARCH)

                fragment.preferenceContent.contentSelectionAuthorName = "author"
                assertTrue(fragment.preferenceContent.contentSelectionAuthorName == "author")

                fragment.preferenceContent.contentSelection = ContentSelection.FAVOURITES
                fragment.setSelection()
                assertTrue(fragment.preferenceContent.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.SEARCH)

                fragment.preferenceContent.contentSelection = ContentSelection.SEARCH
                fragment.setSelection()
                assertTrue(fragment.preferenceContent.contentSelection == ContentSelection.SEARCH)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent.contentSelection == ContentSelection.FAVOURITES)
            }
        }
    }

    @Test
    fun `authorSearch`() {
        Assert.fail("todo")
    }

    @Test
    fun `favouritesSend`() {
        Assert.fail("todo")
    }

    @Test
    fun `favouritesReceive`() {
        Assert.fail("todo")
    }
}