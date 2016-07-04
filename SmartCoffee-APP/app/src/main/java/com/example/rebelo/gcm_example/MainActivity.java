package com.example.rebelo.gcm_example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import io.fabric.sdk.android.Fabric;

/**
 * Created by rebelo on 13/05/2016.
 */
public class MainActivity extends Activity implements OnRefreshListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String statusCoffee;
    private String dateCoffee;
    private String hourCoffee;
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used

//        swipeView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeView.setRefreshing(true);
//            }
//        }, 1000);

        getDataBroadcast();

    }

    public void getDataBroadcast() {
        final TextView viewStatusCode = (TextView) findViewById(R.id.text_status);
        final TextView viewStatusWaiting = (TextView) findViewById(R.id.text_waiting);
        final TextView viewDate = (TextView) findViewById(R.id.text_date);
        final TextView viewHour = (TextView) findViewById(R.id.text_hour);
        final ImageView viewImage = (ImageView) findViewById(R.id.image_status);

        if (!isNetworkConnected()) {
            alertAndroidOptions("Sem conexão com internet", "Conecte-se a uma rede e tente novamente!");
            //Toast.makeText(getApplicationContext(), "CONNECTION INTERNET: " + isNetworkConnected(), Toast.LENGTH_LONG).show();
        } else {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Check type of intent filter
                    if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                        //Registration success
                        String token = intent.getStringExtra(GCMRegistrationIntentService.EXTRA_TOKEN);
                    /*Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();*/
                    } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                        //Registration error
                        Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                    } else {
                        //Tobe define
                    }

                    if (intent.getAction().equals(GCMRegistrationIntentService.DATA_COFFEE_SUCCESS)) {
                        statusCoffee = intent.getStringExtra(GCMRegistrationIntentService.EXTRA_STATUS_COFFEE);
                        dateCoffee = intent.getStringExtra(GCMRegistrationIntentService.EXTRA_DATE_COFFEE);
                        hourCoffee = intent.getStringExtra(GCMRegistrationIntentService.EXTA_HOUR_COFFEE);
                        Toast.makeText(getApplicationContext(), "*** 1 - Status Coffee: " + statusCoffee + "\nDate: " + dateCoffee + "\nHour: " + hourCoffee, Toast.LENGTH_LONG).show();

                        switch (statusCoffee) {
                            case "1":
                                viewDate.setVisibility(View.INVISIBLE);
                                viewHour.setVisibility(View.INVISIBLE);
                                viewStatusWaiting.setVisibility(View.VISIBLE);

                                viewStatusCode.setText("Cafeteira disponível!");
                                viewStatusWaiting.setText("Prepare um delicioso café");
                                viewImage.setImageResource(R.drawable.coffee_grains);
                                break;
                            case "2":
                                viewStatusWaiting.setVisibility(View.INVISIBLE);
                                viewDate.setVisibility(View.VISIBLE);
                                viewHour.setVisibility(View.VISIBLE);

                                viewStatusCode.setText("Preparando o café!");
                                viewDate.setText(dateCoffee);
                                viewHour.setText(hourCoffee);
                                viewImage.setImageResource(R.drawable.coffee_drink);
                                break;
                            case "3":
                                viewStatusWaiting.setVisibility(View.INVISIBLE);
                                viewDate.setVisibility(View.VISIBLE);
                                viewHour.setVisibility(View.VISIBLE);

                                viewStatusCode.setText("Café pronto!");
                                viewDate.setText(dateCoffee);
                                viewHour.setText(hourCoffee);
                                viewImage.setImageResource(R.drawable.coffee_food);
                                break;
                        }

                    } else if (intent.getAction().equals(GCMRegistrationIntentService.DATA_COFFEE_ERROR)) {
                        //Registration error
                        Toast.makeText(getApplicationContext(), "Data coffee platform error!!!", Toast.LENGTH_LONG).show();
                    }
                }
            };

            //Check status of Google play service in device
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (ConnectionResult.SUCCESS != resultCode) {
                //Check type of error
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                    //So notification
                    GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                } else {
                    Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                }
            } else {
                //Start service
                Intent itent = new Intent(this, GCMRegistrationIntentService.class);
                startService(itent);
            }
        }
    }

    private void finalizeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void alertAndroidOptions(String title, String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();
                    }
                });
                /*
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.DATA_COFFEE_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.DATA_COFFEE_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onRefresh() {
        swipeView.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
                finalizeActivity();
            }
        }, 1000);

    }
}