package com.github.jameshnsears.quoteunquote.preference

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PreferenceHelperTest {
    val preferenceFilename = "preferenceFilename"
    lateinit var preferenceHelper: PreferenceHelper
    lateinit var applicationContext: Context

    @Before
    fun init() {
        applicationContext = getApplicationContext()
        preferenceHelper = PreferenceHelper(preferenceFilename, applicationContext)
    }

    @Test
    fun logging() {
        preferenceHelper.setPreference("1:int", 123)
        val lastLog = ShadowLog.getLogs()[2]
        assertTrue(lastLog.tag == "PreferenceHelper.setPreference, 71")
        assertTrue(lastLog.msg == "1:int=123")
    }

    @Test
    fun api() {
        preferenceHelper.setPreference("1:int", 234)
        assertTrue(preferenceHelper.getPreferenceInt("1:int") == 234)

        preferenceHelper.setPreference("2:boolean", true)
        assertTrue(preferenceHelper.getPreferenceBoolean("2:boolean", true))
        assertFalse(preferenceHelper.getPreferenceBoolean("boolean-default", false))

        preferenceHelper.setPreference("3:string", "abc")
        assertTrue(preferenceHelper.getPreferenceString("3:string") == "abc")

        PreferenceHelper.empty(preferenceFilename, applicationContext, 1)
        assertTrue(preferenceHelper.getPreferenceInt("1:int") == -1)

        PreferenceHelper.empty(preferenceFilename, applicationContext)
        assertTrue(preferenceHelper.getPreferenceString("3:string") == "")
    }
}