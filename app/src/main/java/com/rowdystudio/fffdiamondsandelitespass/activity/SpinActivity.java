package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdSize;
import com.rowdystudio.fffdiamondsandelitespass.App;
import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.services.PointsService;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;


import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.StartAppAd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {
    List<WheelItem> wheelItems = new ArrayList<>();
    LuckyWheel luckyWheel;
    SpinActivity activity;
    private AppCompatButton play_btn;
    private String points;
    TextView user_points_text_view, spin_count_text_view;
    private int spin_count = 1;
    private String TAG = "SpinActivity";
    public StartAppAd startAppAd;
    public int poiints = 0;
    public boolean rewardShow = true, interstitialShow = true;

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
        setContentView(R.layout.activity_spin);
        activity = this;
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));

        luckyWheel = findViewById(R.id.lwv);
        play_btn = findViewById(R.id.play);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        spin_count_text_view = findViewById(R.id.spin_count_textView);

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "3"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "6"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "9"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "12"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "15"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "18"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "21"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "24"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "27"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "30"));
        luckyWheel.addWheelItems(wheelItems);
        if (Constant.isNetworkAvailable(activity)) {
            loadBanner();
            if (getResources().getString(R.string.ad_network).equals("startapp")) {
                LoadInterstitial();
            } else {
                Constant.loadInterstitialAd();
                Constant.loadRewardedVideo(activity);
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
        }
    }

    private void onClick() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }

        String spinCount = Constant.getString(activity, Constant.SPIN_COUNT);
        if (spinCount.equals("0")) {
            spinCount = "";
            Log.e("TAG", "onInit: spin card 0");
        }
        if (spinCount.equals("")) {
            Log.e("TAG", "onInit: spin card empty vala part");
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_SPIN);
            Log.e("TAG", "Lat date" + last_date);
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                spin_count_text_view.setText(getResources().getString(R.string.spin_and_scratch_count));
                Constant.setString(activity, Constant.SPIN_COUNT, getResources().getString(R.string.spin_and_scratch_count));
                Constant.setString(activity, Constant.LAST_DATE_SPIN, currentDate);
            } else {
                Log.e("TAG", "onInit: last date not empty part");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date current_date = sdf.parse(currentDate);
                    Date lastDate = sdf.parse(last_date);
                    long diff = current_date.getTime() - lastDate.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Difference" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_SPIN, currentDate);
                        Constant.setString(activity, Constant.SPIN_COUNT, getResources().getString(R.string.spin_and_scratch_count));
                        spin_count_text_view.setText(Constant.getString(activity, Constant.SPIN_COUNT));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                    } else {
                        spin_count_text_view.setText("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: spin in preference part");
            spin_count_text_view.setText(spinCount);
        }

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                    play_btn.setEnabled(false);
                    Random random = new Random();
                    points = String.valueOf(random.nextInt(10));
                    if (points.equals("0")) {
                        points = String.valueOf(1);
                    }
                    Log.e(TAG, "onClick: " + points);
                    luckyWheel.rotateWheelTo(Integer.parseInt(points));
                } else {
                    Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

        luckyWheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                try {
                    WheelItem item = wheelItems.get(Integer.parseInt(points) - 1);
                    String points_amount = item.text;
                    Log.e("TAG", "onReachTarget: " + points_amount);

                    int counter = Integer.parseInt(spin_count_text_view.getText().toString());
                    if (counter == 0) {
                        showDialogPoints(0, "0", counter);
                    } else {
                        showDialogPoints(1, points_amount, counter);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onReachTarget: " + e.getMessage().toString());
                }

            }
        });
    }

    private void showDialogPoints(final int addValueOrNoAdd, final String points, final int counter) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        AppCompatButton can_btn = dialog.findViewById(R.id.cancel_btn);
        can_btn.setVisibility(View.GONE);
        if (Constant.isNetworkAvailable(activity)) {
            if (addValueOrNoAdd == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "spinDialog: 0 points");
                    imageView.setImageResource(R.drawable.ic_trophy);
                    title_text.setText(getResources().getString(R.string.better_luck));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else {
                    Log.e("TAG", "spinDialog: points");
                    imageView.setImageResource(R.drawable.ic_trophy);
                    title_text.setText(getResources().getString(R.string.you_won));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.add_to_wallet));
                }
            } else {
                Log.e("TAG", "spinDialog: chance over");
                imageView.setImageResource(R.drawable.ic_trophy);
                title_text.setText(getResources().getString(R.string.today_chance_over));
                points_text.setVisibility(View.GONE);
                add_btn.setText(getResources().getString(R.string.okk));
            }

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    play_btn.setEnabled(true);
                    poiints = 0;
                    if (addValueOrNoAdd == 1) {
                        if (points.equals("0")) {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SPIN_COUNT, current_counter);
                            spin_count_text_view.setText(Constant.getString(activity, Constant.SPIN_COUNT));
                            dialog.dismiss();
                        } else {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SPIN_COUNT, current_counter);
                            spin_count_text_view.setText(Constant.getString(activity, Constant.SPIN_COUNT));
                            try {
                                int finalPoint;
                                if (points.equals("")) {
                                    finalPoint = 0;
                                } else {
                                    finalPoint = Integer.parseInt(points);
                                }
                                poiints = finalPoint;
                                Log.e(TAG, "onClick: " + poiints);
                                Constant.addPoints(activity, finalPoint,0);
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                            } catch (NumberFormatException ex) {
                                Log.e("TAG", "onScratchComplete: " + ex.getMessage().toString());
                            }
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                    }
                    if (spin_count == Integer.parseInt(getResources().getString(R.string.rewarded_and_interstitial_ads_between_count))) {
                        if (rewardShow) {
                            Log.e(TAG, "onReachTarget: rewaded ads showing method");
                            if (getResources().getString(R.string.ad_network).equals("startapp")) {
                                ShowInterstital();
                            } else {
                                showDailog();
                            }
                            rewardShow = false;
                            interstitialShow = true;
                            spin_count = 1;
                        } else if (interstitialShow) {
                            Log.e(TAG, "onReachTarget: interstital ads showing method");
                            if (getResources().getString(R.string.ad_network).equals("startapp")) {
                                ShowInterstital();
                            } else {
                                Constant.showInterstitialAds();
                            }
                            rewardShow = true;
                            interstitialShow = false;
                            spin_count = 1;
                        }
                    } else {
                        spin_count += 1;
                    }
                }
            });
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
        dialog.show();

    }

    public void showDailog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btn);
        // ads cannot cancel
        cancel_btn.setVisibility(View.GONE);
        add_btn.setText("Yes");
        imageView.setImageResource(R.drawable.ic_trophy);
        title_text.setText("Watch Full Video");
        points_text.setText("To Unlock this Reward Points");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Constant.showRewardedAds(activity);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (poiints != 0) {
                    String po = Constant.getString(activity, Constant.USER_POINTS);
                    if (po.equals("")) {
                        po = "0";
                    }
                    int current_Points = Integer.parseInt(po);
                    int finalPoints = current_Points - poiints;
                    Constant.setString(activity, Constant.USER_POINTS, String.valueOf(finalPoints));
                    Intent serviceIntent = new Intent(activity, PointsService.class);
                    serviceIntent.putExtra("points", Constant.getString(activity, Constant.USER_POINTS));
                    startService(serviceIntent);
                    user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    private void loadBanner() {
        final LinearLayout layout = findViewById(R.id.banner_container);
        String typeOfAds = getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("admob")) {
            final AdView adView = new AdView(activity);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.admob_banner_ad_id));
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    layout.addView(adView);
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.v("appnext", "client connection error");
                }
            });
            adView.loadAd(new AdRequest.Builder().build());
        } else if (typeOfAds.equals("facebook")) {
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, getResources().getString(R.string.facebook_banner_ads), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            adView.loadAd();
            layout.addView(adView);
        } else if (typeOfAds.equals("startapp")) {
            Banner startAppBanner = new Banner(activity, new BannerListener() {
                @Override
                public void onReceiveAd(View view) {

                }

                @Override
                public void onFailedToReceiveAd(View view) {

                }

                @Override
                public void onImpression(View view) {

                }

                @Override
                public void onClick(View view) {

                }
            });
            startAppBanner.loadAd();
            layout.addView(startAppBanner);
        }
    }

}