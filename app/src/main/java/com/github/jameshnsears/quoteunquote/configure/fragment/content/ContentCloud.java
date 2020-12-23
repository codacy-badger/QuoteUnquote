package com.github.jameshnsears.quoteunquote.configure.fragment.content;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.jameshnsears.quoteunquote.cloud.CloudServiceReceive;

public class ContentCloud {
    @Nullable
    public CloudServiceReceive cloudServiceReceive;
    public boolean isServiceReceiveBound;

    @NonNull
    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            final CloudServiceReceive.LocalBinder binder = (CloudServiceReceive.LocalBinder) service;

            if (cloudServiceReceive != null) {
                cloudServiceReceive = binder.getService();
                isServiceReceiveBound = true;
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            isServiceReceiveBound = false;
        }
    };
}
