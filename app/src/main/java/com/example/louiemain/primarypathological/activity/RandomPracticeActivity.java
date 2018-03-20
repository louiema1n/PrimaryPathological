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
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.utils.DatabaseHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RandomPracticeActivity extends Activity implements View.OnClickListener {

    private static final String TABLE_EXAM = "blcjexam";
    private static final String TABLE_RADIO = "radio";
    private Toolbar toolbar_simple;
    private TextView tv_title;
    private Button btn_query;
    private Button btn_insert;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_practice);
        initView();

        // 设置menu
        toolbar_simple.inflateMenu(R.menu.menu);
        tv_title.setText(this.getString(R.string.random) + this.getString(R.string.actionbar_Practice_text));

        // 获取数据库操作对象
        helper = new DatabaseHelper(this, "topic", null, 5);
    }

    private void initView() {
        toolbar_simple = (Toolbar) findViewById(R.id.toolbar_simple);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_insert:
                getData();
                break;
        }
//        database.close();
    }

    private void query() {
        // 得到数据库操作对象-读取模式
        database = helper.getReadableDatabase();
        Cursor cursorExam = database.query(TABLE_EXAM,
                new String[]{"id", "name", "catalog", "type", "eid", "commons", "anser", "analysis", "rid"},
                null,
                null,
                null,
                null,
                null);

        // cursor置顶
        cursorExam.moveToFirst();
        String rid = cursorExam.getString(cursorExam.getColumnIndex("rid"));
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
                new String[]{rid},
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
    }

    private void insert(String jsonString) {
        //获得SQLiteDatabase对象，读写模式
        database = helper.getWritableDatabase();

        //ContentValues类似HashMap，区别是ContentValues只能存简单数据类型，不能存对象
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            ContentValues values = new ContentValues();
            values.put("name", jsonObject.optString("name"));
            values.put("catalog", jsonObject.optString("catalog"));
            values.put("type", jsonObject.optString("type"));
            values.put("eid", jsonObject.optString("eid"));
            values.put("commons", jsonObject.optString("commons"));
            values.put("anser", jsonObject.optString("anser"));
            values.put("analysis", jsonObject.optString("analysis"));
            values.put("rid", jsonObject.optInt("rid"));
            //执行插入操作
            database.insert(TABLE_EXAM, null, values);
            values = new ContentValues();
            String radio = jsonObject.optString("radio");
            JSONObject radioObj = new JSONObject(radio);
            values.put("a", radioObj.optString("a"));
            values.put("b", radioObj.optString("b"));
            values.put("c", radioObj.optString("c"));
            values.put("d", radioObj.optString("d"));
            values.put("e", radioObj.optString("e"));
            database.insert(TABLE_RADIO, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 必须开启子线程访问网络-添加权限
     */
    private void getData() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                URL url = null;
                String result = "";
                try {
                    url = new URL("http://192.168.110.94/blcj/get/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);

                    if (conn.getResponseCode() == 200) {
                        // 连接成功
                        InputStream is = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String str = null;
                        while ((str = br.readLine()) != null) {
                            // 还有数据
                            result += str;
                        }
                        br.close();
                        is.close();
                        insert(result);
                    }
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

}
