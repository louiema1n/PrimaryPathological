package com.example.louiemain.primarypathological.view;/**
 * @description
 * @author&date Created by louiemain on 2018/3/24 15:45
 */

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.example.louiemain.primarypathological.activity.OrderPracticeActivity;
import com.example.louiemain.primarypathological.activity.RandomPracticeActivity;

/**
 * @Pragram: PrimaryPathological
 * @Type: Class
 * @Description: 测试代码实例化View
 * @Author: louiemain
 * @Created: 2018/3/24 15:45
 **/
public class OrderAndRandomView extends LinearLayout {

    private Context context;
    private View test_order;
    private View test_random;

    private boolean isSlide = false;

    // 试图识别器
    private GestureDetector gestureDetector;

    public OrderAndRandomView(Context context) {
        this(context, null);
    }

    public OrderAndRandomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderAndRandomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        gestureDetector = new GestureDetector(context, new simpleGestureListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        test_order = getChildAt(0);
        test_random = getChildAt(1);

    }

    class simpleGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            isSlide = true;
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            test_order.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderPracticeActivity.class);
                    context.startActivity(intent);
                }
            });
            test_random.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RandomPracticeActivity.class);
                    context.startActivity(intent);
                }
            });

            isSlide = false;
            return super.onSingleTapUp(e);
        }

        /**
         * 按下时设置为false-重要
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e) {
            isSlide = false;
            return super.onDown(e);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
