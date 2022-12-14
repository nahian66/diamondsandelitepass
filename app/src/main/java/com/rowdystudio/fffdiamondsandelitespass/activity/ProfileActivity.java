package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

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
import com.rowdystudio.fffdiamondsandelitespass.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.rowdystudio.fffdiamondsandelitespass.utils.Constant.hideKeyboard;

public class ProfileActivity extends AppCompatActivity {

    ProfileActivity activity;
    private EditText name_editText, email_editText, number_editText;
    private AppCompatButton update_profile_btn, redeem_btn, change_language, privacy_policy_btn, terms_btn;
    private ProgressDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activity = this;
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));

        name_editText = findViewById(R.id.profile_name_edit_text);
        email_editText = findViewById(R.id.profile_email_edit_text);
        number_editText = findViewById(R.id.profile_number_edit_text);
        update_profile_btn = findViewById(R.id.update_profile_btn);
        redeem_btn = findViewById(R.id.withdraw_btn);
        change_language = findViewById(R.id.change_language_btn);
        privacy_policy_btn = findViewById(R.id.prvicy_policy_btn);
        terms_btn = findViewById(R.id.term_and_condition_btn);
        onClick();
    }

    public void showProgressDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void onClick() {
        name_editText.setText(Constant.getString(activity, Constant.USER_NAME));
        email_editText.setText(Constant.getString(activity, Constant.USER_EMAIL));
        number_editText.setText(Constant.getString(activity, Constant.USER_NUMBER));

        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                    if (name_editText.getText().toString().length() == 0) {
                        name_editText.setError(getResources().getString(R.string.enter_name));
                        name_editText.requestFocus();
                    } else if (number_editText.getText().toString().length() == 0) {
                        number_editText.setError(getResources().getString(R.string.enter_number));
                        number_editText.requestFocus();
                    } else if (number_editText.getText().toString().length() != 10) {
                        number_editText.setError(getResources().getString(R.string.enter_valid_number));
                        number_editText.requestFocus();
                    } else {
                        alertDialog = new ProgressDialog(activity);
                        alertDialog.setTitle(getResources().getString(R.string.updateing_profile));
                        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
                        alertDialog.setMessage(getResources().getString(R.string.please_wait));
                        alertDialog.setCancelable(false);
                        hideKeyboard(activity);
                        showProgressDialog();
                        updateProfile(name_editText.getText().toString(), email_editText.getText().toString(), number_editText.getText().toString());
                    }
                } else {
                    Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanDialog();
            }
        });
        privacy_policy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, PrivacyActivity.class, "privacy");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, PrivacyActivity.class, "terms");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        redeem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, RedeemActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    private void updateProfile(String name, String email, String number) {
        String points = Constant.getString(activity, Constant.USER_POINTS);
        String user_email = Constant.getString(activity, Constant.USER_EMAIL);
        String userId = Constant.getString(activity, Constant.USER_ID);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("update_profile", "any");
        params.put("email", email);
        params.put("name", name);
        params.put("password", "");
        params.put("img", "");
        params.put("user_id", userId);
        params.put("number", number);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.UPDATE_PROFILE, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        JSONObject jsonObject = response.getJSONObject("0");
                        Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                        final User user = new User(jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("email"), jsonObject.getString("points"), jsonObject.getString("referraled_with"), jsonObject.getString("status"), jsonObject.getString("referral_code"));
                        hideProgressDialog();

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
                            Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                            Log.e("TAG", "onDataChange: " + user.getPoints());
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

                        hideProgressDialog();
                        Constant.setString(activity, Constant.IS_LOGIN, "true");
                        Constant.showToastMessage(activity, getResources().getString(R.string.update_successfully));
                    } else {
                        Constant.showToastMessage(activity, "Not Updated Try Again...");
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));
    }

    public void showLanDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_lan_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        final RadioGroup language_group = dialog.findViewById(R.id.radio_group_language_profile);
        AppCompatButton language_btn = dialog.findViewById(R.id.select_lan_profile_btn);
        ImageView close_btn = dialog.findViewById(R.id.close_btn);
        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                int selectId = language_group.getCheckedRadioButtonId();

                if (selectId == R.id.english_button_profile) {
                    Constant.setString(activity, Constant.LANGUAGE, "en");
                    Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));
                    if (activity == null) {
                        return;
                    }
                    dialog.dismiss();
                    startActivity(intent);
                } else if (selectId == R.id.hindi_button_profile) {
                    Constant.setString(activity, Constant.LANGUAGE, "hi");
                    Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));
                    if (activity == null) {
                        return;
                    }
                    dialog.dismiss();
                    startActivity(intent);
                }
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}