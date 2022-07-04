package com.example.groupplanstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("회원가입");

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnLogin = findViewById(R.id.btnLogin);
        ImageButton btnGoogleLogin = findViewById(R.id.btnGoogleLogin);

        EditText editId = findViewById(R.id.editId);
        EditText editPassword = findViewById(R.id.editPassword);
        
        //회원가입버튼
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        //로그인버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editId.getText().toString();
                String password = editPassword.getText().toString();


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