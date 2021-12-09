package com.xrvislab.myrecipe;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class UserItem {
    public final int id;
    public final String username;
    public final float height;
    public final float weight;
    public final float target_weight;

    public final List<Integer> stars;

    private final String password;

    public UserItem(int id, String username, String password, float height, float weight, float target_weight, List<Integer> stars){
        this.id = id;
        this.username = username;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.target_weight = target_weight;

        this.stars = new ArrayList<Integer>();
        this.stars.addAll(stars);
    }

    public UserItem(int id, String username, String password, float height, float weight, float target_weight){
        this.id = id;
        this.username = username;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.target_weight = target_weight;

        this.stars = new ArrayList<Integer>();
    }

    public UserItem(int id, String username, String password, List<Integer> stars){
        this.id = id;
        this.username = username;
        this.password = password;
        this.height = 0f;
        this.weight = 0f;
        this.target_weight = 0f;

        this.stars = new ArrayList<Integer>();
        this.stars.addAll(stars);
    }

    public UserItem(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
        this.height = 0f;
        this.weight = 0f;
        this.target_weight = 0f;

        this.stars = new ArrayList<Integer>();
    }

    public boolean check(String password){
        if(this.password.equals(password)){
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString(){
        String rnt = id + " " + username + " " + password + " " + height + " " + weight + " " + target_weight + " {";
        for(int i = 0; i < stars.size(); i++){
            rnt = rnt + " " + stars.get(i);
        }
        rnt = rnt + " }\n";
        return rnt;
    }
}
