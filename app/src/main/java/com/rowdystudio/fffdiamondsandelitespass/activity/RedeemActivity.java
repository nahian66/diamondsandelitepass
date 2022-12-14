package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.rowdystudio.fffdiamondsandelitespass.utils.BaseUrl;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;
import com.rowdystudio.fffdiamondsandelitespass.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends AppCompatActivity {

    private RedeemActivity activity;
    private TextView user_points_textView;
    private EditText redeem_editText, points_editText;
    private AppCompatButton redeemBtn;
    private ProgressDialog progressDialog;
    private RadioGroup payment_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        activity = this;
        Constant.setLanguage(activity, Constant.getString(this, Constant.LANGUAGE));
        onClick();
    }

    private void onClick() {
        payment_group = findViewById(R.id.payment_group);
        user_points_textView = findViewById(R.id.user_points_text_view_redeem);
        redeem_editText = findViewById(R.id.redeem_edit_text);
        points_editText = findViewById(R.id.points_edit_text);
        redeemBtn = findViewById(R.id.redeem_btn);
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_textView.setText("0");
        } else {
            user_points_textView.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                    String numberOrUpiId = redeem_editText.getText().toString();
                    String points = points_editText.getText().toString();
                    if (numberOrUpiId.length() == 0) {
                        redeem_editText.setError(getResources().getString(R.string.enterNumberOrUpi));
                        redeem_editText.requestFocus();
                    } else if (points.length() == 0) {
                        points_editText.setError(getResources().getString(R.string.enter_points));
                        points_editText.requestFocus();
                    } else if (points.equals("0")) {
                        points_editText.setError(getResources().getString(R.string.enter_correct_points));
                        points_editText.requestFocus();
                    } else {
                        if (Integer.parseInt(points) < Integer.parseInt(getResources().getString(R.string.minimum_redeem_amount))) {
                            Constant.showToastMessage(activity, "Minimum Redeem Coins is " + getResources().getString(R.string.minimum_redeem_amount));
                            return;
                        }
                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(points)) {
                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                            return;
                        }
                        int selectId = payment_group.getCheckedRadioButtonId();
                        if (selectId == -1) {
                            Constant.showToastMessage(activity, "Select Payment Method");
                            return;
                        }
                        String type = "";
                        if (selectId == R.id.paytm_btn) {
                            type = "paytm";
                        }
                        if (selectId == R.id.phone_pe_btn) {
                            type = "paypal";
                        }
                        if (selectId == R.id.upi_btn) {
                            type = "upi";
                        }
                        Constant.hideKeyboard(activity);
                        RedeemPointsDialog(numberOrUpiId, points, type);
                    }
                } else {
                    Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                }
            }
        });
    }

    private void RedeemPointsDialog(final String numberOrUpiId, final String points, final String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton confirm_btn = dialog.findViewById(R.id.add_btn);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btn);

        imageView.setImageResource(R.drawable.ic_trophy);
        title_text.setVisibility(View.VISIBLE);
        points_text.setVisibility(View.VISIBLE);
        confirm_btn.setVisibility(View.VISIBLE);
        cancel_btn.setVisibility(View.VISIBLE);

        title_text.setText(getResources().getString(R.string.redeem_tag_line_1));
        String points_text_string = getResources().getString(R.string.redeem_tag_line_2) + " " + numberOrUpiId + " " + getResources().getString(R.string.redeem_tag_line_3) + " " + points + " " + getResources().getString(R.string.redeem_tag_line_4) + " " + type;
        points_text.setText(points_text_string);
        confirm_btn.setText(getResources().getString(R.string.yes));
        cancel_btn.setText(getResources().getString(R.string.cancel));

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle(getResources().getString(R.string.in_progress));
                progressDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_trophy, null));
                progressDialog.setMessage(getResources().getString(R.string.please_wait));
                progressDialog.setCancelable(false);
                redeemBtn.setEnabled(false);
                if (Constant.getString(activity, Constant.IS_LOGIN).equalsIgnoreCase("true")) {
                    showProgressDialog();
                    makeRedeemRequest(numberOrUpiId, points, type, Constant.getString(activity, Constant.REFER_CODE));
                } else {
                    Constant.showToastMessage(activity, "Login First");
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemBtn.setEnabled(true);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void makeRedeemRequest(String numberOrUpiId, final String points, String type, String refer_by) {
        redeemBtn.setEnabled(true);
        final String user_previous_points = Constant.getString(activity, Constant.USER_POINTS);
        final int current_points = Integer.parseInt(user_previous_points) - Integer.parseInt(points);
        Constant.setString(activity, Constant.USER_POINTS, String.valueOf(current_points));
        user_points_textView.setText(String.valueOf(current_points));
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("redeem_point", "redeem");
        if (!refer_by.equalsIgnoreCase("")) {
            params.put("referraled_with", refer_by);
        }
        params.put("user_id", Constant.getString(activity, Constant.USER_ID));
        params.put("new_point", String.valueOf(current_points));
        params.put("redeemed_point", points);
        params.put("payment_mode", type);
        params.put("payment_info", numberOrUpiId);
        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.UPDATE_POINTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        Constant.showToastMessage(activity, getResources().getString(R.string.redeem_successfully));
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)), 1);
                    } else {
                        Constant.showToastMessage(activity, response.getString("message"));
                        user_points_textView.setText(String.valueOf(user_previous_points));
                        Constant.setString(activity, Constant.USER_POINTS, String.valueOf(user_previous_points));
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)), 1);
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
                user_points_textView.setText(String.valueOf(user_previous_points));
                Constant.setString(activity, Constant.USER_POINTS, String.valueOf(user_previous_points));
                Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)), 1);
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}