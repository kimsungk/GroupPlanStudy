package com.example.groupplanstudy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.Home;
import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.MyGroupRoomAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.Applicable;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto2;
import com.example.groupplanstudy.Server.DTO.GroupRole;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.GroupMemberService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;

import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {

    private static Context context;
    TextView tvNickname,tvEmail,tvIntroduce;
    private String nickname, email, introduce;
    Button btnUpdateUser, btnBack;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private MyGroupRoomAdapter myGroupRoomAdapter;

    long uid;
    List<GroupMemberDto2> groupMemberDto2List;


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

        recyclerView = findViewById(R.id.listGroupRoom);

        String text = PreferenceManager.getString(context, "user");

        try {
            JSONObject jsonObject = new JSONObject(text);
            uid = jsonObject.getLong("uid");
            nickname = jsonObject.getString("nickname");
            email = jsonObject.getString("email");
            introduce = jsonObject.getString("introduce");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvIntroduce.setText(introduce);
        tvEmail.setText(email);
        tvNickname.setText(nickname);

        //참여 중인 그룹방

        GroupMemberService groupMemberService
                = Client.getClient().create(GroupMemberService.class);

        Call<APIMessage> call = groupMemberService.getMyGroup(uid);

        call.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful()){

                    APIMessage apiMessage = response.body();

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    PreferenceManager.setString(context, "mygroup", gson.toJson(response.body().getData()));

                    String myGroup = PreferenceManager.getString(context, "mygroup");

                    try {
                        JSONArray jsonArray = new JSONArray(myGroup);
                        Log.d("jsonarray",""+jsonArray.length());
                        groupMemberDto2List = new ArrayList<>();
                        for(int i=0; i<jsonArray.length(); i++){
                            //subJsonObject가 하나의 member를 가진다
                            JSONObject subJsonObject = jsonArray.getJSONObject(i);
                            //groupRoomDto를 제외한 값 가져오기
                            long gmId = subJsonObject.getLong("gmId");
                            long uid = subJsonObject.getLong("uid");
                            String role = subJsonObject.getString("role");

                            String groupRoomDto = subJsonObject.getString("groupRoomDto");
                            //groupRoomDto 의 값들 가져오기
                            JSONObject subJsonObject2 = new JSONObject(groupRoomDto);
                            long grId = subJsonObject2.getLong("grId");
                            String title = subJsonObject2.getString("title");
                            String introduce = subJsonObject2.getString("introduce");
                            String applicable = subJsonObject2.getString("applicable");
                            int memberLimit = subJsonObject2.getInt("memberLimit");

                            //GroupRoomDto에 값넣기
                            GroupRoomDto groupRoom = new GroupRoomDto();
                            groupRoom.setGrId(grId);
                            groupRoom.setTitle(title);
                            groupRoom.setIntroduce(introduce);

                            if(applicable.equals("OPEN")){
                                groupRoom.setApplicable(Applicable.OPEN);
                            }else if(applicable.equals("CLOSED")){
                                groupRoom.setApplicable(Applicable.CLOSED);
                            }
                            groupRoom.setMemberLimit(memberLimit);

                            //GroupMemberDto2에 값넣기
                            GroupMemberDto2 groupMember = new GroupMemberDto2();
                            groupMember.setGmId(gmId);
                            groupMember.setUid(uid);
                            if(role.equals("LEADER")){
                                groupMember.setRole(GroupRole.LEADER);
                            }else if(role.equals("MEMBER")){
                                groupMember.setRole(GroupRole.MEMBER);
                            }

                            groupMember.setGroupRoomDto(groupRoom);
                            groupMemberDto2List.add(groupMember);

                        }


                        for (int i=0; i<groupMemberDto2List.size(); i++){
                            Log.d(" "," "+groupMemberDto2List.get(i).getGroupRoomDto().getTitle());
                        }
                        //recycler view 넣기
                        setMygroupRecyclerView(groupMemberDto2List);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("result","onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Log.d("에러",t.getMessage());
            }
        });

//       Log.d(" "," "+groupMemberDto2List.get(0));

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
    private void setMygroupRecyclerView(List<GroupMemberDto2> MyGroupList){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        myGroupRoomAdapter = new MyGroupRoomAdapter(MyGroupList, MyPageActivity.this, context);
        recyclerView.setAdapter(myGroupRoomAdapter);
    }
}