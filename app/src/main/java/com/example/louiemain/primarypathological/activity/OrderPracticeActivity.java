package com.example.louiemain.primarypathological.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                // 刷新
                onDestroy();
                onCreate(null);
                // 生成随机id
                super.generatePractice(String.valueOf(++id));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
