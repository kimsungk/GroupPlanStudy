package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.Applicable;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.OpenGroupService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenGroupMakeActivity extends AppCompatActivity {
    private Spinner maxMemberSpinner;
    private ArrayAdapter maxMemberAdapter;
    private String maxMemberSpinnerResult;

    private RadioGroup groupRoomRadioHermeticity;
    private RadioButton groupRoomRadioHermeticityOpen, groupRoomRadioHermeticityClose;
    private String groupRoomRadioHermeticityResult;

    private Button groupRoomMakeBtn, groupRoomCancleBtn;

    private EditText groupRoomIntroduce, groupRoomTitle;

    private GroupRoomDto groupRoomDto;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_group_make_activity);


        //Uid 받아오기
        String text = PreferenceManager.getString(getApplicationContext(), "user");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(text);
            user = new User();
            user.setUid(jsonObject.getLong("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 최대인원
        maxMemberSpinner = findViewById(R.id.grouproommaxspinner);
        maxMemberAdapter = ArrayAdapter.createFromResource(this, R.array.max_member, android.R.layout.simple_spinner_item);
        maxMemberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxMemberSpinner.setAdapter(maxMemberAdapter);

        maxMemberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //선택
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                maxMemberSpinnerResult = adapterView.getItemAtPosition(position).toString();
            }

            //미선택
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(OpenGroupMakeActivity.this, "최대 인원을 설정해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        //공개여부
        groupRoomRadioHermeticity = findViewById(R.id.grouproomradiohermeticity);
        groupRoomRadioHermeticityOpen = findViewById(R.id.grouproomradiohermeticityopen);
        groupRoomRadioHermeticityClose = findViewById(R.id.grouproomradiohermeticityclose);

        groupRoomRadioHermeticity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.grouproomradiohermeticityopen) {
                    groupRoomRadioHermeticityResult = "OPEN";
                    Log.d(" groupRoomRadioHermet", groupRoomRadioHermeticityOpen.toString());
                } else if (i == R.id.grouproomradiohermeticityclose) {
                    groupRoomRadioHermeticityResult = "CLOSED";
                }

            }
        });


        //방만들기
        groupRoomMakeBtn = findViewById(R.id.grouproommakebtn);

        groupRoomIntroduce = findViewById(R.id.grouproomintroduce);
        groupRoomTitle = findViewById(R.id.grouproomtitle);

        groupRoomMakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (groupRoomTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "방 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (groupRoomRadioHermeticityResult == null) {
                    Toast.makeText(getApplicationContext(), "공개 여부를 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (groupRoomIntroduce.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "소개글을 작성해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String title = groupRoomTitle.getText().toString();
                final String introduce = groupRoomIntroduce.getText().toString();
                final String applicable = groupRoomRadioHermeticityResult;
                final String memberLimit = maxMemberSpinnerResult;

                groupRoomDto = new GroupRoomDto();

                groupRoomDto.setTitle(title);
                groupRoomDto.setIntroduce(introduce);

                if (applicable.equals("OPEN")) {
                    groupRoomDto.setApplicable(Applicable.OPEN);
                } else if (applicable.equals("CLOSED")) {
                    groupRoomDto.setApplicable(Applicable.CLOSED);
                }

                groupRoomDto.setMemberLimit(Integer.parseInt(memberLimit));

                OpenGroupService openGroupService = Client.getClient().create(OpenGroupService.class);

                Call<GroupRoomDto> call = openGroupService.createGroupRoom(groupRoomDto, user.getUid());

                call.enqueue(new Callback<GroupRoomDto>() {
                    @Override
                    public void onResponse(Call<GroupRoomDto> call, Response<GroupRoomDto> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), GroupRoomActivity.class);
                            intent.putExtra("groupRoomDto", response.body());
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "그룹방 생성 완료", Toast.LENGTH_SHORT).show();

                            groupRoomTitle.setText("");
                            groupRoomIntroduce.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<GroupRoomDto> call, Throwable t) {
                        Log.d("message : ", t.getMessage());
                    }
                });


            }
        });


        //취소
        groupRoomCancleBtn = findViewById(R.id.grouproomcanclebtn);

        groupRoomCancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}