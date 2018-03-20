package com.example.louiemain.primarypathological.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.base.BasePager;
import com.example.louiemain.primarypathological.pager.ExamPager;
import com.example.louiemain.primarypathological.pager.MinePager;
import com.example.louiemain.primarypathological.pager.OthersPager;
import com.example.louiemain.primarypathological.pager.PracticePager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar_simple;

    private RadioGroup rg_bottom_tag;

    private Fragment fragment1, fragment2, fragment3, fragment4;

    // 视图集合
    private List<BasePager> basePagers;
    // 当前点击视图的位置-对应其在集合中的index
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // 初始化pager集合
        basePagers = new ArrayList<>();
        basePagers.add(new PracticePager(this));
        basePagers.add(new ExamPager(this));
        basePagers.add(new MinePager(this));
        basePagers.add(new OthersPager(this));

        // 设置RadioGroup监听-将对应的视图添加到fragment
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        // 默认选中练习
        rg_bottom_tag.check(R.id.rb_practice);
    }

    private void initView() {
        toolbar_simple = (Toolbar) findViewById(R.id.toolbar_simple);

        // 设置menu
//        toolbar_simple.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar_simple);

        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_syn_db) {
            showProgressDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);     // 设置点击back键取消
        progressDialog.setCanceledOnTouchOutside(false); // 设置是否点击dialog外其他区域取消进度条
        progressDialog.setTitle("我是进度条");
        progressDialog.incrementProgressBy(20);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
            }
        });

        // 条形进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();
    }

    /**
     * @Description: 设置监听
     * @Author: louiemain
     * @Date: 2018-03-19 12:22
     * @param
     * @return:
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                default:
                    position = 0;
                    if (fragment1 == null) {
                        fragment1 = getFragment(position);
                        addFragment(fragment1);
                    }
                    // 设置Fragment
                    showFragment(fragment1);
                    break;
                case R.id.rb_exam:
                    position = 1;
                    if (fragment2 == null) {
                        fragment2 = getFragment(position);
                        addFragment(fragment2);
                    }
                    // 设置Fragment
                    showFragment(fragment2);
                    break;
                case R.id.rb_mine:
                    position = 2;
                    if (fragment3 == null) {
                        fragment3 = getFragment(position);
                        addFragment(fragment3);
                    }
                    // 设置Fragment
                    showFragment(fragment3);
                    break;
                case R.id.rb_others:
                    position = 3;
                    if (fragment4 == null) {
                        fragment4 = getFragment(position);
                        addFragment(fragment4);
                    }
                    // 设置Fragment
                    showFragment(fragment4);
                    break;
                case R.id.action_syn_db:
                    Toast.makeText(MainActivity.this, "同步数据库", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * @Description: 隐藏所有fragment，显示当前点击fragment
     * @Author: louiemain
     * @Date: 2018-03-19 17:09
     * @param fragment
     * @return: void
     */
    private void showFragment(Fragment fragment) {
        // 1.得到fragmentmanager
        FragmentManager manager = getSupportFragmentManager();

        // 2.开启事务
        FragmentTransaction transaction = manager.beginTransaction();

        // 3.隐藏所有fragment
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }
        // 显示当前fragment
        transaction.show(fragment);

        // 4.提交事务
        transaction.commit();
    }

    /**
     * @Description: 根据当前视图实例化对应的fragment
     * @Author: louiemain
     * @Date: 2018-03-19 17:10
     * @param index
     * @return: android.support.v4.app.Fragment
     */
    private Fragment getFragment(int index) {
        final int i = index;
        return new Fragment() {
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                // 得到当前选中的pager
                BasePager pager = basePagers.get(i);
                if (pager != null && !pager.isInitData) {
                    // 没有初始化data，则初始化
                    pager.initData();
                    pager.isInitData = true;
                    return pager.rootView;
                }
                return null;
            }
        };
    }

    /**
     * @Description: 添加fragment到transaction
     * @Author: louiemain
     * @Date: 2018-03-19 11:55
     * @param
     * @return: void
     */
    private void addFragment(Fragment fragment) {
        // 1.得到fragmentmanager
        FragmentManager manager = getSupportFragmentManager();

        // 2.开启事务
        FragmentTransaction transaction = manager.beginTransaction();

        // 3.添加fragment
        transaction.add(R.id.fl_main_content, fragment);

        // 4.提交事务
        transaction.commit();
    }

}
