package com.github.jameshnsears.quoteunquote.utils.sqlite

import androidx.annotation.CallSuper
import com.github.jameshnsears.quoteunquote.utils.logging.ShadowLoggingHelper
import com.guness.robolectric.sqlite.library.SQLiteLibraryLoader
import org.junit.Before


open class SqliteLoaderHelper : ShadowLoggingHelper() {
    @Before
    @CallSuper
    fun setUp() {
        // fix for https://github.com/robolectric/robolectric/issues/4209
        SQLiteLibraryLoader.load()
    }
}