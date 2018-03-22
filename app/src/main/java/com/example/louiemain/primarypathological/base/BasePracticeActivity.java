package com.example.louiemain.primarypathological.base;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.utils.DatabaseHelper;

/**
 * @Program: PrimaryPathological
 * @Type: Class
 * @Description: 共用的题目处理类
 * @Author: louiemain
 * @Created: 2018-03-22 10:08
 **/
public abstract class BasePracticeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TABLE_EXAM = "blcjexam";
    private static final String TABLE_RADIO = "radio";
    private Toolbar toolbar_practice;
    private TextView tv_title;
    // 获取数据库操作对象
    private DatabaseHelper helper = new DatabaseHelper(this, "topic", null, 13);
    private SQLiteDatabase database;
    private TextView tv_name;
    private TextView tv_anser;
    private TextView tv_analysis;
    private RadioButton rb_a;
    private RadioButton rb_b;
    private RadioButton rb_c;
    private RadioButton rb_d;
    private RadioButton rb_e;
    private TextView tv_commons;
    private RadioGroup rg_option;

    // 正确答案
    private String anser;
    // 位置
    private int position;
    private LinearLayout ly_result_analysis;
    private TextView tv_number;

    // 定义gestureDetector手势识别器
    private GestureDetector gestureDetector;

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_practice);
        initView();

        // 隐藏答案及解析
        ly_result_analysis.setVisibility(View.GONE);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        toolbar_practice = (Toolbar) findViewById(R.id.toolbar_practice);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_anser = (TextView) findViewById(R.id.tv_anser);
        tv_analysis = (TextView) findViewById(R.id.tv_analysis);
        rb_a = (RadioButton) findViewById(R.id.rb_a);
        rb_a.setOnClickListener(this);
        rb_b = (RadioButton) findViewById(R.id.rb_b);
        rb_b.setOnClickListener(this);
        rb_c = (RadioButton) findViewById(R.id.rb_c);
        rb_c.setOnClickListener(this);
        rb_d = (RadioButton) findViewById(R.id.rb_d);
        rb_d.setOnClickListener(this);
        rb_e = (RadioButton) findViewById(R.id.rb_e);
        rb_e.setOnClickListener(this);
        tv_commons = (TextView) findViewById(R.id.tv_commons);
        rg_option = (RadioGroup) findViewById(R.id.rg_option);
        ly_result_analysis = (LinearLayout) findViewById(R.id.ly_result_analysis);
        tv_number = (TextView) findViewById(R.id.tv_number);

        gestureDetector = new GestureDetector(this, MyGestureDetector);

        // 设置toolbar
        setSupportActionBar(toolbar_practice);
        // 设置title
        tv_title.setText(getToolbarTitle());
        // 设置当前题目数
        tv_number.setText(getNumber());
    }

    /**
     * 监听view的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_a:
                initRightResult();
                break;
            case R.id.rb_b:
                initRightResult();
                break;
            case R.id.rb_c:
                initRightResult();
                break;
            case R.id.rb_d:
                initRightResult();
                break;
            case R.id.rb_e:
                initRightResult();
                break;
        }
    }

    /**
     * 根据id生成题目
     *
     * @param id
     */
    public void generatePractice(String id) {
        // 得到数据库操作对象-读取模式
        database = helper.getReadableDatabase();
        try {
            Cursor cursorExam = database.query(TABLE_EXAM,
                    new String[]{"id", "name", "catalog", "type", "eid", "commons", "anser", "analysis", "rid"},
                    "id = ?",
                    new String[]{id},
                    null,
                    null,
                    null);

            // cursor置顶
            cursorExam.moveToFirst();
            if (cursorExam != null) {
                // 设置exam
                String name = handleName(cursorExam);

                // 去掉前面的数字
                String commons = cursorExam.getString(cursorExam.getColumnIndex("commons"));    // commons
                commons = commons.replaceFirst("\\d+.", "").replace("<br>", "\n");

                anser = cursorExam.getString(cursorExam.getColumnIndex("anser"));        // anser
                tv_name.setText(name);
                tv_anser.setText("答案 " + anser);
                tv_analysis.setText(cursorExam.getString(cursorExam.getColumnIndex("analysis")));
                tv_commons.setText(commons);
            } else {
                Log.i("msg", "未查询到数据Exam");
            }

            Cursor cursorRadio = database.query(TABLE_RADIO,
                    new String[]{"id", "a", "b", "c", "d", "e"},
                    "id = ?",
                    new String[]{id},
                    null,
                    null,
                    null);
            cursorRadio.moveToFirst();

            if (cursorRadio != null) {
                // 设置Radio
                rb_a.setText(cursorRadio.getString(cursorRadio.getColumnIndex("a")));
                rb_b.setText(cursorRadio.getString(cursorRadio.getColumnIndex("b")));
                rb_c.setText(cursorRadio.getString(cursorRadio.getColumnIndex("c")));
                rb_d.setText(cursorRadio.getString(cursorRadio.getColumnIndex("d")));
                rb_e.setText(cursorRadio.getString(cursorRadio.getColumnIndex("e")));
            } else {
                Log.i("msg", "未查询到数据Radio");
            }
            cursorExam.close();
            cursorRadio.close();
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(this, "未查询到数据，请先同步远程数据库。", Toast.LENGTH_SHORT).show();
            // 结束当前视图
            finish();
        } finally {
            database.close();
        }
    }

    /**
     * 初始化正确答案
     */
    private void initRightResult() {
        // 禁用所有选项
        for (int i = 0; i < rg_option.getChildCount(); i++) {
            rg_option.getChildAt(i).setEnabled(false);
        }
        // 显示正确答案
        switch (anser) {
            case "A":
                position = 0;
                break;
            case "B":
                position = 1;
                break;
            case "C":
                position = 2;
                break;
            case "D":
                position = 3;
                break;
            case "E":
                position = 4;
                break;
        }
        RadioButton button = (RadioButton) rg_option.getChildAt(position);
        // 不能使用button.setTextColor(R.color.colorAccent);
        button.setTextColor(this.getResources().getColor(R.color.colorRbRight, null));

        // 显示答案及解析
        ly_result_analysis.setVisibility(View.VISIBLE);
    }

    /**
     * 处理题目名称
     *
     * @param cursorExam
     * @return
     */
    public abstract String handleName(Cursor cursorExam);

    /**
     * 设置toolbar title
     *
     * @return
     */
    public abstract String getToolbarTitle();
    public abstract String getNumber();

    /**
     * 设置menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_practice, menu);
        return true;
    }

    /**
     * 监听toolbar的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:     // 必须有android（返回按钮）
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private int FLING_MIN_DISTANCE = (int) (width * 0.5);   //最小距离
    private static final int FLING_MIN_VELOCITY = 20;  //最小速度
    GestureDetector.SimpleOnGestureListener MyGestureDetector = new  GestureDetector.SimpleOnGestureListener() {
        // 滑动结束时触发
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            getWindowWidthAndHeight();
            float left = e1.getX() - e2.getX();     // e1.getX() > e2.getX()-左
            float right = e2.getX() - e1.getX();     // e1.getX() > e2.getX()-左
            // 向右滑
            if (right > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                rightFlyingHandle();
            } else if (left > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // 向左滑
                leftFlyingHandle();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    /**
     * 左滑处理
     */
    protected abstract void leftFlyingHandle();

    /**
     * 右滑处理
     */
    protected abstract void rightFlyingHandle();

    // 将触摸事件传递给gestureDetector-当子view消费touch事件时使用dispatchTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取屏幕宽度
     */
    private void getWindowWidthAndHeight() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        this.width = metrics.widthPixels;
//        this.heigth = metrics.heightPixels;
    }
}