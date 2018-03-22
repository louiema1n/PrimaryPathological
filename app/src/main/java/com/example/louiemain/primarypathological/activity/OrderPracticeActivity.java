package com.example.louiemain.primarypathological.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.base.BasePracticeActivity;

public class OrderPracticeActivity extends BasePracticeActivity {

    private int id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.generatePractice(String.valueOf(id));
    }

    @Override
    public String handleName(Cursor cursorExam) {
        // 去掉前面的数字前面加上id
        String name = cursorExam.getString(cursorExam.getColumnIndex("name"));
        name = id + name.replaceFirst("\\d+", "");
        return name;
    }

    @Override
    public String getToolbarTitle() {
        return this.getString(R.string.order) + this.getString(R.string.actionbar_Practice_text);
    }

    @Override
    public String getNumber() {
        return "当前题目: " + id + "/2140";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                nextTopic();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 下一个题目
     */
    public void nextTopic() {
        id++;
        // 刷新
        onDestroy();
        onCreate(null);
        // 生成随机id
        if (id > 2140) {
            finish();
            Toast.makeText(this, "恭喜你！完成了所有的题目练习。", Toast.LENGTH_SHORT).show();
        } else {
            super.generatePractice(String.valueOf(id));
        }
    }

    /**
     * 上一个题目
     */
    public void upTopic() {
        id--;
        if (id < 1) {
            Toast.makeText(this, "这是第一个题目，别翻了。", Toast.LENGTH_SHORT).show();
        } else {
            onDestroy();
            onCreate(null);
            super.generatePractice(String.valueOf(id));
        }
    }

    @Override
    protected void leftFlyingHandle() {
        nextTopic();
    }

    @Override
    protected void rightFlyingHandle() {
        upTopic();
    }

}
