package com.example.louiemain.primarypathological.activity;

import android.app.Activity;
import android.os.Bundle;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.fragment.SettingsFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsFragment settingsFragment = new SettingsFragment();
        // 不能使用getSupportFragmentManager()--3.0以上
        getFragmentManager().beginTransaction().replace(R.id.fl_settings, settingsFragment).commit();
    }
}
