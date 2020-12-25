package com.github.jameshnsears.quoteunquote.configure.fragment.content

import android.os.Bundle
import android.view.View
import com.github.jameshnsears.quoteunquote.configure.IntentHelper

class ContentFragmentDouble(widgetId: Int = IntentHelper.WIDGET_ID) : ContentFragment(widgetId) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createListenerRadioGroup()
        createListenerAuthor()
        createListenerFavouriteButtonSend()
        createListenerFavouriteButtonReceive()

        setSelection()

        enableFavouriteButtonReceive(true)

        // invoke rxjava usage through junit tests, else https://github.com/robolectric/robolectric/issues/4947
    }

    companion object {
        fun newInstance(widgetId: Int): ContentFragmentDouble {
            val fragment = ContentFragmentDouble(widgetId)
            fragment.arguments = null
            return fragment
        }
    }
}