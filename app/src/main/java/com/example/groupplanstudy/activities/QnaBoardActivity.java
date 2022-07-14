package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.QnaBoardAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.QnaBoardCommentDto;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.ApplyMemberService;
import com.example.groupplanstudy.Server.Service.GroupMemberService;
import com.example.groupplanstudy.Server.Service.QnaBoardCommentService;

import org.json.JSONException;
import org.json.JSONObject;
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
    private EditText etMessage;
    private ImageButton imageButtonSend;

    private Retrofit retrofit;
    private QnaBoardCommentService qnaBoardCommentService;
    private ModelMapper modelMapper = new ModelMapper();

    private List<QnaBoardCommentDto> qnaBoardCommentDtoList;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_board);

        tvNickname = findViewById(R.id.qnaboard_tv_nickname);
        tvContent = findViewById(R.id.qnaboard_tv_content);
        recyclerViewComment = findViewById(R.id.qnaboard_comments_recyclerview);
        etMessage = findViewById(R.id.qnaboard_comments_et_message);
        imageButtonSend = findViewById(R.id.qnaboard_comments_imageButton_send);

        Intent intent = getIntent();
        groupQnaDto = (GroupQnaDto) intent.getSerializableExtra("groupQnaDto");
        tvNickname.setText(groupQnaDto.getUser().getNickname());
        tvContent.setText(groupQnaDto.getContent());

        toolbar = findViewById(R.id.qnaboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = QnaBoardActivity.this;

        initRetrofit();
        getQnaBoardCommentsFromServer();
        sendCommentFromServer();
    }

    private void initRetrofit()
    {
        retrofit = Client.getClient();

        qnaBoardCommentService = retrofit.create(QnaBoardCommentService.class);
    }

    private long getUid()
    {
        long loginUserId=0;

        String text= PreferenceManager.getString(getApplicationContext(), "user");

        Log.d("text: ",text);
        int a= 0;
        String val="";
        try
        {
            JSONObject userJsonObject = new JSONObject(text);

            val =userJsonObject.getString("uid");

            a= Integer.valueOf(val);

        } catch (JSONException |NumberFormatException e) {
            e.printStackTrace();
            loginUserId= (long)Double.parseDouble(val);
        }
        return loginUserId;
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

        qnaBoardAdapter= new QnaBoardAdapter(qnaBoardCommentDtoList, QnaBoardActivity.this, groupQnaDto.getGroupRoomDto().getGrId(),groupQnaDto.getBid());
        recyclerViewComment.setAdapter(qnaBoardAdapter);

        recyclerViewComment.scrollToPosition(qnaBoardAdapter.getItemCount()-1);

    }

    private void sendCommentFromServer()
    {
        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QnaBoardCommentDto qnaDto = new QnaBoardCommentDto();
                qnaDto.setContent(etMessage.getText().toString());

                Call<QnaBoardCommentDto> sendCommentCall = qnaBoardCommentService.createQnaComment(
                        groupQnaDto.getGroupRoomDto().getGrId(), groupQnaDto.getBid(),getUid(),
                        qnaDto);
                sendCommentCall.enqueue(new Callback<QnaBoardCommentDto>() {
                    @Override
                    public void onResponse(Call<QnaBoardCommentDto> call, Response<QnaBoardCommentDto> response) {
                        if(response.isSuccessful())
                        {
                            QnaBoardCommentDto qnaBoardCommentDto = response.body();
                            qnaBoardAdapter.addComment(qnaBoardCommentDto);
                            etMessage.setText("");
                            recyclerViewComment.scrollToPosition(qnaBoardAdapter.getItemCount()-1);

                        }
                    }

                    @Override
                    public void onFailure(Call<QnaBoardCommentDto> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크 에러", Toast.LENGTH_SHORT).show();
                    }
                });
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
        inflater.inflate(R.menu.qnaboard_top_toolbar, menu);
        return true;
    }
}