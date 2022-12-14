package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.rowdystudio.fffdiamondsandelitespass.App;
import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.models.User;
import com.rowdystudio.fffdiamondsandelitespass.utils.BaseUrl;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import com.rowdystudio.fffdiamondsandelitespass.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplashActivity extends AppCompatActivity {
    boolean LOGIN = false;
    private AppUpdateManager appUpdateManager;
    public static final int RC_APP_UPDATE = 101;
    SplashActivity activity;
    String user_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));
        String is_login = Constant.getString(activity, Constant.IS_LOGIN);
        if (is_login.equals("true")) {
            LOGIN = true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "onCreate:if part activarte ");
            appUpdateManager = AppUpdateManagerFactory.create(this);
            UpdateApp();
        } else {
            Log.e("TAG", "onCreate:else part activarte ");
            onInit();
        }
    }


    private void onInit() {
        if (Constant.isNetworkAvailable(activity)) {
            if (LOGIN) {
                try {
                    String tag_json_obj = "json_login_req";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("get_login", "any");
                    params.put("email", Constant.getString(activity, Constant.USER_EMAIL));
                    params.put("password", Constant.getString(activity, Constant.USER_PASSWORD));

                    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                            BaseUrl.LOGIN_API, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    JSONObject jsonObject = response.getJSONObject("0");
                                    Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                                    final User user = new User(jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("email"), jsonObject.getString("points"), jsonObject.getString("referraled_with"), jsonObject.getString("status"), jsonObject.getString("referral_code"));

                                    if (user.getName() != null) {
                                        Constant.setString(activity, Constant.USER_NAME, user.getName());
                                        Log.e("TAG", "onDataChange: " + user.getName());
                                    }
                                    if (user.getNumber() != null) {
                                        Constant.setString(activity, Constant.USER_NUMBER, user.getNumber());
                                        Log.e("TAG", "onDataChange: " + user.getNumber());
                                    }
                                    if (user.getEmail() != null) {
                                        Constant.setString(activity, Constant.USER_EMAIL, user.getEmail());
                                        Log.e("TAG", "onDataChange: " + user.getEmail());
                                    }
                                    if (user.getPoints() != null) {
                                        if (!Constant.getString(activity, Constant.LAST_TIME_ADD_TO_SERVER).equals("")) {
                                            Log.e("TAG", "onDataChange: Last time not empty");
                                            if (!Constant.getString(activity, Constant.USER_POINTS).equals("")) {
                                                Log.e("TAG", "onDataChange: user points not empty");
                                                if (Constant.getString(activity, Constant.IS_UPDATE).equalsIgnoreCase("")) {
                                                    Constant.setString(activity, Constant.USER_POINTS, Constant.getString(activity, Constant.USER_POINTS));
                                                    Log.e("TAG", "onDataChange: " + user.getPoints());
                                                    Constant.setString(activity, Constant.IS_UPDATE, "true");
                                                } else {
                                                    Constant.setString(activity, Constant.IS_UPDATE, "true");
                                                    Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                                                    Log.e("TAG", "onDataChange: " + user.getPoints());
                                                }
                                            }
                                        }
                                    }
                                    if (user.getReferCode() != null) {
                                        Constant.setString(activity, Constant.REFER_CODE, user.getReferCode());
                                        Log.e("TAG", "onDataChange: " + user.getReferCode());
                                    }
                                    if (user.getIsBLocked() != null) {
                                        Constant.setString(activity, Constant.USER_BLOCKED, user.getIsBLocked());
                                        Log.e("TAG", "onDataChange: " + user.getIsBLocked());
                                    }
                                    if (user.getUserReferCode() != null) {
                                        Constant.setString(activity, Constant.USER_REFFER_CODE, user.getUserReferCode());
                                        Log.e("TAG", "onDataChange: " + user.getUserReferCode());
                                    }

                                    if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                                        Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
                                    } else {
                                        Log.e("TAG", "onInit: login pART");
                                        Constant.GotoNextActivity(activity, MainActivity.class, "");
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                        finish();
                                    }
                                } else {
                                    Log.e("TAG", "onInit: user_information from database");
                                    Constant.setString(activity, Constant.IS_LOGIN, "");
                                    Constant.GotoNextActivity(activity, LoginActivity.class, "");
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            VolleyLog.d("TAG", "Error: " + error.getMessage());
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                            }
                        }
                    });
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            1000 * 20,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    // Adding request to request queue
                    App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                } catch (Exception e) {
                    Log.e("TAG", "onInit: excption " + e.getMessage().toString());
                }
            } else {
                if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                    Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
                    return;
                }
                Log.e("TAG", "onInit: else part of no login");
                Constant.GotoNextActivity(activity, LoginActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
    }

    public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                            Log.e("TAG", "onCreate:startUpdateFlowForResult part activarte ");
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", "onCreate:startUpdateFlowForResult else part activarte ");
                        activity.onInit();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onCreate:addOnFailureListener else part activarte ");
                    activity.onInit();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                onInit();
            } else {
                onInit();
            }
        }
    }
}