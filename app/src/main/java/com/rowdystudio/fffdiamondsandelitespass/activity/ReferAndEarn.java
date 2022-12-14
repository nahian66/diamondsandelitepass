package com.rowdystudio.fffdiamondsandelitespass.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;

public class ReferAndEarn extends AppCompatActivity {
    private ReferAndEarn activity;
    private TextView refer_code_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);
        activity = this;
        Constant.setLanguage(activity, Constant.getString(activity, Constant.LANGUAGE));

        refer_code_textView = findViewById(R.id.refer_code);

        refer_code_textView.setText(Constant.getString(activity, Constant.USER_REFFER_CODE));
        findViewById(R.id.refer_and_win_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.referApp(activity, refer_code_textView.getText().toString());
            }
        });
    }
}