package com.example.rebelo.gcm_example;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.rebelo.gcm_example.backend.platformdata.Platformdata;
import com.example.rebelo.gcm_example.backend.platformdata.model.PlatformDataRecord;
import com.example.rebelo.gcm_example.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by rebelo on 13/05/2016.
 */
public class GCMRegistrationIntentService extends IntentService {

    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String DATA_COFFEE_SUCCESS = "DataCoffeeSuccess";
    public static final String DATA_COFFEE_ERROR = "DataCoffeeError";


    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_STATUS_COFFEE = "status_coffee";
    public static final String EXTRA_DATE_COFFEE = "date_coffee";
    public static final String EXTA_HOUR_COFFEE = "hour_coffee";

    public GCMRegistrationIntentService() {
        super(GCMRegistrationIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        Intent registrationComplete = null;
        Intent dataCoffee = null;
        String token = null;
        PlatformDataRecord platformDataCoffee;

        // SENDING TOKEN TO GOOGLE CLOUD
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra(EXTRA_TOKEN, token);

            sendRegistrationToServer(token);
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //  GETTING DATA IOT PLATFORM
        try {
            platformDataCoffee = receiveStatusCoffee();

            dataCoffee = new Intent(DATA_COFFEE_SUCCESS);
            dataCoffee.putExtra(EXTRA_STATUS_COFFEE, platformDataCoffee.getStatusCoffee());
            dataCoffee.putExtra(EXTRA_DATE_COFFEE, platformDataCoffee.getDate());
            dataCoffee.putExtra(EXTA_HOUR_COFFEE, platformDataCoffee.getHour());
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Data error");
            dataCoffee = new Intent(DATA_COFFEE_ERROR);
        }

        // Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataCoffee);
    }

    private void sendRegistrationToServer(String token) throws IOException {
        Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl("https://gcm-example-c5203.appspot.com/_ah/api/");
        Registration regService = builder.build();
        regService.reigster(token).execute();
        Log.w("**** MainActivity", "REGISTER LOG IN BACKEND");
    }

    private PlatformDataRecord receiveStatusCoffee() throws IOException {
        Platformdata.Builder builder = new Platformdata.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl("https://gcm-example-c5203.appspot.com/_ah/api/");
        Platformdata platformService = builder.build();
        PlatformDataRecord regPlatformData = platformService.getPlatformData().execute();
        System.out.println("***** APP STATUS COFFEE: " + regPlatformData.getStatusCoffee());
        System.out.println("***** APP DATA COFFEE: " + regPlatformData.getDate());
        System.out.println("***** APP HOUR COFFEE: " + regPlatformData.getHour());

        return regPlatformData;
    }
}
