package com.github.bassaer.example;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.Nullable;

public class User implements IChatUser {
    Integer id;
    String name;
    Bitmap icon;

    public User(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String getId() {
        return this.id.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Bitmap getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    @Nullable
    @Override
    public String getUrl() {
        return null;
    }

    @Nullable
    @Override
    public Integer getTextColor() {
        return null;
    }

    @Nullable
    @Override
    public Integer getDrawable() {
        return null;
    }
}