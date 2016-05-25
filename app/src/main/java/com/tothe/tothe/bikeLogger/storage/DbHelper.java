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

        //   db.execSQL("SELECT name FROM sqlite_master WHERE type='table' AND name='USER_DETAILS';");
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                //       Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }
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

}
