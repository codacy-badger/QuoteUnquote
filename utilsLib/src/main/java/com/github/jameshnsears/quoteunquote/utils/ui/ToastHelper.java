package com.github.jameshnsears.quoteunquote.utils.ui;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ToastHelper {
    @Nullable
    public static Toast toast;

    public static void makeToast(@NonNull final Context context, @NonNull final String message, final int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, length);
        toast.show();
    }
}
