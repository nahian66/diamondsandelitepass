package com.rowdystudio.fffdiamondsandelitespass.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.rowdystudio.fffdiamondsandelitespass.App;
import com.rowdystudio.fffdiamondsandelitespass.utils.BaseUrl;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;
import com.rowdystudio.fffdiamondsandelitespass.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PointsService extends Service {
    Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String points = intent.getStringExtra("points");

        addDataToFirebase(points);

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate: service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onCreate: service destroy");
    }

    private void addDataToFirebase(final String points) {

        thread = new Thread() {
            @Override
            public void run() {
                Constant.setString(App.getContext(), Constant.LAST_TIME_ADD_TO_SERVER, "");
                String tag_json_obj = "json_login_req";
                Map<String, String> params = new HashMap<String, String>();
                params.put("update_point", "any");
                params.put("new_point", points);
                params.put("user_id", Constant.getString(App.getContext(), Constant.USER_ID));
                Log.e("TAG", "run: " + params);
                CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                        BaseUrl.UPDATE_POINTS, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                Constant.setString(App.getContext(), Constant.LAST_TIME_ADD_TO_SERVER, "add");
                            } else {
                                Constant.setString(App.getContext(), Constant.LAST_TIME_ADD_TO_SERVER, "");
                            }
                            stopSelf();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopSelf();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        stopSelf();
                    }
                });
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 30,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }
        };
        thread.start();
    }
}
