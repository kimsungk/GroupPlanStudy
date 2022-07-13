package com.example.groupplanstudy.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupplanstudy.DB.MyStudyDB;
import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.databinding.FragmentNotificationsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Button btnStuStart,btnStudySave,btnStudyCancel;
    private TextView tvStartTime,tvEndTime;
    private EditText editStudy,editStudyContent;
    private Context context;
    long now;
    Date date;
    String getTimeStart,getTimeEnd;

    long userid; // 현재로그인한 아이디

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = container.getContext();
        MyStudyDB myStudyDB = new MyStudyDB(context);

        btnStuStart = root.findViewById(R.id.btnStuStart);
        btnStudySave = root.findViewById(R.id.btnStudySave);
        btnStudyCancel = root.findViewById(R.id.btnStudyCancel);

        tvStartTime = root.findViewById(R.id.tvStartTime);
        tvEndTime = root.findViewById(R.id.tvEndTime);

        editStudy = root.findViewById(R.id.editStudy);
        editStudyContent = root.findViewById(R.id.editStudyContent);

        //현재접속한 uid를 userid에 입력
        String text = PreferenceManager.getString(context, "user");
        try {
            JSONObject jsonObject = new JSONObject(text);
            userid = jsonObject.getLong("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //타이머로 저장했던 유저id
        long userCheck = PreferenceManager.getLong(context,"userid");
        
        //이전에 로그인타이머저장했던 userCheck와 현재로그인한 아이디 userid 비교후 같으면 전에 있던 학습타이머 불러오기
        if(userid == userCheck){
            tvStartTime.setText(PreferenceManager.getString(context,"startTime"));
            tvEndTime.setText(PreferenceManager.getString(context,"endTime"));
            //*****EditText를 ""로 바꾼후 EditText 재입력안됨 -> 저장,초기화의 경우에만 EditText 사라짐
            //*****수정여부 확인
            editStudy.setText(PreferenceManager.getString(context,"editStudy"));
            editStudyContent.setText(PreferenceManager.getString(context,"editStudyContent"));
            //***********
            btnStuStart.setText("기록하기");
            Log.d("통과지점","userCheck true");
        }else{  //아니면 초기화
            tvStartTime.setText("");
            tvEndTime.setText("");
            editStudy.setText("");
            editStudyContent.setText("");
            btnStuStart.setText("시작하기");
            Log.d("통과지점","userCheck fails");
        }//시작시간이 저장되어있다면 시작하기 저장되어있지않다면 기록하기

        Log.d("시작시간",tvStartTime.getText().toString());

        btnStuStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //학습과목 학습내용 입력하기
                if(editStudy.getText().toString().equals("")){
                    Toast.makeText(getContext(),"학습과목을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editStudyContent.getText().toString().equals("")){
                    Toast.makeText(getContext(),"학습내용을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                
                //버튼이 시작하기 일경우
                if (btnStuStart.getText().equals("시작하기")) {
                    
                    //현재시간을 가져와 초기화하여 넣기
                    now = System.currentTimeMillis();
                    date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    getTimeStart = sdf.format(date);

                    //초기화한 현재시간을 시작시간에 입력
                    tvStartTime.setText(getTimeStart);
                    tvEndTime.setText("");
                    btnStuStart.setText("기록하기");

                    String text = PreferenceManager.getString(context, "user");

                    //현재 접속한 아이디와 시작시간,종료시간,학습과목,학습내용 내부저장
                    PreferenceManager.setLong(getContext(),"userid", userid);
                    PreferenceManager.setString(getContext(),"startTime", getTimeStart);
                    PreferenceManager.setString(getContext(),"endTime", getTimeEnd);
                    PreferenceManager.setString(getContext(), "editStudy", editStudy.getText().toString());
                    PreferenceManager.setString(getContext(), "editStudyContent", editStudyContent.getText().toString());
                    
                } else if (btnStuStart.getText().equals("기록하기")) { // 버튼이 기록하기 일경우
                    
                    //현재시간을 가져와 초기화하여 넣기
                    now = System.currentTimeMillis();
                    date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    getTimeEnd = sdf.format(date);
                    
                    //초기화한 시간을 종료시간에 입력
                    tvEndTime.setText(getTimeEnd);
                    
                    PreferenceManager.setString(getContext(),"startTime", getTimeStart);
                    PreferenceManager.setString(getContext(),"endTime", getTimeEnd);
                    PreferenceManager.setString(getContext(), "editStudy", editStudy.getText().toString());
                    PreferenceManager.setString(getContext(), "editStudyContent", editStudyContent.getText().toString());
                }
            }
        });

        btnStudySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvStartTime.getText().toString().equals("")){
                    Toast.makeText(getContext(),"시작시간을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tvEndTime.getText().toString().equals("")){
                    Toast.makeText(getContext(),"종료시간을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editStudy.getText().toString().equals("")){
                    Toast.makeText(getContext(),"학습과목을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editStudyContent.getText().toString().equals("")){
                    Toast.makeText(getContext(),"학습내용을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                //DB에 저장하기 -> 저장하고나서 내부저장 데이터 삭제하기

                String start = tvStartTime.getText().toString();
                String end = tvEndTime.getText().toString();

                try {
                    Date format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
                    Date format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);

                    long diffSec = (format2.getTime() - format1.getTime())/1000; //초 차이 계산

                    Log.d("초차이",String.valueOf(diffSec));

                    //데이터 삽입
                    myStudyDB.insertTime(userid, diffSec,
                            editStudy.getText().toString(),
                            editStudyContent.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "저장되었습니다.",Toast.LENGTH_SHORT).show();

                //내부저장 데이터 삭제
                PreferenceManager.removeKey(context, "startTime");
                PreferenceManager.removeKey(context, "endTime");
                PreferenceManager.removeKey(context, "editStudy");
                PreferenceManager.removeKey(context, "editStudyContent");
                PreferenceManager.removeKey(context, "userid");

                //저장했을경우 버튼 시작하기로 변경
                btnStuStart.setText("시작하기");
                editStudy.setText("");
                editStudyContent.setText("");
                tvStartTime.setText("");
                tvEndTime.setText("");
            }
        });

        //초기화
        btnStudyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //시작시간 종료시간 학습과목 학습내용 버튼 초기화
                editStudy.setText("");
                editStudyContent.setText("");
                tvStartTime.setText("");
                tvEndTime.setText("");
                btnStuStart.setText("시작하기");

                PreferenceManager.setString(getContext(),"startTime", tvStartTime.getText().toString());
                PreferenceManager.setString(getContext(),"endTime", tvEndTime.getText().toString());
                PreferenceManager.setString(getContext(), "editStudy", editStudy.getText().toString());
                PreferenceManager.setString(getContext(), "editStudyContent", editStudyContent.getText().toString());

                //내부 저장 데이터 삭제하기
                PreferenceManager.removeKey(context, "startTime");
                PreferenceManager.removeKey(context, "endTime");
                PreferenceManager.removeKey(context, "editStudy");
                PreferenceManager.removeKey(context, "editStudyContent");
                PreferenceManager.removeKey(context, "userid");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}