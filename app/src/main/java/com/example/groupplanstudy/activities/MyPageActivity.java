package com.example.groupplanstudy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupplanstudy.Home;
import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPageActivity extends AppCompatActivity {

    private static Context context;
    TextView tvNickname,tvEmail,tvIntroduce;
    private String nickname, email, introduce;
    Button btnUpdateUser, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        context = getApplicationContext();

        tvNickname = findViewById(R.id.tvNickname);
        tvEmail = findViewById(R.id.tvEmail);
        tvIntroduce = findViewById(R.id.tvIntroduce);
        btnBack = findViewById(R.id.btnBack);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);

        String text = PreferenceManager.getString(context, "user");

        try {
            JSONObject jsonObject = new JSONObject(text);
            nickname = jsonObject.getString("nickname");
            email = jsonObject.getString("email");
            introduce = jsonObject.getString("introduce");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvIntroduce.setText(introduce);
        tvEmail.setText(email);
        tvNickname.setText(nickname);

        //유저 정보수정
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateUserActivity.class);
                startActivity(intent);
            }
        });

        //뒤로가기
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

    }
}