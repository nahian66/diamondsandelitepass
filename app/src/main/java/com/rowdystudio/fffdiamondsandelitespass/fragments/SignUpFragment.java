package com.rowdystudio.fffdiamondsandelitespass.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.rowdystudio.fffdiamondsandelitespass.App;
import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.activity.MainActivity;
import com.rowdystudio.fffdiamondsandelitespass.models.User;
import com.rowdystudio.fffdiamondsandelitespass.utils.BaseUrl;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;
import com.rowdystudio.fffdiamondsandelitespass.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.rowdystudio.fffdiamondsandelitespass.utils.Constant.hideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText name_edit_text, number_edit_text, email_edit_text, password_edit_text, reffreal_code_edit_text;
    private TextView login_text_view;
    private AppCompatButton sign_up_button;
    private Context mContext;
    private ProgressDialog alertDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Constant.setLanguage(mContext, Constant.getString(mContext, Constant.LANGUAGE));

        name_edit_text = view.findViewById(R.id.sign_up_name_edit_text);
        number_edit_text = view.findViewById(R.id.sign_up_number_edit_text);
        password_edit_text = view.findViewById(R.id.sign_up_password_edit_text);
        email_edit_text = view.findViewById(R.id.sign_up_email_edit_text);
        sign_up_button = view.findViewById(R.id.sign_up_btn);
        login_text_view = view.findViewById(R.id.login_text);
        reffreal_code_edit_text = view.findViewById(R.id.sign_up_refer_code_edit_text);
        alertDialog = new ProgressDialog(mContext);
        alertDialog.setTitle(getResources().getString(R.string.signup_in_progress));
        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
        alertDialog.setMessage(getResources().getString(R.string.please_wait));
        alertDialog.setCancelable(false);
        onInitView();

        return view;
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

    private void onInitView() {
        login_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().onBackPressed();
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(mContext)) {
                    String Name = name_edit_text.getText().toString();
                    String Number = number_edit_text.getText().toString();
                    String Email = email_edit_text.getText().toString();
                    String Password = password_edit_text.getText().toString();
                    String refercode = reffreal_code_edit_text.getText().toString();

                    if (Name.length() == 0) {
                        name_edit_text.setError(getResources().getString(R.string.enter_name));
                        name_edit_text.requestFocus();
                    } else if (Number.length() == 0) {
                        number_edit_text.setError(getResources().getString(R.string.enter_number));
                        number_edit_text.requestFocus();
                    } else if (Number.length() != 10) {
                        number_edit_text.setError(getResources().getString(R.string.enter_valid_number));
                        number_edit_text.requestFocus();
                    } else if (Email.length() == 0) {
                        email_edit_text.setError(getResources().getString(R.string.enter_email));
                        email_edit_text.requestFocus();
                    } else if (!Constant.isValidEmailAddress(Email)) {
                        email_edit_text.setError(getResources().getString(R.string.enter_valid_email));
                        email_edit_text.requestFocus();
                    } else if (Password.length() == 0) {
                        password_edit_text.setError(getResources().getString(R.string.enter_password));
                        password_edit_text.requestFocus();
                    } else if (Password.length() < 6) {
                        password_edit_text.setError(getResources().getString(R.string.enter_6_digit_password));
                        password_edit_text.requestFocus();
                    } else {
                        hideKeyboard(getActivity());
                        showProgressDialog();
                        String singup_bonus = getActivity().getResources().getString(R.string.singup_bonus_points);
                        signupNewUser(Email, Password, Name, Number, refercode, singup_bonus);
                    }
                } else {
                    Constant.showInternetErrorDialog(mContext, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

    }

    private void signupNewUser(final String email, final String password, final String name, final String number, final String refercode, final String sign_up_bonus) {
        if (getActivity() == null) {
            hideProgressDialog();
            return;
        }

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("register", "anythimg");
        params.put("name", name);
        params.put("email", email);
        params.put("number", number);
        params.put("password", password);
        params.put("referral_code", name.replaceAll("\\s+", "") + number.substring(0, 4));
        if (!refercode.isEmpty()) {
            params.put("referral_with", refercode);
        }
        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.REGISTER_API, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        Constant.setString(mContext, Constant.USER_PASSWORD, password);
                        final User user = new User(name.trim(), number, email, sign_up_bonus, refercode, "false", name.replaceAll("\\s+", "") + number.substring(0, 4));
                        if (getActivity() == null) {
                            hideProgressDialog();
                            return;
                        }
                        JSONObject object = response.getJSONObject("0");

                        if (object.has("id")) {
                            Constant.setString(mContext, Constant.USER_ID, object.getString("id"));
                            Log.e("TAG", "onResponse: " + object.getString("id"));
                        }
                        if (user.getName() != null) {
                            Constant.setString(mContext, Constant.USER_NAME, user.getName());
                            Log.e("TAG", "onDataChange: " + user.getName());
                        }
                        if (user.getNumber() != null) {
                            Constant.setString(mContext, Constant.USER_NUMBER, user.getNumber());
                            Log.e("TAG", "onDataChange: " + user.getNumber());
                        }
                        if (user.getEmail() != null) {
                            Constant.setString(mContext, Constant.USER_EMAIL, user.getEmail());
                            Log.e("TAG", "onDataChange: " + user.getEmail());
                        }
                        if (user.getPoints() != null) {
                            Constant.setString(mContext, Constant.USER_POINTS, user.getPoints());
                            Log.e("TAG", "onDataChange: " + user.getPoints());
                        }
                        if (user.getReferCode() != null) {
                            Constant.setString(mContext, Constant.REFER_CODE, user.getReferCode());
                            Log.e("TAG", "onDataChange: " + user.getReferCode());
                        }
                        if (user.getIsBLocked() != null) {
                            Constant.setString(mContext, Constant.USER_BLOCKED, user.getIsBLocked());
                            Log.e("TAG", "onDataChange: " + user.getIsBLocked());
                        }
                        if (user.getUserReferCode() != null) {
                            Constant.setString(mContext, Constant.USER_REFFER_CODE, user.getUserReferCode());
                            Log.e("TAG", "onDataChange: " + user.getUserReferCode());
                        }
                        hideProgressDialog();
                        Constant.setString(mContext, Constant.IS_LOGIN, "true");
                        Constant.showToastMessage(mContext, getResources().getString(R.string.registration_success));
                        Constant.GotoNextActivity(mContext, MainActivity.class, "");
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        getActivity().finish();
                    } else {
                        Constant.showToastMessage(mContext, response.getString("message"));
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
                    Constant.showToastMessage(mContext, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}