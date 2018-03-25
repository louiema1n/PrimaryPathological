package com.example.louiemain.primarypathological.fragment;/**
 * @description
 * @author&date Created by louiemain on 2018/3/25 16:36
 */

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.example.louiemain.primarypathological.R;

/**
 * @Pragram: PrimaryPathological
 * @Type: Class
 * @Description: 设置相关fragment
 * @Author: louiemain
 * @Created: 2018/3/25 16:36
 **/
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 引入创建的xml文件
        addPreferencesFromResource(R.xml.pref_settings);
    }
}
