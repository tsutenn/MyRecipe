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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button next;
    private Button createDb;
    private Button add;
    private Button update;
    private Button query;
    private TextView textView;
//    private MyDatabaseHelper dbHelper;
    private DatabaseManager dbManager;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = DatabaseManager.New(this);
//        dbHelper = new MyDatabaseHelper(this, "MyRecipe.db", null, 1); //创建一个MyDatabaseHelper对象
        createDb = (Button) findViewById(R.id.create_database);
        createDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.addUser("c1tun", "1234");
                Log.d("User test", dbManager.queryUser("c1tun").toString());
                Log.d("User test", dbManager.queryUser(1).toString());
            }
        });

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbManager.isStared(1, 1)){
                    Log.d("Star test", " " + dbManager.removeStar(1, 1));
                }
                else{
                    Log.d("Star test", " " + dbManager.addStar(1, 1));
                }
                Log.d("Star test", dbManager.queryUser("c1tun").toString());
            }
        });

        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbManager.isStared(1, 2)){
                    Log.d("Star test", " " + dbManager.removeStar(1, 2));
                }
                else{
                    Log.d("Star test", " " + dbManager.addStar(1, 2));
                }
                Log.d("Star test", dbManager.queryUser(1).toString());
            }
        });

        query = (Button) findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RecipeItem> items = dbManager.randomQuery(4);
                for(int i = 0; i < items.size(); i++){
                    Log.d("recipe item", items.get(i).toString());
                }
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
