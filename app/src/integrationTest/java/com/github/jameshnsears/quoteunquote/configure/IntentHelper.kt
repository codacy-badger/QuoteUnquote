package com.github.jameshnsears.quoteunquote.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.test.core.app.ApplicationProvider

class IntentHelper {
    companion object {
        const val WIDGET_ID = -1

        fun getIntent(): Intent {
            val intent = Intent(ApplicationProvider.getApplicationContext(), ConfigureActivityDouble::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WIDGET_ID)
            return intent
        }
    }
}