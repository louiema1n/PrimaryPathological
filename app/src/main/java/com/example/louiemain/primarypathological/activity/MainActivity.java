package com.example.louiemain.primarypathological.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.louiemain.primarypathological.R;
import com.example.louiemain.primarypathological.base.BasePager;
import com.example.louiemain.primarypathological.pager.ExamPager;
import com.example.louiemain.primarypathological.pager.MinePager;
import com.example.louiemain.primarypathological.pager.OthersPager;
import com.example.louiemain.primarypathological.pager.PracticePager;
import com.example.louiemain.primarypathological.utils.DatabaseHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LINK_NETWORK_FAIL = 0;
    private static final int UPDATE_SUCCESS = 1;
    private static final int SOCKET_TIMEOUT = 2;
    private static final int UPDATE_FAILURE = 3;
    private Toolbar toolbar_simple;

    private RadioGroup rg_bottom_tag;

    private Fragment fragment1, fragment2, fragment3, fragment4;

    private static final String TABLE_EXAM = "blcjexam";
    private static final String TABLE_RADIO = "radio";
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    private ProgressDialog progressDialog;

    // 是否中断线程
    private boolean ifInterrupt = false;

    // 视图集合
    private List<BasePager> basePagers;
    // 当前点击视图的位置-对应其在集合中的index
    private int position;

    // 试图识别器
    private GestureDetector gestureDetector;
    private boolean flag;
    private int FLING_MIN_DISTANCE;
    private int FLING_MIN_VELOCITY;

    // 是否滑动
    private boolean isSlide;

    // radiobutton数据
    private int[] rbs = new int[4];

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
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        // 设置menu
//        toolbar_simple.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar_simple);

        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);
        // 初始化radiobutton id数组
        for (int i = 0; i < rg_bottom_tag.getChildCount(); i++) {
            rbs[i] = rg_bottom_tag.getChildAt(i).getId();
        }
        // 获取数据库操作对象
        helper = new DatabaseHelper(this, "topic", null, 13);

        gestureDetector = new GestureDetector(this, new simpleGestureListener());

        // 初始化屏幕宽度
        if (!flag) {
            getWindowWidthAndHeight();
            flag = true;
        }

    }

    class simpleGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float left = e1.getX() - e2.getX();     // e1.getX() > e2.getX()-左
            float right = e2.getX() - e1.getX();     // e1.getX() > e2.getX()-左
            // 向右滑
            if (right > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // 切换底部栏
                position--;
                if (position < 0) {
                    position = 0;
                }
                RadioButton button = (RadioButton) rg_bottom_tag.getChildAt(position);
                button.setChecked(true);
            } else if (left > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // 向左滑
                position++;
                if (position > 3) {
                    position = 3;
                }
                RadioButton button = (RadioButton) rg_bottom_tag.getChildAt(position);
                button.setChecked(true);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_syn_db) {

            initDataBase();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param
     * @return void
     * @description 从服务器更新数据到本地数据库
     * @author louiemain
     * @date Created on 2018/3/20 20:10
     */
    private void initDataBase() {
        progressDialog = getProgressDialog(2140, MainActivity.this.getString(R.string.update));
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();

                // 解决 Can't create handler inside thread that has not called Looper.prepare()
                Looper.prepare();

                HttpURLConnection conn = null;
                URL url = null;
                int i = 1;
                for (; i <= 2140; i++) {
                    if (ifInterrupt) {
                        // 终止线程
                        break;
                    }
                    try {
                        String result = "";
//                        url = new URL("http://192.168.110.94/blcj/get/" + i);
                        url = new URL("http://192.168.1.102/blcj/get/" + i);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(3000);
                        conn.setReadTimeout(3000);

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
                    } catch (SocketTimeoutException e) {
                        // 超时处理
                        handler.sendEmptyMessage(SOCKET_TIMEOUT);
                        e.printStackTrace();
                        break;
                    } catch (UnknownHostException e) {
                        // 异常主机处理
                        handler.sendEmptyMessage(LINK_NETWORK_FAIL);
                        e.printStackTrace();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        // 关闭连接
                        conn.disconnect();
                    }
                }
                if (i == 2141) {
                    handler.sendEmptyMessage(UPDATE_SUCCESS);
                } else {
                    handler.sendEmptyMessage(UPDATE_FAILURE);
                }
            }
        }.start();
        // 关闭数据库
//        database.close();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LINK_NETWORK_FAIL:
                    Toast.makeText(MainActivity.this, "连接服务器失败，请稍后重试。", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_SUCCESS:
                    Toast.makeText(MainActivity.this, "数据更新成功。", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_FAILURE:
                    Toast.makeText(MainActivity.this, "数据更新失败，线程被终止。请退出程序后重试。", Toast.LENGTH_SHORT).show();
                    break;
                case SOCKET_TIMEOUT:
                    Toast.makeText(MainActivity.this, "服务器连接超时，请稍后重试。", Toast.LENGTH_SHORT).show();
                    break;
            }
            progressDialog.dismiss();

        }
    };

    /**
     * @param result
     * @return void
     * @description 插入数据到数据库
     * @author louiemain
     * @date Created on 2018/3/20 20:17
     */
    private void insert(String result) {
        //获得SQLiteDatabase对象，读写模式
        database = helper.getWritableDatabase();

        database.beginTransaction();
        //ContentValues类似HashMap，区别是ContentValues只能存简单数据类型，不能存对象
        try {
            JSONObject jsonObject = new JSONObject(result);
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
            // 更新进度条
            String sql = "select last_insert_rowid() from " + TABLE_RADIO;
            Cursor cursor = database.rawQuery(sql, null);
            int a = -1;
            if (cursor.moveToFirst()) {
                a = cursor.getInt(0);
            }
            progressDialog.setProgress(a);


            // 设置事务成功
            database.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            database.endTransaction();
            database.close();
        }
    }

    /**
     * @param max
     * @param title
     * @return android.app.ProgressDialog
     * @description 创建一个进度条
     * @author louiemain
     * @date Created on 2018/3/20 20:22
     */
    private ProgressDialog getProgressDialog(int max, String title) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMax(max);
        progressDialog.setCancelable(false);     // 设置点击back键取消
        progressDialog.setCanceledOnTouchOutside(false); // 设置是否点击dialog外其他区域取消进度条
        progressDialog.setTitle(title);
        progressDialog.incrementProgressBy(0);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消进度条
                dialog.dismiss();
                ifInterrupt = true;
            }
        });

        // 条形进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        return progressDialog;
    }

    /**
     * @param
     * @Description: 设置监听
     * @Author: louiemain
     * @Date: 2018-03-19 12:22
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
     * @param fragment
     * @Description: 隐藏所有fragment，显示当前点击fragment
     * @Author: louiemain
     * @Date: 2018-03-19 17:09
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
     * @param index
     * @Description: 根据当前视图实例化对应的fragment
     * @Author: louiemain
     * @Date: 2018-03-19 17:10
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
     * @param
     * @Description: 添加fragment到transaction
     * @Author: louiemain
     * @Date: 2018-03-19 11:55
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

    /**
     * 获取屏幕宽度
     */
    private void getWindowWidthAndHeight() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        this.FLING_MIN_DISTANCE = (int) (metrics.widthPixels * 0.15);
        this.FLING_MIN_VELOCITY = this.FLING_MIN_DISTANCE / 4;
//        this.heigth = metrics.heightPixels;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
