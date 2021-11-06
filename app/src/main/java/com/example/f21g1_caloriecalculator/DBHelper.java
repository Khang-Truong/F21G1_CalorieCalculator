package com.example.f21g1_caloriecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="LoginDB.db";

    public DBHelper(@Nullable Context context) {
        super(context, "LoginDB.db", null, 1);
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(userId INTEGER PRIMARY KEY, userName TEXT, password TEXT, gender TEXT, age TEXT, height TEXT, weight TEXT, TDEE TEXT)");
        MyDB.execSQL("create Table calendardata(userId INTEGER , date TEXT , consumedCal TEXT, PRIMARY KEY (userID, date))");
    }

    //delete table
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }


    public void reCreateDatabase() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists calendardata");
        onCreate(MyDB);
    }

    public void updateTDEE(String TDEE, String name) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("Update users SET TDEE = ? where userName = ?", new String[] { TDEE, name});
    }

    //insert data to Login.db
    public boolean insertData(String name, String pass, String gender, String age, String height, String weight){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("userId", (byte[]) null);
        contentValues.put("username", name);
        contentValues.put("password",pass);
        contentValues.put("gender",gender);
        contentValues.put("age",age);
        contentValues.put("height",height);
        contentValues.put("weight",weight);
        long result=MyDB.insert("users",null,contentValues);
        if(result==-1)return false;
        else return true;
    }

    //check user name have already in database or not
    public Boolean checkNameExist(String name){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        Cursor cursor=MyDB.rawQuery("Select * from users where userName=?",new String[]{name});
        if(cursor.getCount()>0)return true;
        else return false;
    }

    //check account exists
    public Boolean checkUserNamePassword(String name,String pass){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        Cursor cursor=MyDB.rawQuery("Select * from users where userName=? and password=?",new String[]{name, pass});
        if(cursor.getCount()>0)return true;
        else return false;
    }

    public String[] getDetail(String name, String password, int userId) {
        String[] detail = new String[5];
        if (checkUserNamePassword(name, password)) {
            SQLiteDatabase myDB = this.getWritableDatabase();
            Cursor cursor = myDB.rawQuery("Select * from users where userId = ?", new String[] {String.format("%d", userId)});

            if (cursor.moveToFirst()) {
                detail[0] = cursor.getString(3);
                detail[1] = cursor.getString(4);
                detail[2] = cursor.getString(5);
                detail[3] = cursor.getString(6);
                detail[4] = cursor.getString(7);
//                Log.d("Cursor", detail[0]);
//                Log.d("Cursor", detail[1]);
//                Log.d("Cursor", detail[2]);
//                Log.d("Cursor", detail[3]);
//                Log.d("Cursor", detail[4]);
            }
        }

        return detail;
    }

    public int getUserId(String name, String password) {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        Cursor cursor=MyDB.rawQuery("Select userId from users where userName=? and password=?",new String[]{name, password});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            return userId;
        } else
            return -1;
    }

    public boolean insertConsumedCal(int userId, String date, String dataInput) {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("date", date);
        contentValues.put("consumedCal",dataInput);

        long result=MyDB.insert("calendardata",null,contentValues);
        if(result==-1)return false;
        else return true;

    }

    public String getConsumedCal(int userId, String date) {
        SQLiteDatabase MyDB=this.getWritableDatabase();

        Cursor cursor=MyDB.rawQuery("Select consumedCal from calendardata where userId=? and date=?",new String[]{String.format("%d", userId), date});
        if (cursor.moveToFirst()) {
            String result = cursor.getString(0);
            Log.d("DB", "Get !");
            return result;
        } else {
            Log.d("DB", "False !");
        }
            return null;
    }

    public boolean updateConsumedCal(int userId, String date, String dataInput) {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        try {
            MyDB.execSQL("Update calendardata SET consumedCal = ? where userId = ? and date = ?", new String[] { dataInput, String.format("%d", userId), date});
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
