package com.example.groupplanstudy.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.util.ArrayList;

public class MyStudyDB extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;

    public MyStudyDB(@Nullable Context context) {
        super(context, "myStudy", null, 1);
    }

    //학습타이머 테이블 데이터 입력
    public void insertTime(long userid, long diffSec, String title, String content){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("insert into myStudy (userid,diffSec,title,content) values" +
                "('"+userid+"','"+diffSec+"','"+title+"','"+content+"')");
        sqLiteDatabase.close();
    }

    //학습시간 그룹 가져오기
    public Cursor studyList(long userid){
        sqLiteDatabase = getReadableDatabase();

        return sqLiteDatabase.rawQuery("select sum(diffSec) as studySec, title " +
                "from myStudy where userid='"+userid+"' " +
                "group by title order by studySec desc",null);
    }

    //유저의 전체 학습시간 가져오기
    public Cursor studyTotalList(long userid){
        sqLiteDatabase = getReadableDatabase();

        return sqLiteDatabase.rawQuery("select diffSec, title " +
                "from myStudy where userid='"+userid+"'",null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //테이블생성
        sqLiteDatabase.execSQL("create table if not exists " +
                "myStudy(id INTEGER primary key AUTOINCREMENT, " +
                "userid bigint, " +
                "diffSec bigint, " +
                "title varchar(500), " +
                "content varchar(500));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
