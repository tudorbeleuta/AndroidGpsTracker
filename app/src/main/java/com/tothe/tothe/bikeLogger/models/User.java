package com.tothe.tothe.bikeLogger.models;

import android.content.ContentValues;

import com.tothe.tothe.bikeLogger.storage.DbHelper;

/**
 * Created by tothe on 5/25/16.
 */
public class User {

    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_NAME = "USER_NAME";

    private int id;


    private String name;
    private String email;

    public User() {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //for further field updates

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, email);
        values.put(USER_NAME, name);

        return values;
    }
}
