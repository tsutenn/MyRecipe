package com.xrvislab.myrecipe;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeItem {
    public final int id;
    public final String name;
    public final String information;
    public final String avatar;

    public final List<String> steps;
    public final List<String> images;
    public final List<String> tags;

    public RecipeItem(int id, String name, String information, String avatar, List<String> steps, List<String> images, List<String> tags){
        this.id = id;
        this.name = name;
        this.information = information;
        this.avatar = avatar;

        this.steps = new ArrayList<String>();
        this.images = new ArrayList<String>();
        this.tags = new ArrayList<String>();
        this.steps.addAll(steps);
        this.images.addAll(images);
        this.tags.addAll(tags);
    }

    @NonNull
    @Override
    public String toString(){
        String rnt = id + "\n" + name + "\n" + avatar + "\n" + information + "\n";
        for(int i = 0; i < steps.size(); i++){
            rnt = rnt + " [" + i + "] " + steps.get(i) + " >" + images.get(i) + "\n";
        }
        for(int i = 0; i < tags.size(); i++){
            rnt = rnt + " " + tags.get(i);
        }
        return rnt + "\n";
    }
}
