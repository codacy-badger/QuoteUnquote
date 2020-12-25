package com.github.jameshnsears.quoteunquote.configure

import androidx.annotation.NonNull
import com.github.jameshnsears.quoteunquote.configure.fragment.content.FragmentContentDouble

class ActivityConfigureDouble : ActivityConfigure() {
    @NonNull
    override fun getFragmentContentNewInstance(): FragmentContentDouble {
        return FragmentContentDouble.newInstance(widgetId)
    }

    override fun broadcastFinishIntent() {
        // don't broadcast anything for test(s)
    }
}