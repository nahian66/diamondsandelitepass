package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.rowdystudio.fffdiamondsandelitespass.App;
import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;
import com.startapp.sdk.adsbase.StartAppAd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MainActivity activity;
    LinearLayout check_in_layout, scratch_layout, spin_layout, refer_layout, profile_layout, rating_layout, sound_layout;
    private TextView user_name_text_view, user_points_text_view;
    private ProgressDialog alertDialog;
    private boolean isShowing = true;

    public StartAppAd startAppAd;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        String typeOfAds = getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("startapp")) {
            startAppAd.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        String typeOfAds = getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("startapp")) {
            startAppAd.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));
        Constant.setString(activity, Constant.LAST_TIME_ADD_TO_SERVER, "");
        user_name_text_view = findViewById(R.id.user_name_text_view);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        check_in_layout = findViewById(R.id.daily_check_in_layout);
        scratch_layout = findViewById(R.id.scratch_card_layout);
        spin_layout = findViewById(R.id.spin_wheel_layout);
        refer_layout = findViewById(R.id.refer_and_earn_layout);
        profile_layout = findViewById(R.id.profile_layout);
        rating_layout = findViewById(R.id.rating_app_layout);
        sound_layout = findViewById(R.id.sound_layout);
        if (Constant.isNetworkAvailable(activity)) {
            alertDialog = new ProgressDialog(activity);
            alertDialog.setTitle(getResources().getString(R.string.loading));
            alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
            alertDialog.setMessage(getResources().getString(R.string.please_wait));
            alertDialog.setCancelable(false);
            if (getResources().getString(R.string.ad_network).equals("startapp")) {
                LoadInterstitial();
            } else {
                Constant.loadInterstitialAd();
            }
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
        onClick();
    }

    private void LoadInterstitial() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(App.getContext());
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }

    private void ShowInterstital() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd();
            isShowing = false;
        }
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
        checkIsBlocked();
        rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.ratingApp(activity);
            }
        });

        check_in_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Log.e("TAG", "onClick: Current Date" + currentDate);
                    String last_date = Constant.getString(activity, Constant.LAST_DATE);
                    Log.e("TAG", "onClick: last_date Date" + last_date);
                    if (last_date.equals("")) {
                        Constant.setString(activity, Constant.LAST_DATE, currentDate);
                        Constant.addPoints(activity, Integer.parseInt(getResources().getString(R.string.daily_check_in_points)),0);
                        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                        showDialogOfPoints(Integer.parseInt(getResources().getString(R.string.daily_check_in_points)));
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date pastDAte = sdf.parse(last_date);
                            Date currentDAte = sdf.parse(currentDate);
                            long diff = currentDAte.getTime() - pastDAte.getTime();
                            long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                            Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                            if (difference_In_Days > 0) {
                                Constant.setString(activity, Constant.LAST_DATE, currentDate);
                                Constant.addPoints(activity, Integer.parseInt(getResources().getString(R.string.daily_check_in_points)),0);
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                                showDialogOfPoints(Integer.parseInt(getResources().getString(R.string.daily_check_in_points)));
                            } else {
                                showDialogOfPoints(0);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

        scratch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, ScratchActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        spin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, SpinActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProfile();
            }
        });

        refer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, ReferAndEarn.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        sound_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = Constant.getString(activity, Constant.SOUND_ON_OR_OFF);
                if (tag.equals("off")) {
                    Constant.setString(activity, Constant.SOUND_ON_OR_OFF, "");
                    Constant.setString(activity, Constant.SOUND_VALUE, "");
                    sound_layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.sound_background));
                    App.startService();
                } else if (tag.equals("")) {
                    Constant.setString(activity, Constant.SOUND_ON_OR_OFF, "off");
                    Constant.setString(activity, Constant.SOUND_VALUE, "true");
                    sound_layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.of_sound_background));
                    App.stopService();
                }
            }
        });
    }

    private void gotoProfile() {
        if (Constant.getString(activity, Constant.IS_LOGIN).equals("true")) {
            Constant.GotoNextActivity(activity, ProfileActivity.class, "msg");
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            Constant.showToastMessage(activity, getResources().getString(R.string.login_first));
            Constant.GotoNextActivity(activity, LoginActivity.class, "msg");
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }
    }

    private void setUSerName() {
        user_name_text_view.setText(Constant.getString(activity, Constant.USER_NAME));
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
    }

    private void checkSoundValue() {
        String tag = Constant.getString(activity, Constant.SOUND_ON_OR_OFF);
        if (tag.equals("")) {
            sound_layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.sound_background));
        } else if (tag.equals("off")) {
            sound_layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.of_sound_background));
        }
    }

    private void checkIsBlocked() {
        if (Constant.getString(activity, Constant.USER_BLOCKED).equals("true")) {
            Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
            return;
        }
        checkUserInfo();
        checkSoundValue();
        setUSerName();
    }

    private void checkUserInfo() {
        String user_refer_code = Constant.getString(activity, Constant.USER_REFFER_CODE);
        String user_name = Constant.getString(activity, Constant.USER_NAME);
        String user_number = Constant.getString(activity, Constant.USER_NUMBER);
        if (user_refer_code.equals("") || user_name.equals("") || user_number.equals("")) {
            showUpdateProfileDialog();
        }
    }

    private void showUpdateProfileDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.profile);
        title_text.setText(getResources().getString(R.string.incomplite_profile));
        points_text.setVisibility(View.GONE);
        add_btn.setText(getResources().getString(R.string.update_profile));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoProfile();
                    }
                }, 1000);
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));
        checkIsBlocked();
        String typeOfAds = getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("startapp")) {
            if (isShowing) {
                ShowInterstital();
            }
        } else {
            if (isShowing) {
                Constant.showInterstitialAds();
                isShowing = false;
            }
        }
    }

    public void showDialogOfPoints(int points) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        if (points == Integer.parseInt(getResources().getString(R.string.daily_check_in_points))) {
            imageView.setImageResource(R.drawable.ic_trophy);
            title_text.setText(getResources().getString(R.string.you_won));
            points_text.setVisibility(View.VISIBLE);
            points_text.setText(String.valueOf(points));
            add_btn.setText(getResources().getString(R.string.add_to_wallet));
        } else {
            imageView.setImageResource(R.drawable.ic_trophy);
            title_text.setText(getResources().getString(R.string.today_checkin_over));
            points_text.setVisibility(View.GONE);
            add_btn.setText(getResources().getString(R.string.okk));
        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btn);
        AppCompatButton yes_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.ic_error);
        title_text.setText(getResources().getString(R.string.exit_text));
        points_text.setVisibility(View.GONE);
        cancel_btn.setVisibility(View.VISIBLE);
        cancel_btn.setText(getResources().getString(R.string.cancel));
        yes_btn.setText(getResources().getString(R.string.yes));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

}