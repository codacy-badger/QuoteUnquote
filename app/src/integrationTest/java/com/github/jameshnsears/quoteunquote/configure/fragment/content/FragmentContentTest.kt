package com.github.jameshnsears.quoteunquote.configure.fragment.content

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.utils.BuildConfig
import com.github.jameshnsears.quoteunquote.utils.ContentSelection
import com.github.jameshnsears.quoteunquote.utils.sqlite.SqliteLoaderHelper
import junit.framework.Assert.fail
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentContentTest : SqliteLoaderHelper() {
    @Test
    fun changeContentSelection() {
        with(launchFragment<FragmentContentDouble>()) {
            onFragment { fragment ->
                // default content selection
                assertTrue(fragment.preferenceContent?.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.SEARCH)

                fragment.preferenceContent?.contentSelection = ContentSelection.AUTHOR
                fragment.setSelection()
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.ALL)
                assertTrue(fragment.preferenceContent?.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.SEARCH)

                fragment.preferenceContent?.contentSelection = ContentSelection.FAVOURITES
                fragment.setSelection()
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.AUTHOR)
                assertTrue(fragment.preferenceContent?.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.SEARCH)

                fragment.preferenceContent?.contentSelection = ContentSelection.SEARCH
                fragment.setSelection()
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.FAVOURITES)
                assertTrue(fragment.preferenceContent?.contentSelection == ContentSelection.SEARCH)
            }
        }
    }

    @Test
    fun confirmInitialContentSelections() {
        with(launchFragment<FragmentContentDouble>()) {
            onFragment { fragment ->
                assertTrue(fragment.preferenceContent?.contentSelection == ContentSelection.ALL)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.AUTHOR)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.FAVOURITES)
                assertFalse(fragment.preferenceContent?.contentSelection == ContentSelection.SEARCH)

                fragment.setAllCount()
                fragment.disposableCompletedAllCount.await()
                if (BuildConfig.DEBUG) {
                    assertEquals("", "All: 7", fragment.fragmentContentBinding?.radioButtonAll?.text.toString())
                }

                fragment.setAuthor()
                fragment.disposableCompletedSetAuthor.await()
                if (BuildConfig.DEBUG) {
                    assertEquals("", "Author: 1", fragment.fragmentContentBinding?.radioButtonAuthor?.text.toString())
                }

                fragment.setFavouriteCount()
                fragment.disposableCompletedSetFavouriteCount.await()
                if (BuildConfig.DEBUG) {
                    assertEquals("", "Favourites: 0", fragment.fragmentContentBinding?.radioButtonFavourites?.text.toString())
                }

                fragment.setFavouriteLocalCode()
                assertTrue(fragment.fragmentContentBinding?.textViewLocalCodeValue?.text.toString().length == 10)

                fragment.setSearch()
                assertEquals("", "", fragment.fragmentContentBinding?.editTextSearchText?.text.toString())

            }
        }
    }

    @Test
    fun `todo - authorSearch`() {
        fail("todo")
    }

    @Test
    fun `todo - favouritesSend`() {
        fail("todo")
    }

    @Test
    fun `todo - favouritesReceive`() {
        fail("todo")
    }
}