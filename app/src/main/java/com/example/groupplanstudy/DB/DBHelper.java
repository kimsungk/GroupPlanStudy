package com.example.groupplanstudy.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;

    public DBHelper(@Nullable Context context) {
        super(context, "myDB", null, 1);
    }

    //추가 마이학습 캘린더 데이터 입력
    public void insert(String userid, String date, String content){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("Insert into myDiary (userid,diaryDate,content) values" +
                "('"+userid+"','"+date+"','"+content+"')");
        sqLiteDatabase.close();
    }

    //테이블 조회
    public ArrayList<String> getResult(String date, String uid){
        sqLiteDatabase = getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        Cursor cursor =
                sqLiteDatabase.rawQuery("SELECT * " +
                        "FROM myDiary " +
                        "where userid='"+uid+"' and diaryDate='"+date+"'"
                        ,null);

        while(cursor.moveToNext()){
            Log.v("test11111" ,cursor.getString(3) );
            result.add(cursor.getString(3));
        }
        cursor.close();
        sqLiteDatabase.close();

        return result;
    }

    //수정
    //uid는 넣을지말지 고려해서 확인할것
    public void update(String content, String uid, String date){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE myDiary set content=" +
                "'"+content+"' where userid='"+uid+"' and diaryDate='"+date+"'");
        sqLiteDatabase.close();
    }

    //삭제
    public void delete(String uid, String date){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE from myDiary " +
                "where userid='"+uid+"' and diaryDate='"+date+"'");
    }


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
