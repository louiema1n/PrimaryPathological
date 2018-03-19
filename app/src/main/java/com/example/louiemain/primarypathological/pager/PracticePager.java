package com.example.louiemain.primarypathological.pager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.example.louiemain.primarypathological.R;
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

    private DrawableVerticalButton btn_order;
    private DrawableVerticalButton btn_random;

    public PracticePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.layout_practice, null);
        // 初始化组件
        btn_order = (DrawableVerticalButton) view.findViewById(R.id.btn_order);
        btn_random = (DrawableVerticalButton) view.findViewById(R.id.btn_random);

        // 设置组件监听
        btn_order.setOnClickListener(new MyOnClickListener());
        btn_random.setOnClickListener(new MyOnClickListener());
        return view;
    }

    /**
     * @description 监听
     * @author louiemain
     * @date Created on 2018/3/19 20:54
     * @return
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_order:

                    break;
                case R.id.btn_random:
                    Intent intent = new Intent(context, RandomPracticeActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
    }
}
