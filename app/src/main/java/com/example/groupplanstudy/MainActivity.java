package com.example.groupplanstudy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.groupplanstudy.DB.DBHelper;
import com.example.groupplanstudy.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    //데이터베이스
    SQLiteDatabase sqLiteDatabase;
    //내부클래스 MyDBHelper 는 DDL 작업(테이블 생성 등)을 수행하고
    //sqLiteDatabase DML 작업을 수행함
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB쓰기권한 업데이트
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(sqLiteDatabase, 1,2);


        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnLogin = findViewById(R.id.btnLogin);
        ImageButton btnGoogleLogin = findViewById(R.id.btnGoogleLogin);

        EditText editId = findViewById(R.id.editId);
        EditText editPassword = findViewById(R.id.editPassword);
        
        //회원가입버튼
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        //로그인버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editId.getText().toString();
                String password = editPassword.getText().toString();

                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
        //구글로그인버튼
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "구글로그인",Toast.LENGTH_SHORT).show();
            }
        });

    }

}