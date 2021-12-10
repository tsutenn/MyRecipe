package com.xrvislab.myrecipe;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StarItem implements Comparable<StarItem> {
    public final int user_id;
    public final int recipe_id;
    public final String date;

    public final int year;
    public final int month;
    public final int day_of_month;

    public StarItem(int user_id, int recipe_id, String date){
        this.user_id = user_id;
        this.recipe_id = recipe_id;
        this.date = date;

        String[] str = date.split("-");
        this.year = Integer.parseInt(str[0]);
        this.month = Integer.parseInt(str[1]);
        this.day_of_month = Integer.parseInt(str[2]);
    }

    @Override
    public int compareTo(StarItem o) {
        @SuppressLint("SimpleDateFormat") DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Long num = 0L;

        try {
            Date end = dft.parse(this.date);
            Date start = dft.parse(o.date);

            Long starTime=start.getTime();
            Long endTime=end.getTime();

            num = endTime-starTime;//时间戳相差的毫秒数
            num /= 24 * 60 * 60 * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return num.intValue();
    }

    @NonNull
    @Override
    public String toString(){
        return "user="+user_id+", recipe="+recipe_id+", date="+date;
    }
}
