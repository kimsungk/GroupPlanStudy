package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.AdminAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.LoginService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private Context context;
    private List<User> userList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private AdminAdapter adminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        context = getApplicationContext();

        recyclerView = findViewById(R.id.user_recyclerview);

        LoginService loginService =
                Client.getClient().create(LoginService.class);
        Call<List<User>> call = loginService.userList();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Log.d("",""+gson.toJson(response.body()));

                    PreferenceManager.setString(context, "userList", gson.toJson(response.body()));
                    String users = PreferenceManager.getString(context,"userList");

                    try {
                        JSONArray jsonArray = new JSONArray(users);
                        userList = new ArrayList<>();
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject subJsonObject = jsonArray.getJSONObject(i);
                            long uid = subJsonObject.getLong("uid");
                            String password = subJsonObject.getString("password");
                            String email = subJsonObject.getString("email");
                            String introduce = subJsonObject.getString("introduce");
                            String nickname = subJsonObject.getString("nickname");

                            User user = new User();

                            user.setUid(uid);
                            user.setEmail(email);
                            user.setPassword(password);
                            user.setIntroduce(introduce);
                            user.setNickname(nickname);

                            userList.add(user);
                        }

                        for(int i=0; i<userList.size(); i++){
                            Log.d("",""+userList.get(i).getEmail());
                        }

                        setUserListRecyclerView(userList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),
                    "서버와의 통신에 실패하였습니다.",
                    Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
    private void setUserListRecyclerView(List<User> userList){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adminAdapter = new AdminAdapter(userList, AdminActivity.this, context);
        recyclerView.setAdapter(adminAdapter);
    }
}