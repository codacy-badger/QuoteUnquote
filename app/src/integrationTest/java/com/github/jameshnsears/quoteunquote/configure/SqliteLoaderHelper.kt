package com.github.jameshnsears.quoteunquote.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.guness.robolectric.sqlite.library.SQLiteLibraryLoader
import org.junit.Before


open class SqliteLoaderHelper : ShadowLoggingHelper() {
    @Before
    @CallSuper
    fun setUp() {
        // fix for https://github.com/robolectric/robolectric/issues/4209
        SQLiteLibraryLoader.load()
    }

    protected fun getIntent(): Intent {
        val intent = Intent(getApplicationContext(), ActivityConfigureDouble::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        return intent
    }
}