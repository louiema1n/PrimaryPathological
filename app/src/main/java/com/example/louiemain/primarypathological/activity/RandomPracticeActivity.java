package com.example.louiemain.primarypathological.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.example.louiemain.primarypathological.R;

public class RandomPracticeActivity extends Activity {

    private Toolbar toolbar_simple;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_practice);
        initView();

        // 设置menu
        toolbar_simple.inflateMenu(R.menu.menu);
        tv_title.setText(this.getString(R.string.random) + this.getString(R.string.actionbar_Practice_text));
    }

    private void initView() {
        toolbar_simple = (Toolbar) findViewById(R.id.toolbar_simple);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }
}
