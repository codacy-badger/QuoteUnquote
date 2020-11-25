package com.github.jameshnsears.quoteunquote.ui;

import android.content.Context;
import android.widget.Toast;

public final class ToastHelper {
    public static Toast toast;

    public static void makeToast(final Context context, final String message, final int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, length);
        toast.show();
    }
}
