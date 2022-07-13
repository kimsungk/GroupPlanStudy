package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.QnaBoardAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.QnaBoardCommentDto;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.ApplyMemberService;
import com.example.groupplanstudy.Server.Service.GroupMemberService;
import com.example.groupplanstudy.Server.Service.QnaBoardCommentService;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QnaBoardActivity extends AppCompatActivity {

    private GroupQnaDto groupQnaDto;
    private QnaBoardAdapter qnaBoardAdapter;
    private Toolbar toolbar;
    private TextView tvNickname, tvContent;
    private RecyclerView recyclerViewComment;
    private LinearLayoutManager linearLayoutCommentManager;

    private Retrofit retrofit;
    private QnaBoardCommentService qnaBoardCommentService;
    private ModelMapper modelMapper = new ModelMapper();

    private List<QnaBoardCommentDto> qnaBoardCommentDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_board);

        tvNickname = findViewById(R.id.qnaboard_tv_nickname);
        tvContent = findViewById(R.id.qnaboard_tv_content);
        recyclerViewComment = findViewById(R.id.qnaboard_comments_recyclerview);

        Intent intent = getIntent();
        groupQnaDto = (GroupQnaDto) intent.getSerializableExtra("groupQnaDto");
        tvNickname.setText(groupQnaDto.getUser().getNickname());
        tvContent.setText(groupQnaDto.getContent());

        toolbar = findViewById(R.id.qnaboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initRetrofit();
        getQnaBoardCommentsFromServer();
    }

    private void initRetrofit()
    {
        retrofit = Client.getClient();

        qnaBoardCommentService = retrofit.create(QnaBoardCommentService.class);
    }

    private void getQnaBoardCommentsFromServer()
    {
        Call<APIMessage> qnaBoardCommentsCall = qnaBoardCommentService.getAllQnaCommentList(groupQnaDto.getGroupRoomDto().getGrId(), groupQnaDto.getBid());
        qnaBoardCommentsCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response)
            {
                if(response.isSuccessful())
                {
                    APIMessage apiMessage = response.body();

                    List<Object> objects = (List<Object>) apiMessage.getData();

                    qnaBoardCommentDtoList = new ArrayList<>();
                    for(int i =0 ; i < objects.size(); i++) {
                        qnaBoardCommentDtoList.add(modelMapper.map(objects.get(i), QnaBoardCommentDto.class));
                    }

                }
                setQnaBoardCommentRecyclerView(qnaBoardCommentDtoList);

            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Log.d("result","실패: "+t.getMessage());
            }
        });
    }

    private void setQnaBoardCommentRecyclerView(List<QnaBoardCommentDto> qnaBoardCommentDtoList)
    {
        linearLayoutCommentManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewComment.setLayoutManager(linearLayoutCommentManager);

        qnaBoardAdapter= new QnaBoardAdapter(qnaBoardCommentDtoList);
        recyclerViewComment.setAdapter(qnaBoardAdapter);

        recyclerViewComment.scrollToPosition(qnaBoardAdapter.getItemCount()-1);

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
        inflater.inflate(R.menu.qnaboard_top_toolbar, menu);
        return true;
    }
}