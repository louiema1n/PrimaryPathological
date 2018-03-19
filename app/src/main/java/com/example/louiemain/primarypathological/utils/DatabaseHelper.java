package com.example.louiemain.primarypathological.utils;/**
 * @description
 * @author&date Created by louiemain on 2018/3/19 22:53
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Pragram: PrimaryPathological
 * @Type: Class
 * @Description: 数据库访问工具类
 * @Author: louiemain
 * @Created: 2018/3/19 22:53
 **/
public class DatabaseHelper extends SQLiteOpenHelper {

    private String createSQL = "create table student("
            + "id integer primary key  autoincrement not null ,"
            + "name varchar(20) not null,"
            + "age integer not null ,"
            + "gender varchar(2) not null)";

    /**
     * @description 调用父类的构造函数
     * @author louiemain
     * @date Created on 2018/3/19 22:55
     * @param context 上下文
     * @param name 数据库名字
     * @param factory 一个可选的cursor工厂
     * @param version 数据库模型版本
     * @return
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * @description 数据库第一次创建的时候会调用这个方法
     * 根据需要对传入的SQLiteDatabase对象填充表和初始化数据
     * @author louiemain
     * @date Created on 2018/3/19 22:57
     * @param db
     * @return void
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSQL);
    }

    /**
     * @description 当数据库进行升级的时候调用这个方法
     * 一般我们在这个方法里边删除数据库表，并建立新的数据库表。
     * @author louiemain
     * @date Created on 2018/3/19 22:58
     * @param db
     * @param oldVersion
     * @param newVersion
     * @return void
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE student ADD COLUMN other TEXT");
    }

    /**
     * @description 每次成功打开数据库的时候执行
     * @author louiemain
     * @date Created on 2018/3/19 22:59
     * @param db 
     * @return void
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
