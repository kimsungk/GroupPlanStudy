package com.example.groupplanstudy.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;

    public DBHelper(@Nullable Context context) {
        super(context, "myDB", null, 1);
    }

    //추가


    //수정


    //삭제


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //테이블 없을경우 생성
        sqLiteDatabase.execSQL("create table if not exists " +
                "myDiary(id INTEGER primary key AUTOINCREMENT, " +
                "userid varchar, " +
                "diaryDate char(10), " +
                "content varchar(500));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        onCreate(sqLiteDatabase);
    }
}
