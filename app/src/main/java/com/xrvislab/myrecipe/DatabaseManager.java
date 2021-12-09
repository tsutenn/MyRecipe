package com.xrvislab.myrecipe;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class DatabaseManager {
    private Context context;
    private SQLiteDatabase writableDatabase;

    private static DatabaseManager instance;

    public DatabaseManager(Context context) {
        this.context = context;
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this.context, "MyRecipe.db", null, 1);
        writableDatabase = dbHelper.getWritableDatabase();
    }

    public static DatabaseManager New(Context context) {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(context);

                    Cursor cursor = instance.writableDatabase.rawQuery("select * from Recipe", null);
                    int amount = cursor.getCount();
                    if(amount == 0){
                        ContentValues recipe_values = new ContentValues();
                        ContentValues step_values = new ContentValues();
                        InputStreamReader is = null;

                        try {
                            is = new InputStreamReader(context.getAssets().open("recipes.txt"));
                            BufferedReader reader = new BufferedReader(is);
                            String line;
                            int cnt = 0;
                            int recipe_id = 0;

                            while ((line = reader.readLine()) != null) {
                                if(line.equals("")){
                                    cnt = 0;
                                }

                                else if(cnt == 0){
                                    recipe_id = Integer.parseInt(line);
                                    recipe_values.put("recipe_id", recipe_id);
                                    cnt++;
                                }
                                else if(cnt == 1){
                                    recipe_values.put("recipe_name", line);
                                    cnt++;
                                }
                                else if(cnt == 2){
                                    recipe_values.put("img_url", line);
                                    cnt++;
                                }
                                else if(cnt == 3){
                                    recipe_values.put("recipe_info", line);
                                    instance.writableDatabase.insert("Recipe", null, recipe_values);
                                    recipe_values.clear();
                                    Log.d("read file", "Recipe " + recipe_id);
                                    cnt++;
                                }

                                else if(cnt > 3){
                                    String[] data = line.split("#");
                                    step_values.put("recipe_id", recipe_id);
                                    step_values.put("step_no", Integer.parseInt(data[0]));
                                    step_values.put("step_text", data[1]);
                                    step_values.put("img_url", data[2]);
                                    instance.writableDatabase.insert("Step", null, step_values);
                                    step_values.clear();
                                    cnt++;
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cursor.close();

                        Log.d("DatabaseManger", "#\n###################\n# Create database #\n###################");
                    }
                }
            }
        }
        return instance;
    }

    public List<RecipeItem> query(){
        List<RecipeItem> recipes = new ArrayList<RecipeItem>();

        Cursor cursor = writableDatabase.rawQuery("select * from Recipe", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("recipe_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("recipe_name"));
                @SuppressLint("Range") String information = cursor.getString(cursor.getColumnIndex("recipe_info"));
                @SuppressLint("Range") String avatar = cursor.getString(cursor.getColumnIndex("img_url"));

                List<String> steps = new ArrayList<>();
                List<String> images = new ArrayList<>();
                Cursor cursor_steps = writableDatabase.rawQuery("select * from Step where recipe_id is ? order by step_no asc", new String[]{String.valueOf(id)});
                if(cursor_steps.moveToFirst()){
                    do{
                        @SuppressLint("Range") String step = cursor_steps.getString(cursor_steps.getColumnIndex("step_text"));
                        @SuppressLint("Range") String image = cursor_steps.getString(cursor_steps.getColumnIndex("img_url"));
                        steps.add(step);
                        images.add(image);
                    } while (cursor_steps.moveToNext());
                }

                recipes.add(new RecipeItem(id, name, information, avatar, steps, images));
                cursor_steps.close();
            } while (cursor.moveToNext());
        }
        cursor.close();

        return recipes;
    }

    public RecipeItem query(int id){
        Cursor cursor = writableDatabase.rawQuery("select * from Recipe where recipe_id is ?", new String[]{String.valueOf(id)});

        if(!cursor.moveToFirst()){
            cursor.close();
            return null;
        }

        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("recipe_name"));
        @SuppressLint("Range") String information = cursor.getString(cursor.getColumnIndex("recipe_info"));
        @SuppressLint("Range") String avatar = cursor.getString(cursor.getColumnIndex("img_url"));
        List<String> steps = new ArrayList<>();
        List<String> images = new ArrayList<>();
        Cursor cursor_steps = writableDatabase.rawQuery("select * from Step where recipe_id is ? order by step_no asc", new String[]{String.valueOf(id)});
        if(cursor_steps.moveToFirst()){
            do{
                @SuppressLint("Range") String step = cursor_steps.getString(cursor_steps.getColumnIndex("step_text"));
                @SuppressLint("Range") String image = cursor_steps.getString(cursor_steps.getColumnIndex("img_url"));
                steps.add(step);
                images.add(image);
            } while (cursor_steps.moveToNext());
        }
        cursor_steps.close();
        cursor.close();

        return new RecipeItem(id, name, information, avatar, steps, images);
    }

    public List<RecipeItem> randomQuery(int num) {
        List<RecipeItem> recipes = query();
        List<RecipeItem> randomRecipes = new ArrayList<RecipeItem>();
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Random random = new Random(hour);
        int len = recipes.size();
        int minLen = len - num;

        if(len > minLen){
            while(len > minLen){
                int index = random.nextInt(len);
                randomRecipes.add(recipes.get(index));
                recipes.remove(index);
                len--;
            }
        }

        return  randomRecipes;
    }

    public int addUser(String username, String password, float height, float weight, float target_weight){
        if(is_username_exist(username)){
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("height", height);
        values.put("weight", weight);
        values.put("target_weight", target_weight);
        writableDatabase.insert("User", null, values);

        return 1;
    }

    public int addUser(String username, String password){
        return addUser(username, password, 0f, 0f, 0f);
    }

    public boolean is_username_exist(String username){
        Cursor cursor = writableDatabase.rawQuery("select * from User where username is ?", new String[]{username});
        if(cursor.moveToFirst() && cursor.getCount() != 0){
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    public boolean is_username_exist(int user_id){
        Cursor cursor = writableDatabase.rawQuery("select * from User where user_id is ?", new String[]{String.valueOf(user_id)});
        if(cursor.moveToFirst() && cursor.getCount() != 0){
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    public boolean check_password(String username, String password){
        Cursor cursor = writableDatabase.rawQuery("select * from User where username is ?", new String[]{username});
        if(cursor.moveToFirst() && cursor.getCount() != 0){
            @SuppressLint("Range") String _password = cursor.getString(cursor.getColumnIndex("password"));
            if(password.equals(_password)){
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }

    public boolean check_password(int user_id, String password){
        Cursor cursor = writableDatabase.rawQuery("select * from User where user_id is ?", new String[]{String.valueOf(user_id)});
        if(cursor.moveToFirst() && cursor.getCount() != 0){
            @SuppressLint("Range") String _password = cursor.getString(cursor.getColumnIndex("password"));
            if(password.equals(_password)){
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }

    public boolean changePassword(String username, String old_password, String new_password){
        if(check_password(username, old_password)){
            ContentValues values = new ContentValues();
            values.put("password", new_password);
            writableDatabase.update("User", values, "username=?", new String[]{username});
            return true;
        }

        return false;
    }

    public boolean changePassword(int user_id, String old_password, String new_password){
        if(check_password(user_id, old_password)){
            ContentValues values = new ContentValues();
            values.put("password", new_password);
            writableDatabase.update("User", values, "user_id=?", new String[]{String.valueOf(user_id)});
            return true;
        }

        return false;
    }

    public boolean updateUser(String username, float height, float weight, float target_weight){
        if(is_username_exist(username)){
            ContentValues values = new ContentValues();
            values.put("height", height);
            values.put("weight", weight);
            values.put("target_weight", target_weight);
            writableDatabase.update("User", values, "username=?", new String[]{username});
            return true;
        }

        return false;
    }

    public boolean updateUser(int user_id, float height, float weight, float target_weight){
        if(is_username_exist(user_id)){
            ContentValues values = new ContentValues();
            values.put("height", height);
            values.put("weight", weight);
            values.put("target_weight", target_weight);
            writableDatabase.update("User", values, "user_id=?", new String[]{String.valueOf(user_id)});
            return true;
        }

        return false;
    }

    public UserItem queryUser(String username){
        Cursor cursor = writableDatabase.rawQuery("select * from User where username is ?", new String[]{username});

        if(cursor.moveToFirst() && cursor.getCount() != 0){
            UserItem item;

            @SuppressLint("Range") int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") float height = cursor.getFloat(cursor.getColumnIndex("height"));
            @SuppressLint("Range") float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
            @SuppressLint("Range") float target_weight = cursor.getFloat(cursor.getColumnIndex("target_weight"));

            Cursor cursor_star = writableDatabase.rawQuery("select * from Star where user_id is ?", new String[]{String.valueOf(user_id)});
            if(cursor_star.moveToFirst() && cursor_star.getCount() != 0){
                List<Integer> stars = new ArrayList<>();
                do{
                    @SuppressLint("Range") int recipe_id = cursor_star.getInt(cursor_star.getColumnIndex("recipe_id"));
                    stars.add(recipe_id);
                } while (cursor_star.moveToNext());

                item = new UserItem(user_id, username, password, height, weight, target_weight, stars);
            }
            else{
                item = new UserItem(user_id, username, password, height, weight, target_weight);
            }

            cursor_star.close();
            cursor.close();
            return item;
        }

        cursor.close();
        return null;
    }

    public UserItem queryUser(int user_id){
        Cursor cursor = writableDatabase.rawQuery("select * from User where user_id is ?", new String[]{String.valueOf(user_id)});

        if(cursor.moveToFirst() && cursor.getCount() != 0){
            UserItem item;

            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") float height = cursor.getFloat(cursor.getColumnIndex("height"));
            @SuppressLint("Range") float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
            @SuppressLint("Range") float target_weight = cursor.getFloat(cursor.getColumnIndex("target_weight"));

            Cursor cursor_star = writableDatabase.rawQuery("select * from Star where user_id is ?", new String[]{String.valueOf(user_id)});
            if(cursor_star.moveToFirst() && cursor_star.getCount() != 0){
                List<Integer> stars = new ArrayList<>();
                do{
                    @SuppressLint("Range") int recipe_id = cursor_star.getInt(cursor_star.getColumnIndex("recipe_id"));
                    stars.add(recipe_id);
                } while (cursor_star.moveToNext());

                item = new UserItem(user_id, username, password, height, weight, target_weight, stars);
            }
            else{
                item = new UserItem(user_id, username, password, height, weight, target_weight);
            }

            cursor_star.close();
            cursor.close();
            return item;
        }

        cursor.close();
        return null;
    }

    public boolean addStar(int user_id, int recipe_id){
        if(isStared(user_id, recipe_id)){
           return false;
        }
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipe_id);
        values.put("user_id", user_id);
        writableDatabase.insert("Star", null, values);
        return true;
    }

    public boolean isStared(int user_id, int recipe_id){
        Cursor cursor = writableDatabase.rawQuery("select * from Star where user_id is ? and recipe_id is ?",
                new String[]{String.valueOf(user_id), String.valueOf(recipe_id)});

        if(cursor.moveToFirst() && cursor.getCount() != 0){
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    public boolean removeStar(int user_id, int recipe_id){
        if(isStared(user_id, recipe_id)){
            writableDatabase.delete("Star",
                    "recipe_id=? and user_id=?",
                    new String[]{String.valueOf(recipe_id), String.valueOf(user_id)});
            return true;
        }

        return false;
    }
}