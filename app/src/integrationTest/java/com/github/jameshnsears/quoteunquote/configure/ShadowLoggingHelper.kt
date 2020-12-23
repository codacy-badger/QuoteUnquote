package com.github.jameshnsears.quoteunquote.configure

import org.junit.Before
import org.robolectric.shadows.ShadowLog

open class ShadowLoggingHelper {
    @Before
    fun logging() {
        ShadowLog.stream = System.out
    }
}