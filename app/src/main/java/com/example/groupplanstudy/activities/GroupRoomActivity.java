package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.GroupRoomAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.ApplyMemberDto;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.ApplyMemberService;
import com.example.groupplanstudy.Server.Service.GroupMemberService;
import com.example.groupplanstudy.Server.Service.GroupRoomService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupRoomActivity extends AppCompatActivity {

    private Button grouproomButton;
    private TextView grouproomName, grouproomIntroduce;
    private RecyclerView qnaboard_recyclerView;
    private FloatingActionButton grouproom_floatBtn;
    private Toolbar toolbar;

    private Retrofit retrofit;
    private GroupRoomService groupRoomService;
    private ApplyMemberService applyMemberService;
    private GroupMemberService groupMemberService;

    private List<GroupQnaDto> groupQnaDtos;
    private GroupRoomAdapter groupRoomAdapter;

    private LinearLayoutManager linearLayoutManager;

    private ModelMapper modelMapper = new ModelMapper();

    private Long grId;
    private User user;

    private GroupRoomDto groupRoomDto;
    private Map<Long, GroupMemberDto> groupMemberDtoMap = new HashMap<>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_room);

        grouproomButton = findViewById(R.id.grouproom_apply_button);
        qnaboard_recyclerView = findViewById(R.id.qnaboard_recyclerView);
        grouproom_floatBtn = findViewById(R.id.grouproom_floatBtn);
        grouproomName = findViewById(R.id.grouproom_name);
        grouproomIntroduce = findViewById(R.id.grouproom_introduce);

        // 인텐트로 grId 추가
        Intent intent = getIntent();
        groupRoomDto = (GroupRoomDto) intent.getSerializableExtra("groupRoomDto");
        grId = groupRoomDto.getGrId();

        //그룹방 정보 추가
        grouproomName.setText(groupRoomDto.getTitle());
        grouproomIntroduce.setText(groupRoomDto.getIntroduce());

        mContext = getApplicationContext();

        //uid 추가
        String text = PreferenceManager.getString(getApplicationContext(), "user");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(text);
            user = new User();
            user.setUid(jsonObject.getLong("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        toolbar = findViewById(R.id.grouproom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRetrofit();

        getGroupMember();

        clickApplyButton();
        writeQnaBoard();


        getGrouQnasFromServer();
        onItemClickListeners();

    }

    //레트로핏
    private void initRetrofit() {
        retrofit = Client.getClient();

        groupRoomService = retrofit.create(GroupRoomService.class);
        applyMemberService = retrofit.create(ApplyMemberService.class);
        groupMemberService = retrofit.create(GroupMemberService.class);
    }

    private void getGroupMember() {
        Call<APIMessage> getGroupMemberCall = groupMemberService.getGroupMember(grId);
        getGroupMemberCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonString = gson.toJson(response.body().getData());

                Type memberListType = new TypeToken<ArrayList<GroupMemberDto>>() {
                }.getType();
                ArrayList<GroupMemberDto> userArray = gson.fromJson(jsonString, memberListType);


                for (GroupMemberDto gmd : userArray) {
                    groupMemberDtoMap.put(gmd.getUid(), gmd);
                }

            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isGroupMember() {
        if (groupMemberDtoMap.containsKey(user.getUid())) return true;
        else return false;
    }

    private void applyGroupRoom() {
        AlertDialog.Builder alertDig = new AlertDialog.Builder(GroupRoomActivity.this);
        alertDig.setMessage("스터디 그룹에 가입하시겠습니까?").setPositiveButton("가입", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<APIMessage> applyGroupRoomCall = applyMemberService.applyGroupRoom(new ApplyMemberDto(grId, user.getUid()));
                        applyGroupRoomCall.enqueue(new Callback<APIMessage>() {
                            @Override
                            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                                if (response.isSuccessful()) {
                                    APIMessage apiMessage = response.body();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupRoomActivity.this);

                                    builder.setTitle("알림").setMessage(apiMessage.getMessage());

                                    AlertDialog alertDialog = builder.create();

                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIMessage> call, Throwable t) {
                                Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

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

    private void clickApplyButton() {
        grouproomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyGroupRoom();
            }
        });
    }

    private void writeQnaBoard() {
        //QnA 글쓰기
        grouproom_floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGroupMember()) {
                    applyGroupRoom();
                    return;
                }

                View dialogView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.grouproomqna_write, null);

                final EditText groupqna_ettitle = dialogView.findViewById(R.id.groupqna_ettitle);
                final EditText groupqna_etcontent = dialogView.findViewById(R.id.groupqna_etcontent);
                AlertDialog.Builder dlg = new AlertDialog.Builder(GroupRoomActivity.this);
                dlg.setTitle("질문등록");
                dlg.setView(dialogView);
                dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GroupRoomService groupRoomService
                                = Client.getClient().create(GroupRoomService.class);

                        final EditText groupqna_ettitle = dialogView.findViewById(R.id.groupqna_ettitle);
                        final EditText groupqna_etcontent = dialogView.findViewById(R.id.groupqna_etcontent);

                        GroupQnaDto groupQnaDto = new GroupQnaDto();
                        groupQnaDto.setTitle(groupqna_ettitle.getText().toString());
                        groupQnaDto.setContent(groupqna_etcontent.getText().toString());

                        Call<GroupQnaDto> call = groupRoomService.writeQna(groupQnaDto, grId, user.getUid());

                        call.enqueue(new Callback<GroupQnaDto>() {
                            @Override
                            public void onResponse(Call<GroupQnaDto> call, Response<GroupQnaDto> response) {
                                GroupQnaDto groupQnaDto = response.body();

                                Intent intent = new Intent(getApplicationContext(), GroupRoomActivity.class);
                                intent.putExtra("groupRoomDto", groupRoomDto);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<GroupQnaDto> call, Throwable t) {
                                Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dlg.setNegativeButton("닫기", null);
                dlg.show();
            }
        });
    }


    private void onItemClickListeners() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.grouproom_top_menu_member:
                        Intent intent = new Intent(getApplicationContext(), GroupMemberActivity.class);
                        intent.putExtra("groupRoomDto", groupRoomDto);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });
    }


    //리사이클러뷰
    private void setRecyclerView(List<GroupQnaDto> groupQnaDtos) {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        qnaboard_recyclerView.setLayoutManager(linearLayoutManager);

        groupRoomAdapter = new GroupRoomAdapter(this, groupQnaDtos, user.getUid(), groupMemberDtoMap);
        qnaboard_recyclerView.setAdapter(groupRoomAdapter);

        qnaboard_recyclerView.scrollToPosition(groupRoomAdapter.getItemCount() - 1);
    }

    //Qna 전체보기
    private void getGrouQnasFromServer() {
        Call<APIMessage> groupQnaCall = groupRoomService.getGroupQnaByGrId(grId);
        groupQnaCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if (response.isSuccessful()) {
                    APIMessage apiMessage = modelMapper.map(response.body(), APIMessage.class);

                    List<Object> objects = (List<Object>) apiMessage.getData();

                    groupQnaDtos = new ArrayList<>();
                    for (int i = 0; i < objects.size(); i++) {
                        groupQnaDtos.add(modelMapper.map(objects.get(i), GroupQnaDto.class));
                    }

                    // recylerview
                    setRecyclerView(groupQnaDtos);
                } else {
                    Log.d("result", "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Log.d("resultag", "실패: " + t.getMessage());

            }
        });
    }

    //툴바 뒤로가기 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //툴바 팝업메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_room_top_menu, menu);
        return true;
    }
}