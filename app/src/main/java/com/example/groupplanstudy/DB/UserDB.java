package com.example.groupplanstudy.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;

public class UserDB extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;

    public UserDB(@Nullable Context context){
        super(context, "UserDB", null, 1);
    }

    //유저 추가
    public void userInsert(long uid, String email, String password, String introduce, String nickname){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("Insert into UserDB (uid, email, password, introduce, nickname) values" +
                "('"+uid+"', '"+email+"', '"+password+"', '"+introduce+"', '"+nickname+"')");
        sqLiteDatabase.close();
    }

    //유저 삭제(유저내용 전체삭제)->로그아웃이나, 끝날경우
    public void userDelete(){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("Drop table UserDB");
        sqLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        //테이블 없을경우 생성
        sqLiteDatabase.execSQL("create table if not exists " +
                "UserDB(id INTEGER primary key AUTOINCREMENT, " +
                "uid bigint, " +
                "email String, " +
                "password String, " +
                "introduce String, " +
                "nickname String);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists UserDB");
        onCreate(sqLiteDatabase);
    }
}
