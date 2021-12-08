package com.xrvislab.myrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button next;
    private Button createDb;
    private Button add;
    private Button update;
    private Button query;
    private TextView textView;
    private MyDatabaseHelper dbHelper;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "EsliteBookstore.db", null, 1); //创建一个MyDatabaseHelper对象

        createDb = (Button) findViewById(R.id.create_database);
        createDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase(); //getReadable/WritableDatabase(), 返回一个SQLiteDatabase对象，经由该对象就可以对数据进行crud操作。
            }
        });

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //组装第一条数据
                values.put("name", "人间词话");
                values.put("author", "img/orange wings/【香橙烤翅】的做法.jpg");
                values.put("price", 19.9);
                //插入第一条数据
                db.insert("Book", null, values);
                values.clear();
                //组装第二条数据
                values.put("name", "废都");
                values.put("author", "贾平凹");
                values.put("price", 16.9);
                //插入第一条数据
                db.insert("Book", null, values);
            }
        });

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 9.99);
                db.update("Book", values, "name = ?", new String[] {"人间词话"});
            }
        });

        query = (Button) findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "book name is " + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book price is " + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        AssetManager am = this.getAssets();
        imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeStream(am.open("img/orange wings/【香橙烤翅】的做法.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bm);
    }
}
