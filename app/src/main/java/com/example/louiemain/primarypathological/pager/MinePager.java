package com.example.louiemain.primarypathological.pager;

import android.content.Context;
import android.view.View;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.base.BasePager;

/**
 * @author & date Created by ${user} on ${date} ${time}
 * /**
 * @description
 * @Program: PrimaryPathological
 * @Type: Class
 * @Description: 我的页面
 * @Author: louiemain
 * @Created: 2018-03-19 11:15
 **/
public class MinePager extends BasePager {


    public MinePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.activity_settings, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
