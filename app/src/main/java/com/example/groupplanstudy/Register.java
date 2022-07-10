package com.example.groupplanstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupplanstudy.Server.Adapter.UserAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.LoginService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    EditText editRegEmail,editRegNickName,editRegPassword,editRegPassCheck,editRegMyIntro;

    private Retrofit retrofit;
    private UserAdapter userAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegEmail = findViewById(R.id.editRegEmail);
        editRegNickName = findViewById(R.id.editRegNickName);
        editRegPassword = findViewById(R.id.editRegPassword);
        editRegPassCheck = findViewById(R.id.editRegPassCheck);
        editRegMyIntro = findViewById(R.id.editRegMyIntro);

        Button btnRegCancel = findViewById(R.id.btnRegCancel);
        Button btnRegister2 = findViewById(R.id.btnRegister2);

        btnRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //패스워드 검사
                if(!editRegPassword.getText().toString().equals(editRegPassCheck.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "비밀번호가 일치하지 않습니다. 비밀번호를 확인하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editRegEmail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Email을 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editRegNickName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "NickName을 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (editRegPassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Password를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (editRegPassCheck.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "PasswordCheck를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (editRegMyIntro.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "자기소개를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = editRegEmail.getText().toString();
                final String introduce = editRegMyIntro.getText().toString();
                final String nickname = editRegNickName.getText().toString();
                final String password = editRegPassword.getText().toString();

                user = new User();

                user.setEmail(email);
                user.setIntroduce(introduce);
                user.setNickname(nickname);
                user.setPassword(password);

                LoginService loginService
                        = Client.getClient().create(LoginService.class);

                Call<ResponseBody> call =loginService.createUser(user);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            finish();
                            Toast.makeText(getApplicationContext(),"정상적으로회원가입이 완료되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("message : ",t.getMessage());
                    }
                });
            }
        });


        btnRegCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}