package com.example.groupplanstudy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.groupplanstudy.DB.DBHelper;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.LoginService;
import com.example.groupplanstudy.ui.home.HomeFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

                final String id = editId.getText().toString();
                final String password = editPassword.getText().toString();

                User user = new User();

                user.setEmail(id);
                user.setPassword(password);

                LoginService loginService
                        = Client.getClient().create(LoginService.class);

                Call<APIMessage> call = loginService.loginUser(user);

                call.enqueue(new Callback<APIMessage>() {
                    @Override
                    public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {

                        if(response.isSuccessful()){
                            if(response.body().getMessage().equals("success")){
                                Log.d("APIMessage",response.body().getMessage());
                                Log.d("APIMessage",response.body().getData().toString());
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                APIMessage apiMessage = new APIMessage();
                                apiMessage.setMessage(response.body().getMessage());
                                apiMessage.setData(response.body().getData());

                                intent.putExtra("user", apiMessage);
                                startActivity(intent);
                                //데이터 넘기기
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "아이디또는 비밀번호를 틀리셨습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIMessage> call, Throwable t) {

                    }
                });



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