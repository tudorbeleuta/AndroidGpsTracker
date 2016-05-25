package com.tothe.tothe.bikeLogger.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.tothe.tothe.bikeLogger.common.Strings;
import com.tothe.tothe.bikeLogger.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tothe on 5/25/16.
 */
public class DbHelper extends SQLiteOpenHelper {


    public static final String USER_DETAILS = "USER_DETAILS";

    public static final String LOCAL_DB = "gps_storage";

    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS "
            + USER_DETAILS + "(" + "ID"
            + " integer primary key autoincrement, " + User.USER_EMAIL
            + " text not null," + User.USER_NAME + " text );";

    public DbHelper(Context context) {
        super(context, LOCAL_DB, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USER_DETAILS);

        // Create tables again
        onCreate(db);
    }

    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(USER_DETAILS, null, user.getContentValues());
        Log.i("db details update", String.valueOf(id));
    }

    //clears previus user details to uniquely save the new one
    public void singleInsert(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + USER_DETAILS);
        long id = db.insert(USER_DETAILS, null, user.getContentValues());
        Log.i("db details update", String.valueOf(id));
    }


    public List<User> getAllContacts() {
        List<User> users = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.valueOf(cursor.getString(0)));
                user.setEmail(cursor.getString(1));
                user.setName(cursor.getString(2));
                // Adding contact to list
                users.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return users;
    }

    public User getLastUserDetail() {
        List<User> users = getAllContacts();
        if (users.size() > 0) {
            return users.get(users.size() - 1);
        } else return null;
    }

}
