package com.example.knowyourfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLDBHelper extends SQLiteOpenHelper {

    //Declaring variables
    private static final String DATABASE_NAME = "UserInfoDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_UID = "authUid";

    public SQLDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ( " + COLUMN_NAME + " TEXT , " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT , " + COLUMN_UID + " TEXT PRIMARY KEY " + " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS );
        onCreate(db);
    }

    public void addUser(String name, String email, String password, String userUid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_UID, userUid);

        db.insert(TABLE_USERS,null,values);

    }

    public Boolean checkUser(String userUid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_UID + "=?", new String[]{userUid});
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void updatePassword(UserContentModel usercontent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, usercontent.password);

        db.update(TABLE_USERS,values,COLUMN_UID + "=?",new String[]{usercontent.userUid});

    }

    public Boolean checkEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS,null);

        if(cursor.getCount() == 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
