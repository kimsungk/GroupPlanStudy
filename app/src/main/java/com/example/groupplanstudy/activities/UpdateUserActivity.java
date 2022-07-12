package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupplanstudy.MainActivity;
import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.LoginService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    EditText editUpEmail,editUpNickName,editUpPassword,editUpPassCheck,editUpMyIntro;
    Button btnBack,btnUpdate,btnUpCancel;
    User user;
    long uid = 0;
    String email,password, nickname, introduce;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        
        Log.d("테스트","도착");

        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpCancel = findViewById(R.id.btnUpCancel);

        editUpEmail = findViewById(R.id.editUpEmail);
        editUpNickName = findViewById(R.id.editUpNickName);
        editUpPassword = findViewById(R.id.editUpPassword);
        editUpPassCheck = findViewById(R.id.editUpPassCheck);
        editUpMyIntro = findViewById(R.id.editUpMyIntro);

        context = getApplicationContext();
        String text = PreferenceManager.getString(context, "user");
        try {
            JSONObject jsonObject = new JSONObject(text);
            uid = jsonObject.getLong("uid");
            email = jsonObject.getString("email");
            password = jsonObject.getString("password");
            nickname = jsonObject.getString("nickname");
            introduce = jsonObject.getString("introduce");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editUpEmail.setText(email);
        editUpNickName.setText(nickname);
        editUpPassword.setText("");
        editUpPassCheck.setText("");
        editUpMyIntro.setText(introduce);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editUpPassword.getText().toString().equals(editUpPassCheck.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "비밀번호가 일치하지 않습니다. 비밀번호를 확인하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editUpEmail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Email을 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editUpNickName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "NickName을 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editUpPassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Password를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editUpPassCheck.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "PasswordCheck를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editUpMyIntro.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "자기소개를 입력하세요",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //수정할 데이터값 입력
                final String upEmail = editUpEmail.getText().toString();
                final String upIntroduce = editUpMyIntro.getText().toString();
                final String upPassword = editUpPassword.getText().toString();
                final String upNickname = editUpNickName.getText().toString();

                user = new User();
                user.setUid(uid);
                user.setEmail(upEmail);
                user.setPassword(upPassword);
                user.setNickname(upNickname);
                user.setIntroduce(upIntroduce);

                LoginService loginService
                        = Client.getClient().create(LoginService.class);

                Call<APIMessage> call = loginService.updateUser(user, uid);

                call.enqueue(new Callback<APIMessage>() {
                    @Override
                    public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);

                            Toast.makeText(getApplicationContext(),"회원수정이 완료되었습니다 다시 로그인 해주세요.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("response보기",response.body().getData().toString());
                            APIMessage apiMessage = new APIMessage();
                            apiMessage.setData(response.body().getData());

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();

                            PreferenceManager.setString(context,"user",gson.toJson(apiMessage.getData()));

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIMessage> call, Throwable t) {
                        Log.d("message : ",t.getMessage());
                    }
                });

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}