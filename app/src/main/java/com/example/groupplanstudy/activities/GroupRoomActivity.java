package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.GroupRoomAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.GroupRoomService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupRoomActivity extends AppCompatActivity {
    EditText groupqna_ettitle, groupqna_etcontent;
    TextView grouproom_name, grouproom_introduce;
    private RecyclerView grouproom_recyclerView;

    private Retrofit retrofit;
    private GroupRoomService groupRoomService;

    private List<GroupQnaDto> groupQnaDtos;
    private GroupRoomAdapter groupRoomAdapter;

    private LinearLayoutManager linearLayoutManager;

    private ModelMapper modelMapper = new ModelMapper();
    private String query="";

    private Long grId;

    private User user;

    private GroupRoomDto groupRoomDto;
    private String title, introduce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_room);

        grouproom_recyclerView = findViewById(R.id.grouproom_recyclerView);

        FloatingActionButton grouproom_floatBtn = findViewById(R.id.grouproom_floatBtn);
        groupqna_ettitle = findViewById(R.id.groupqna_ettitle);
        groupqna_etcontent = findViewById(R.id.groupqna_etcontent);

        grouproom_name = findViewById(R.id.grouproom_name);
        grouproom_introduce = findViewById(R.id.grouproom_introduce);


        // 인텐트로 grId 추가
        Intent intent = getIntent();
//        grId = intent.getLongExtra("grId", 0);
        groupRoomDto = (GroupRoomDto) intent.getSerializableExtra("groupRoomDto");
        grId = groupRoomDto.getGrId();

        Log.d("groupRoomDto",groupRoomDto.toString());

        //그룹방 정보 추가
        grouproom_name.setText(groupRoomDto.getTitle());
        grouproom_introduce.setText(groupRoomDto.getIntroduce());


        //보내는 곳
        // SerialObj serialObj = new SerialObj();
        // 객체 생성
        // Intent intent = new Intent(this, NextActivity.class);
        // intent.putExtra("serialObj", serialObj);
        // startActivity(intent);



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



        //툴바
        Toolbar toolbar = findViewById(R.id.grouproom_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initRetrofit();

        getGrouQnasFromServer();


        //QnA 글쓰기
        grouproom_floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.grouproomqna_write, null);

                final EditText groupqna_ettitle = dialogView.findViewById(R.id.groupqna_ettitle);
                final EditText groupqna_etcontent = dialogView.findViewById(R.id.groupqna_etcontent);
                AlertDialog.Builder dlg = new AlertDialog.Builder(GroupRoomActivity.this);
                dlg.setTitle("등록");
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

                        Call<GroupQnaDto> call = groupRoomService.writeQna(groupQnaDto,grId,user.getUid());

                        call.enqueue(new Callback<GroupQnaDto>() {
                            @Override
                            public void onResponse(Call<GroupQnaDto> call, Response<GroupQnaDto> response) {
                                GroupQnaDto groupQnaDto= response.body();
                            }

                            @Override
                            public void onFailure(Call<GroupQnaDto> call, Throwable t) {

                            }
                        });
                    }
                });
                dlg.setNegativeButton("닫기", null);
                dlg.show();
            }
        });



    }
    
    //툴바 뒤로가기 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //툴바 팝업메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_room_top_menu, menu);

        return true;
    }

    //레트로핏
    private void initRetrofit(){
        retrofit = Client.getClient();

        groupRoomService= retrofit.create(GroupRoomService.class);
    }

    //리사이클러뷰
    private void setRecyclerView(List<GroupQnaDto> groupQnaDtos){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        grouproom_recyclerView.setLayoutManager(linearLayoutManager);

        groupRoomAdapter= new GroupRoomAdapter(this,groupQnaDtos);
        grouproom_recyclerView.setAdapter(groupRoomAdapter);
    }

    //Qna 전체보기
    private void getGrouQnasFromServer()
    {
        Call<APIMessage> groupQnaCall= groupRoomService.getGroupQnaByGrId(grId);
        groupQnaCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful()){
                    APIMessage apiMessage = modelMapper.map( response.body(), APIMessage.class);

                    List<Object> objects = (List<Object>) apiMessage.getData();

                    groupQnaDtos = new ArrayList<>();
                    for(int i =0 ; i < objects.size(); i++) {
                        groupQnaDtos.add(modelMapper.map(objects.get(i),GroupQnaDto.class));
                    }

                    // recylerview
                    setRecyclerView(groupQnaDtos);
                }
                else{
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