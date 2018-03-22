package com.example.louiemain.primarypathological.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.base.BasePracticeActivity;

import java.util.Random;

public class RandomPracticeActivity extends BasePracticeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.generatePractice(String.valueOf(new Random().nextInt(2140) + 1));
    }

    @Override
    public String handleName(Cursor cursorExam) {
        // 去掉前面的数字
        String name = cursorExam.getString(cursorExam.getColumnIndex("name"));
        name = name.replaceFirst("\\d+.", "");
        return name;
    }

    @Override
    public String getToolbarTitle() {
        return this.getString(R.string.random) + this.getString(R.string.actionbar_Practice_text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                // 刷新
                onDestroy();
                onCreate(null);
                // 生成随机id
                super.generatePractice(String.valueOf(new Random().nextInt(2140) + 1));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

