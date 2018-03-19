package com.example.louiemain.primarypathological.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.example.louiemain.primarypathological.base.BasePager;

/**
 * @author & date Created by ${user} on ${date} ${time}
 * /**
 * @description
 * @Program: PrimaryPathological
 * @Type: Class
 * @Description: 其他页面
 * @Author: louiemain
 * @Created: 2018-03-19 11:15
 **/
public class OthersPager extends BasePager {

    private TextView textView;

    public OthersPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("其他");
    }
}
