package com.example.louiemain.primarypathological.pager;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.activity.OrderPracticeActivity;
import com.example.louiemain.primarypathological.activity.RandomPracticeActivity;
import com.example.louiemain.primarypathological.base.BasePager;
import com.example.louiemain.primarypathological.view.DrawableVerticalButton;

/**
 * @author & date Created by ${user} on ${date} ${time}
 * /**
 * @description
 * @Program: PrimaryPathological
 * @Type: Class
 * @Description: 练习页面
 * @Author: louiemain
 * @Created: 2018-03-19 11:15
 **/
public class PracticePager extends BasePager {

    public PracticePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.layout_practice, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }

}
