package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.ApplyMemberAdapter;
import com.example.groupplanstudy.Server.Adapter.GroupMemberAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.GroupMemberService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupMemberActivity extends AppCompatActivity {

    // Group Room Id
    private long grId;

    private View viewApplyMember;
    private TextView tvApplyMemberTitle;
    private TextView tvApplyMemberCount, tvGroupMemberCount;
    private RecyclerView recyclerViewApplyMember, recyclerViewGroupMember;
    private LinearLayout linearLayoutApplyMember;
    private LinearLayoutManager linearLayoutApplyMemberManager, linearLayoutGroupMemberManager;
    private ApplyMemberAdapter applyMemberAdapter;
    private GroupMemberAdapter groupMemberAdapter;

    private Retrofit retrofit;

    private GroupMemberService groupMemberService;

    private ModelMapper modelMapper = new ModelMapper();

    private List<User> userDtos;
    private List<GroupMemberDto> groupMemberDtos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        linearLayoutApplyMember= findViewById(R.id.applymember_linearLayout);
        linearLayoutApplyMember.setVisibility(View.GONE);

        viewApplyMember= findViewById(R.id.applymember_view);

        tvApplyMemberTitle= findViewById(R.id.group_apply_member_textview_title);

        recyclerViewApplyMember= findViewById(R.id.group_apply_member_recyclerview);
        recyclerViewGroupMember= findViewById(R.id.group_member_recyclerview);

        tvApplyMemberCount= findViewById(R.id.group_apply_member_textview_count);
        tvGroupMemberCount= findViewById(R.id.group_member_textview_count);

        Intent intent = getIntent();
        grId =intent.getLongExtra("grId",0);

        initRetrofit();

        // 리더일때 실행 ( 추후 )
        if(isReader()) getGroupApplyMemberFromServer();
        getGroupMemberFromServer();

    }
    private boolean isReader()
    {
        boolean result=true;
        Gson gson = new Gson();
        String text= PreferenceManager.getString(getApplicationContext(), "user");

        Log.d("json: ", text.toString());
        User user =new User();
//        Log.d("userdto: ", userdto.getUid()+"");
        try {
            JSONObject jsonObject = new JSONObject(text);
            user.setUid(jsonObject.getLong("uid"));
            user.setEmail(jsonObject.getString("email"));
            user.setPassword(jsonObject.getString("password"));
            user.setIntroduce(jsonObject.getString("introduce"));
            user.setNickname(jsonObject.getString("nickname"));
            //테스트 -> 유저클래스에 직접담거나, 타입에 맞는 곳에 직접넣어도됨
            String name = jsonObject.getString("nickname");
            Log.d("test: ","start");

            Log.d("test: ",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void initRetrofit()
    {
        retrofit = Client.getClient();

        groupMemberService= retrofit.create(GroupMemberService.class);
    }

    private void setApplyMemberAndRecyclerView(List<User> userDtos)
    {
        linearLayoutApplyMember.setVisibility(View.VISIBLE);

        linearLayoutApplyMemberManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewApplyMember.setLayoutManager(linearLayoutApplyMemberManager);

        applyMemberAdapter= new ApplyMemberAdapter(this,userDtos);
        recyclerViewApplyMember.setAdapter(applyMemberAdapter);

        tvApplyMemberCount.setText(applyMemberAdapter.getItemCount()+"");
    }

    private void setGroupMemberRecyclerView(List<GroupMemberDto> groupMemberDtos)
    {
        linearLayoutGroupMemberManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewGroupMember.setLayoutManager(linearLayoutGroupMemberManager);

        groupMemberAdapter= new GroupMemberAdapter(this,groupMemberDtos);
        recyclerViewGroupMember.setAdapter(groupMemberAdapter);

        tvGroupMemberCount.setText(groupMemberAdapter.getItemCount()+"");
    }

    private void getGroupApplyMemberFromServer()
    {
        Call<APIMessage> applyMemberCall= groupMemberService.getApplyMember(grId);
        applyMemberCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful()){
                    APIMessage apiMessage= response.body();

                    List<Object> objects = (List<Object>) apiMessage.getData();

                    userDtos = new ArrayList<>();
                    for(int i =0 ; i < objects.size(); i++) {
                        userDtos.add(modelMapper.map(objects.get(i), User.class));
                    }
                    setApplyMemberAndRecyclerView(userDtos);
                } else{
                    Log.d("result","onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Log.d("resultag","실패: "+t.getMessage());

            }
        });
    }

    private void getGroupMemberFromServer()
    {
        Call<APIMessage> applyMemberCall= groupMemberService.getGroupMember(grId);
        applyMemberCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful()){
                    APIMessage apiMessage= response.body();

                    List<Object> objects = (List<Object>) apiMessage.getData();

                    groupMemberDtos = new ArrayList<>();
                    for(int i =0 ; i < objects.size(); i++) {
                        groupMemberDtos.add(modelMapper.map(objects.get(i), GroupMemberDto.class));
                    }
                    setGroupMemberRecyclerView(groupMemberDtos);
                } else{
                    Log.d("result","onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Log.d("resultag","실패: "+t.getMessage());

            }
        });
    }

}