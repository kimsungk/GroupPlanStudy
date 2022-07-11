package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.ApplyMemberAdapter;
import com.example.groupplanstudy.Server.Adapter.OpenGroupAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.GroupMemberService;
import com.example.groupplanstudy.Server.Service.OpenGroupService;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupMemberActivity extends AppCompatActivity {

    // Group Room Id
    private long grId= 4L;

    private TextView tvApplyMemberCount, tvGroupMemberCount;
    private RecyclerView recyclerViewApplyMember, recyclerViewGroupMember;
    private LinearLayoutManager linearLayoutApplyMemberManager, linearLayoutGroupMemberManager;
    private ApplyMemberAdapter applyMemberAdapter;

    private Retrofit retrofit;

    private GroupMemberService groupMemberService;

    private ModelMapper modelMapper = new ModelMapper();

    private List<User> userDtos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        recyclerViewApplyMember= findViewById(R.id.group_apply_member_recyclerview);
        recyclerViewGroupMember= findViewById(R.id.group_member_recyclerview);
        tvApplyMemberCount= findViewById(R.id.group_apply_member_textview_count);
        tvGroupMemberCount= findViewById(R.id.group_member_textview_count);

        Intent intent = getIntent();
        grId =intent.getLongExtra("grId",4L);

        initRetrofit();

        getGroupApplyMemberFromServer();


    }

    private void initRetrofit()
    {
        retrofit = Client.getClient();

        groupMemberService= retrofit.create(GroupMemberService.class);
    }

    private void setRecyclerView(List<User> userDtos)
    {
        linearLayoutApplyMemberManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewApplyMember.setLayoutManager(linearLayoutApplyMemberManager);

        applyMemberAdapter= new ApplyMemberAdapter(this,userDtos);
        recyclerViewApplyMember.setAdapter(applyMemberAdapter);
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
                    setRecyclerView(userDtos);
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