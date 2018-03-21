package com.example.louiemain.primarypathological.activity;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.utils.DatabaseHelper;

import java.util.Random;

public class RandomPracticeActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_practice);
        initView();

        tv_title.setText(this.getString(R.string.random) + this.getString(R.string.actionbar_Practice_text));

        // 随机生成试题
        randomPractice(String.valueOf(new Random().nextInt(2140) + 1));

        // 隐藏答案及解析
        ly_result_analysis.setVisibility(View.GONE);
    }

    /**
     * 显示正确答案
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
     * 随机生成试题
     */
    private void randomPractice(String id) {
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
//        String rid = cursorExam.getString(cursorExam.getColumnIndex("rid"));
            if (cursorExam != null) {
                // 设置exam
                String commons = cursorExam.getString(cursorExam.getColumnIndex("commons"));    // commons
                commons = commons.replace("<br>", "\n");
                anser = cursorExam.getString(cursorExam.getColumnIndex("anser"));        // anser
                tv_name.setText(cursorExam.getString(cursorExam.getColumnIndex("name")));
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

        // 设置toolbar
        setSupportActionBar(toolbar_practice);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_practice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                // 刷新
                onDestroy();
                onCreate(null);
                // 生成随机id
                randomPractice(String.valueOf(new Random().nextInt(2140) + 1));
                break;
            case android.R.id.home:     // 必须有android
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

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

}

