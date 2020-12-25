package com.github.jameshnsears.quoteunquote.configure.fragment.content

import android.os.Bundle
import android.view.View
import com.github.jameshnsears.quoteunquote.configure.IntentHelper

class FragmentContentDouble(widgetId: Int = IntentHelper.WIDGET_ID) : FragmentContent(widgetId) {
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
        fun newInstance(widgetId: Int): FragmentContentDouble {
            val fragment = FragmentContentDouble(widgetId)
            fragment.arguments = null
            return fragment
        }
    }
}