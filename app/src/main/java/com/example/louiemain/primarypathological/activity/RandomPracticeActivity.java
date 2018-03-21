package com.example.louiemain.primarypathological.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.utils.DatabaseHelper;

import java.util.Random;

public class RandomPracticeActivity extends Activity implements View.OnClickListener {

    private static final String TABLE_EXAM = "blcjexam";
    private static final String TABLE_RADIO = "radio";
    private Toolbar toolbar_simple;
    private TextView tv_title;

    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private TextView tv_name;
    private TextView tv_anser;
    private TextView tv_analysis;
    private RadioButton rb_a;
    private RadioButton rb_b;
    private RadioButton rb_c;
    private RadioButton rb_d;
    private RadioButton rb_e;
    private Button btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_practice);
        initView();

        // 设置menu
        toolbar_simple.inflateMenu(R.menu.menu);
        tv_title.setText(this.getString(R.string.random) + this.getString(R.string.actionbar_Practice_text));

        // 获取数据库操作对象
        helper = new DatabaseHelper(this, "topic", null, 13);

        // 随机生成试题
        radnomPractice(String.valueOf(new Random().nextInt(2140) + 1));
    }

    /**
     * 随机生成试题
     */
    private void radnomPractice(String id) {
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
                tv_name.setText(cursorExam.getString(cursorExam.getColumnIndex("name")));
                tv_anser.setText("答案 " + cursorExam.getString(cursorExam.getColumnIndex("anser")));
                tv_analysis.setText(cursorExam.getString(cursorExam.getColumnIndex("analysis")));
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
        toolbar_simple = (Toolbar) findViewById(R.id.toolbar_simple);
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
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                // 生成随机id
                radnomPractice(String.valueOf(new Random().nextInt(2140) + 1));
                break;
        }
    }
}

