package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.ApplyMemberAdapter;
import com.example.groupplanstudy.Server.Adapter.GroupMemberAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.ApplyMemberDto;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.ApplyMemberService;
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

    private TextView tvApplyMemberCount, tvGroupMemberCount;
    private RecyclerView recyclerViewApplyMember, recyclerViewGroupMember;
    private LinearLayout linearLayoutApplyMember;
    private LinearLayoutManager linearLayoutApplyMemberManager, linearLayoutGroupMemberManager;
    private ApplyMemberAdapter applyMemberAdapter;
    private GroupMemberAdapter groupMemberAdapter;

    private Retrofit retrofit;

    private GroupMemberService groupMemberService;
    private ApplyMemberService applyMemberService;

    private ModelMapper modelMapper = new ModelMapper();

    private List<User> userDtos;
    private List<GroupMemberDto> groupMemberDtos;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        mContext= getApplicationContext();

        linearLayoutApplyMember= findViewById(R.id.applymember_linearLayout);
        linearLayoutApplyMember.setVisibility(View.GONE);

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

        Log.d("json: ", text);
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
        applyMemberService= retrofit.create(ApplyMemberService.class);
    }

    private void setApplyMemberAndRecyclerView(List<User> userDtos)
    {
        linearLayoutApplyMember.setVisibility(View.VISIBLE);

        linearLayoutApplyMemberManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewApplyMember.setLayoutManager(linearLayoutApplyMemberManager);

        applyMemberAdapter= new ApplyMemberAdapter(this,userDtos,grId);
        recyclerViewApplyMember.setAdapter(applyMemberAdapter);

        tvApplyMemberCount.setText(applyMemberAdapter.getItemCount()+"");

        adapterOnClick();
    }

    // allow ApplyMember
    private void adapterOnClick()
    {
        applyMemberAdapter.setOnItemClickListener(new ApplyMemberAdapter.OnItemClickListener() {
            @Override
            public void onAllowClick(ApplyMemberDto applyMemberDto, int pos) {
                Call<APIMessage> allowMemberCall = applyMemberService.allowGroupMember(applyMemberDto);
                allowMemberCall.enqueue(new Callback<APIMessage>() {
                    @Override
                    public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                        if(response.isSuccessful())
                        {
                            APIMessage apiMessage= response.body();
                            String message = apiMessage.getMessage();
                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                            // remove Apply Member to list
                            applyMemberAdapter.removeApplyMemberItem(pos);

                            // 후추 서버에서 가져오는거 예정
//                            GroupMemberDto groupMemberDto= null;
//                            groupMemberAdapter.addGroupMemberItem(groupMemberDto);

                            tvApplyMemberCount.setText(applyMemberAdapter.getItemCount()+"");

//                            Intent intent= new Intent(mContext, GroupMemberActivity.class);
//                            intent.putExtra("grId", grId);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            mContext.startActivity(intent);
                        }else{
                            Log.d("result","실패");
                            Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIMessage> call, Throwable t) {
                        Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRefuseClick(ApplyMemberDto applyMemberDto, int pos) {

                AlertDialog.Builder alertDig = new AlertDialog.Builder(GroupMemberActivity.this);
                alertDig.setMessage("삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //삭제하기

                                Call<APIMessage> refuseMemberCall = applyMemberService.refuseGroupMember(applyMemberDto);
                                refuseMemberCall.enqueue(new Callback<APIMessage>() {
                                    @Override
                                    public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                                        if(response.isSuccessful())
                                        {
                                            APIMessage apiMessage= response.body();
                                            String message = apiMessage.getMessage();
                                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                                            applyMemberAdapter.removeApplyMemberItem(pos);
                                            tvApplyMemberCount.setText(applyMemberAdapter.getItemCount()+"");
                                        }else{
                                            Log.d("result","실패");
                                            Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIMessage> call, Throwable t) {
                                        Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }// 승인 거절 서버 통신

                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //취소하기
                                dialogInterface.dismiss();
                            }
                        });
                alertDig.show();


            }
        });
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
                Log.d("result","실패: "+t.getMessage());

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