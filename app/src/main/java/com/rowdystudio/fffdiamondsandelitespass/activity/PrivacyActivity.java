package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;

public class PrivacyActivity extends AppCompatActivity {
    private WebView webView;
    private String url = "";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        Constant.setLanguage(this, Constant.getString(this, Constant.LANGUAGE));

        if (Constant.isNetworkAvailable(this)) {
            swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
            webView = findViewById(R.id.webView);
            String msg = getIntent().getStringExtra("Intent");
            if (msg.equals("privacy")) {
                url = getResources().getString(R.string.privacy_policy_link);
            } else if (msg.equals("terms")) {
                url = getResources().getString(R.string.terms_and_condition_link);
            }

            onClick();
        } else {
            Constant.showInternetErrorDialog(this, getResources().getString(R.string.no_internet_connection));
        }
    }

    private void onClick() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                LoadPage(url);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(url);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage(String Url) {
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(Url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String myUrl) {
            view.loadUrl(myUrl);
            return false;
        }
    }
}