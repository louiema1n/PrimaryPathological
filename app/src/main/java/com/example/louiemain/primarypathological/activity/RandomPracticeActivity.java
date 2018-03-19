package com.example.louiemain.primarypathological.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.utils.DatabaseHelper;

public class RandomPracticeActivity extends Activity implements View.OnClickListener {

    private static final String TABLE_NAME = "student";
    private Toolbar toolbar_simple;
    private TextView tv_title;
    private Button btn_query;
    private Button btn_insert;

    private DatabaseHelper helper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_practice);
        initView();

        // 设置menu
        toolbar_simple.inflateMenu(R.menu.menu);
        tv_title.setText(this.getString(R.string.random) + this.getString(R.string.actionbar_Practice_text));

        // 获取数据库操作对象
        helper = new DatabaseHelper(this, "testdb", null, 1);
    }

    private void initView() {
        toolbar_simple = (Toolbar) findViewById(R.id.toolbar_simple);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_insert:
                insert();
                break;
        }
        database.close();
    }

    private void query() {
        // 得到数据库操作对象-读取模式
        database = helper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{"id", "name", "age", "gender"},
                null,
                null,
                null,
                null,
                null);
        // cursor置顶
        cursor.moveToFirst();
        if (cursor != null) {
            Log.i("result", cursor.getString(cursor.getColumnIndex("name")));
        } else {
            Log.i("msg", "未查询到数据");
        }
        cursor.close();
    }

    private void insert() {
        //获得SQLiteDatabase对象，读写模式
        database = helper.getWritableDatabase();

        //ContentValues类似HashMap，区别是ContentValues只能存简单数据类型，不能存对象
        ContentValues values = new ContentValues();
        values.put("name", "张三");
        values.put("age", 20);
        values.put("gender", "男");
        //执行插入操作
        database.insert(TABLE_NAME, null, values);
    }

}
