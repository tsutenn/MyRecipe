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
            "user_id integer," +
            "date text)";

    public static final String CREATE_TAG = "create table Tag (" +
            "tag_id integer primary key autoincrement," +
            "tag_text text)";

    public static final String CREATE_HAS_TAG = "create table Has_tag (" +
            "relationship_id integer primary key autoincrement," +
            "recipe_id integer," +
            "tag_id integer)";

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
        db.execSQL(CREATE_TAG);
        db.execSQL(CREATE_HAS_TAG);
        Toast.makeText(mContext, "Creation succeed", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Recipe");
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Step");
        db.execSQL("drop table if exists Star");
        db.execSQL("drop table if exists Tag");
        db.execSQL("drop table if exists Has_tag");
        onCreate(db); //??????table??????????????????db
    }
    //??????onUpgrade()->MyDatabaseHelper.java
    //??????onUpgrade()->MainActivity.java
}