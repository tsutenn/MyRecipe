package com.xrvislab.myrecipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECIPE = "create table Recipe ("
            + "recipe_id integer primary key, "
            + "recipe_name text, "
            + "recipe_info text,"
            + "img_url text)";

    public static final String CREATE_USER = "create table User ("
            + "user_id integer primary key autoincrement, "
            + "username text, "
            + "password text, "
            + "height real,"
            + "weight real,"
            + "target_weight real)";

    public static final String CREATE_STEP = "create table Step ("
            + "step_id integer primary key autoincrement, "
            + "recipe_id integer,"
            + "step_no integer,"
            + "step_text text, "
            + "img_url text)";

    public static final String CREATE_STAR = "create table Star (" +
            "star_id integer primary key autoincrement," +
            "recipe_id integer," +
            "user_id integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECIPE);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_STEP);
        db.execSQL(CREATE_STAR);
        Toast.makeText(mContext, "Creation succeed", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Recipe");
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Step");
        db.execSQL("drop table if exists Star");
        onCreate(db); //先将table删除，再创建db
    }
    //定义onUpgrade()->MyDatabaseHelper.java
    //执行onUpgrade()->MainActivity.java
}