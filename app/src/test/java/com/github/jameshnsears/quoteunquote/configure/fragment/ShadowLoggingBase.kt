package com.github.jameshnsears.quoteunquote.configure.fragment

import org.junit.Before
import org.robolectric.shadows.ShadowLog

open class ShadowLoggingBase {
    @Before
    fun logging() {
        ShadowLog.stream = System.out;
    }
}