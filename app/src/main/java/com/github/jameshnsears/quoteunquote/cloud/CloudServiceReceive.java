package com.github.jameshnsears.quoteunquote.cloud;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.FragmentContent;
import com.github.jameshnsears.quoteunquote.database.DatabaseRepository;
import com.github.jameshnsears.quoteunquote.ui.ToastHelper;

import java.util.List;

public final class CloudServiceReceive extends Service {
    private final IBinder binder = new LocalBinder();

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public IBinder onBind(final Intent intent) {
        return binder;
    }

    private void showNoNetworkToast(final Context context) {
        handler.post(() -> ToastHelper.makeToast(
                context,
                context.getString(R.string.fragment_content_favourites_share_comms),
                Toast.LENGTH_SHORT));
    }

    public void receive(final FragmentContent fragmentContent, final String remoteCodeValue) {
        new Thread(() -> {
            final Context context = CloudServiceReceive.this.getApplicationContext();

            final CloudFavourites cloudFavourites = new CloudFavourites();

            try {
                if (!cloudFavourites.isInternetAvailable()) {
                    showNoNetworkToast(context);
                } else {
                    handler.post(() -> ToastHelper.makeToast(
                            context,
                            context.getString(R.string.fragment_content_favourites_share_receiving),
                            Toast.LENGTH_LONG));

                    final List<String> favouritesReceived = cloudFavourites.receive(
                            CloudFavourites.TIMEOUT,
                            CloudFavouritesHelper.receiveRequest(remoteCodeValue));

                    if (favouritesReceived.isEmpty()) {
                        handler.post(() -> ToastHelper.makeToast(
                                context, context.getString(R.string.fragment_content_favourites_share_missing), Toast.LENGTH_LONG));
                    } else {
                        handler.post(() -> ToastHelper.makeToast(
                                context, context.getString(R.string.fragment_content_favourites_share_received), Toast.LENGTH_LONG));

                        final DatabaseRepository databaseRepository = new DatabaseRepository(context);
                        favouritesReceived.forEach(databaseRepository::markAsFavourite);

                        if (fragmentContent != null) {
                            fragmentContent.setFavouriteCount();
                            fragmentContent.enableFavouriteButtonReceive(true);
                        }

                        CloudFavouritesHelper.auditFavourites("FAVOURITE_RECEIVE", remoteCodeValue);
                    }
                }
            } finally {
                cloudFavourites.shutdown();
            }
        }).start();
    }

    public class LocalBinder extends Binder {
        public CloudServiceReceive getService() {
            return CloudServiceReceive.this;
        }
    }
}
