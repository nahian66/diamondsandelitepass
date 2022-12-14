package com.rowdystudio.fffdiamondsandelitespass;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AudienceNetworkAds;
import com.rowdystudio.fffdiamondsandelitespass.services.SoundService;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;


public class App extends Application implements LifecycleObserver {
    private static App mInstance;
    private boolean isPlaying = false;
    public static final String TAG = App.class.getSimpleName();

    private RequestQueue mRequestQueue;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        String typeOfAds = getResources().getString(R.string.ad_network);
        switch (typeOfAds) {
            case "facebook":
                StartAppAd.disableSplash();
                AudienceNetworkAds.initialize(mInstance);
                break;
            case "admob":
                StartAppAd.disableSplash();
                MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                        Log.e("TAG", "onInitializationComplete: Initialize Successfully...");
                    }
                });
                break;
            case "startapp":
                StartAppSDK.setUserConsent(this,
                        "pas",
                        System.currentTimeMillis(),
                        false);
                StartAppSDK.init(mInstance, getResources().getString(R.string.startapp_app_id), false);
                StartAppAd.disableSplash();
                break;
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(mInstance);
        startService();
    }

    public static App getContext() {
        return mInstance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppInForGround() {
        Log.e("MyApp", "App in for ground");
        startService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.e("MyApp", "App in background");
        stopService();
    }

    @Override
    public void onTerminate() {
        if (isPlaying) {
            stopService();
        }
        Log.e("TAG", "onTerminate: onTerminateCAlled");
        super.onTerminate();
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public static void startService() {
        if (Constant.getString(getInstance(), Constant.SOUND_VALUE).equals("")) {
            Log.e("MyApp", "Start Service");
            mInstance.isPlaying = true;
            getInstance().startService(new Intent(getInstance(), SoundService.class));
        }
    }

    public static void stopService() {
        Log.e("MyApp", "Stop Service");
        try {
            mInstance.isPlaying = false;
            getInstance().stopService(new Intent(getInstance(), SoundService.class));
        } catch (Exception e) {
            Log.e("TAG", "stopService: " + e.getMessage());
        }
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}
