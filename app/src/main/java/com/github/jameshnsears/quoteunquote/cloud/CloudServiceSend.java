package com.github.jameshnsears.quoteunquote.cloud;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.ui.ToastHelper;

public final class CloudServiceSend extends Service {
    private final Handler handler = new Handler(Looper.getMainLooper());

    public static boolean isRunning(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (final ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(CloudServiceSend.class.getName())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    private void showNoNetworkToast(final Context context) {
        handler.post(() -> ToastHelper.makeToast(
                context,
                context.getString(R.string.fragment_content_favourites_share_comms),
                Toast.LENGTH_SHORT));
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        new Thread(() -> {
            final Context context = CloudServiceSend.this.getApplicationContext();

            final CloudFavourites cloudFavourites = new CloudFavourites();

            try {
                if (!cloudFavourites.isInternetAvailable()) {
                    showNoNetworkToast(context);
                } else {
                    handler.post(() -> ToastHelper.makeToast(
                            context,
                            context.getString(R.string.fragment_content_favourites_share_sending),
                            Toast.LENGTH_LONG));

                    if (cloudFavourites.save(intent.getStringExtra("savePayload"))) {

                        handler.post(() -> ToastHelper.makeToast(
                                context,
                                context.getString(R.string.fragment_content_favourites_share_sent, intent.getStringExtra("localCodeValue")),
                                Toast.LENGTH_LONG));

                        CloudFavouritesHelper.auditFavourites(
                                "FAVOURITE_SEND",
                                intent.getStringExtra("localCodeValue"));
                    } else {
                        showNoNetworkToast(context);
                    }
                }

                stopSelf();
            } finally {
                cloudFavourites.shutdown();
            }

        }).start();

        return START_NOT_STICKY;
    }
}
