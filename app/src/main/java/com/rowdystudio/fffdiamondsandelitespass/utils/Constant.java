package com.rowdystudio.fffdiamondsandelitespass.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.rowdystudio.fffdiamondsandelitespass.App;
import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.services.PointsService;
import com.rowdystudio.fffdiamondsandelitespass.sharedPref.PrefManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Locale;
import java.util.regex.Pattern;

public class Constant {
    public static final String IS_LOGIN = "IsLogin";
    public static final String DATABASE_NAME = "User";
    public static final String DATABASE_REDEEM = "Redeem";
    public static final String USER_BLOCKED = "user_blocked";
    public static final String LANGUAGE = "language";
    public static final String USER_NAME = "user_name";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_POINTS = "user_points";
    public static final String IS_UPDATE = "user_points";
    public static final String USER_REFFER_CODE = "user_reffer_code";
    public static final String LAST_DATE = "Last_Date";
    public static final String LAST_TIME_ADD_TO_SERVER = "last_time_added";
    public static final String REFER_CODE = "refer_code";
    public static final String SCRATCH_COUNT = "scratch_count";
    public static final String LAST_DATE_SCRATCH = "last_date_scratch";
    public static final String SPIN_COUNT = "spin_count";
    public static final String LAST_DATE_SPIN = "last_date_spin";
    public static final String SOUND_VALUE = "sound_value";
    public static final String SOUND_ON_OR_OFF = "On_Off";
    public static final String USER_ID = "user_id";
    public static final String USER_PASSWORD = "password";
    private static PrefManager prefManager;
    private static String ShareText = "Hey You Know About this App. I have earned 100 Rupees from this app, you also want to earn money so click on link below and download it from play store and use My Refer code to get instant bonus My Refer code is ";
    public static InterstitialAd interstitial_Ad;
    public static com.facebook.ads.InterstitialAd interstitialAd;
    public static RewardedAd rewarded_ad;
    public static boolean isShowInterstitial = true;
    public static boolean isShowRewarded = true;
    public static boolean isShowfacebookRewarded = true;
    public static boolean isAttachedInterstitial = true;
    public static boolean isAttachedfacebookInterstitial = true;
    public static boolean isAttachedRewarded = true;
    public static boolean isShowFacebookInterstitial = true;
    public static RewardedAdLoadCallback adLoadCallback;
    public static final String TAG = "Constant";
    public static InterstitialAdListener interstitialAdListener;
    public static RewardedVideoAd rewardedVideoAd;
    public static ProgressDialog alertDialog;


    public static void loadInterstitialAd() {
        String typeOfAds = App.getContext().getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("facebook")) {
            if (interstitialAd == null) {
                interstitialAd = new com.facebook.ads.InterstitialAd(App.getContext(), App.getContext().getResources().getString(R.string.facebook_interstitial_ad_id));
                AttachFacebookListner();
                interstitialAd.loadAd(
                        interstitialAd.buildLoadAdConfig()
                                .withAdListener(interstitialAdListener)
                                .build());
                isAttachedfacebookInterstitial = false;
            } else {
                interstitialAd.loadAd();
            }
        } else if (typeOfAds.equals("admob")) {
            if (interstitial_Ad == null) {
                interstitial_Ad = new InterstitialAd(App.getContext());
                interstitial_Ad.setAdUnitId(App.getContext().getResources().getString(R.string.admob_interstitial_ad_id));
                interstitial_Ad.loadAd(new AdRequest.Builder().build());
                AttachListner();
                isAttachedInterstitial = false;
            } else if (interstitial_Ad.isLoading()) {
                Log.e("TAG", "loadInterstitialAd: isLoading");
            } else {
                interstitial_Ad.loadAd(new AdRequest.Builder().build());
            }
        }
    }

    public static void AttachFacebookListner() {
        if (isAttachedInterstitial) {
            interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "Interstitial ad displayed.");
                    App.stopService();
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "Interstitial ad dismissed.");
                    App.startService();
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                }


                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                    // Show the ad
                    if (!isShowFacebookInterstitial) {
                        showInterstitialAds();
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                    Log.d(TAG, "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "Interstitial ad impression logged!");
                }
            };
        }
    }

    public static void AttachListner() {
        if (isAttachedInterstitial) {
            interstitial_Ad.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.e("adLoaded", "adLoaded: interstitial");
                    if (!isShowInterstitial) {
                        showInterstitialAds();
                    }
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    Log.e("onAdClosed", "onAdClosed: ");
                    App.startService();
                    loadInterstitialAd();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Log.e("adOpened", "adOpened: ");
                    App.stopService();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.e("adError", "adError: " + loadAdError.toString());
                }
            });
        }
    }

    public static void showProgressDialog(Context context) {
        if (alertDialog == null) {
            alertDialog = new ProgressDialog(context);
            alertDialog.setTitle("Loading");
            alertDialog.setMessage("Please Wait...");
            alertDialog.setCancelable(false);
        }

        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static void showInterstitialAds() {
        String typeOfAds = App.getContext().getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("admob")) {
            if (interstitial_Ad != null && interstitial_Ad.isLoaded()) {
                hideProgressDialog();
                interstitial_Ad.show();

                isShowInterstitial = true;
            } else {
                hideProgressDialog();
                isShowInterstitial = false;
            }
        } else if (typeOfAds.equals("facebook")) {
            if (interstitialAd != null && interstitialAd.isAdLoaded()) {
                hideProgressDialog();
                interstitialAd.show();
                isShowFacebookInterstitial = true;
            } else {
                hideProgressDialog();
                isShowFacebookInterstitial = false;
            }
        }
    }

    public static void showRewardedAds(final Activity context) {
        String typeOfAds = App.getContext().getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("admob")) {
            if (rewarded_ad != null && rewarded_ad.isLoaded()) {
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                        App.stopService();
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                        App.startService();
                        loadRewardedVideo(context);
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.
                    }

                    @Override
                    public void onRewardedAdFailedToShow(AdError adError) {
                        // Ad failed to display.
                    }
                };
                rewarded_ad.show(context, adCallback);
                isShowRewarded = true;
            } else {
                isShowRewarded = false;
                Constant.showToastMessage(context, "Video Ad not Ready");
            }
        } else if (typeOfAds.equals("facebook")) {
            if (rewardedVideoAd != null && rewardedVideoAd.isAdLoaded()) {
                rewardedVideoAd.show();
                isShowfacebookRewarded = true;
            } else {
                isShowfacebookRewarded = false;
                Constant.showToastMessage(context, "Video Ad not Ready");
            }
        }
    }

    public static void loadRewardedVideo(final Activity activity) {
        String typeOfAds = App.getContext().getResources().getString(R.string.ad_network);
        if (typeOfAds.equals("admob")) {
            if (rewarded_ad != null) {
                try {
                    rewarded_ad = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rewarded_ad = new RewardedAd(activity, activity.getResources().getString(R.string.admob_rewarded_video_id));
            AttachedRewaredCallBack(activity);
            rewarded_ad.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        } else if (typeOfAds.equals("facebook")) {
            if (rewardedVideoAd != null) {
                try {
                    rewardedVideoAd = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rewardedVideoAd = new RewardedVideoAd(App.getContext(), activity.getResources().getString(R.string.facebook_rewarded_video_id));
            RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    //Log.e(TAG, "Rewarded video ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Rewarded video ad is loaded and ready to be displayed
                    Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
                    if (!isShowfacebookRewarded) {
                        showRewardedAds(activity);
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Rewarded video ad clicked
                    Log.d(TAG, "Rewarded video ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Rewarded Video ad impression - the event will fire when the
                    // video starts playing
                    Log.d(TAG, "Rewarded video ad impression logged!");
                    App.stopService();
                }

                @Override
                public void onRewardedVideoCompleted() {
                    // Rewarded Video View Complete - the video has been played to the end.
                    // You can use this event to initialize your reward
                    Log.d(TAG, "Rewarded video completed!");
                    App.startService();
                    // Call method to give reward
                    // giveReward();
                }

                @Override
                public void onRewardedVideoClosed() {
                    // The Rewarded Video ad was closed - this can occur during the video
                    // by closing the app, or closing the end card.
                    Log.d(TAG, "Rewarded video ad closed!");
                    App.startService();
                }
            };
            rewardedVideoAd.loadAd(
                    rewardedVideoAd.buildLoadAdConfig()
                            .withAdListener(rewardedVideoAdListener)
                            .build());
        }
    }

    public static void AttachedRewaredCallBack(final Activity activity) {
        if (adLoadCallback != null) {
            adLoadCallback = null;
        }
        adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                if (!isShowRewarded) {
                    showRewardedAds(activity);
                }
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
    }

    public static void GotoNextActivity(Context context, Class nextActivity, String msg) {
        if (context != null && nextActivity != null) {
            if (msg == null) {
                msg = "";
            }
            Intent intent = new Intent(context, nextActivity);
            intent.putExtra("Intent", msg);
            context.startActivity(intent);
        }
    }

    public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean isMatches = pattern.matcher(email).matches();
        Log.e("Boolean Value", "" + isMatches);
        return isMatches;
    }

    public static void showToastMessage(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void setString(Context context, String preKey, String setString) {
        if (prefManager == null) {
            prefManager = new PrefManager(context);
        }
        prefManager.setString(preKey, setString);
    }

    public static String getString(Context context, String prefKey) {
        if (prefManager == null) {
            prefManager = new PrefManager(context);
        }
        return prefManager.getString(prefKey);
    }

    public static void setLanguage(Context context, String language) {
        if (language.equals("")) {
            language = "en";
        }
        Locale locale = new Locale(language);
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        configuration.locale = locale;
        res.updateConfiguration(configuration, displayMetrics);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void ratingApp(Context context) {
        if (context != null) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                context.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        }
    }

    public static void addPoints(Context context, int points, int type) {
        if (context != null) {
            String po = Constant.getString(context, Constant.USER_POINTS);
            if (po.equals("")) {
                po = "0";
            }
            if (type == 1) {
                Constant.setString(context, Constant.USER_POINTS, String.valueOf(po));
                Intent serviceIntent = new Intent(context, PointsService.class);
                serviceIntent.putExtra("points", Constant.getString(context, Constant.USER_POINTS));
                context.startService(serviceIntent);
            } else {
                int current_Points = Integer.parseInt(po);
                String total_points = String.valueOf(current_Points + points);
                Constant.setString(context, Constant.USER_POINTS, total_points);
                Intent serviceIntent = new Intent(context, PointsService.class);
                serviceIntent.putExtra("points", Constant.getString(context, Constant.USER_POINTS));
                context.startService(serviceIntent);
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showInternetErrorDialog(Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.ic_internet);
        title_text.setText(msg);
        points_text.setVisibility(View.GONE);
        add_btn.setText(context.getResources().getString(R.string.okk));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showBlockedDialog(final Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.ic_close);
        title_text.setText(msg);
        points_text.setVisibility(View.GONE);
        add_btn.setText(context.getResources().getString(R.string.okk));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void referApp(Context context, String refer_code) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, Constant.ShareText + "' " + refer_code + " '" + " Download Link = " + " https://play.google.com/store/apps/details?id=" + context.getPackageName());
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            Log.e("TAG", "referApp: " + e.getMessage().toString());
        }
    }
}
