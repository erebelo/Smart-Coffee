package com.example.rebelo.gcm_example;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by rebelo on 13/05/2016.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService{
    /**
     * When token refresh, start service to get new token
     */
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
