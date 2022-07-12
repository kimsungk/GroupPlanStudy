package com.example.groupplanstudy.ui.notifications;

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

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.Time;
import com.example.groupplanstudy.databinding.FragmentNotificationsBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Button btnStuStart,btnStudySave,btnStudyCancel;
    private TextView tvStartTime,tvEndTime;
    private EditText editStudy,editStudyContent;
    long now;
    Date date;
    String getTimeStart,getTimeEnd;
    Time time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnStuStart = root.findViewById(R.id.btnStuStart);
        btnStudySave = root.findViewById(R.id.btnStudySave);
        btnStudyCancel = root.findViewById(R.id.btnStudyCancel);

        tvStartTime = root.findViewById(R.id.tvStartTime);
        tvEndTime = root.findViewById(R.id.tvEndTime);

        editStudy = root.findViewById(R.id.editStudy);
        editStudyContent = root.findViewById(R.id.editStudyContent);

        btnStuStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editStudy.getText().toString().equals("")){
                    Toast.makeText(getContext(),"학습과목을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editStudyContent.getText().toString().equals("")){
                    Toast.makeText(getContext(),"학습내용을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (btnStuStart.getText().equals("시작하기")) {
                    now = System.currentTimeMillis();
                    date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    getTimeStart = sdf.format(date);

                    tvStartTime.setText("      시작시간 : " + getTimeStart);
                    tvEndTime.setText("      종료시간 : ");
                    btnStuStart.setText("기록하기");

//                    time.setStartTime(tvStartTime.getText().toString());
//                    time.setEndTime(tvEndTime.getText().toString());
//
//                    PreferenceManager.setString(getContext(),"date", time.toString());

                } else if (btnStuStart.getText().equals("기록하기")) {

                    now = System.currentTimeMillis();
                    date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    getTimeEnd = sdf.format(date);

                    tvEndTime.setText("      종료시간 : " + getTimeEnd);

                    btnStuStart.setText("시작하기");

//                    time.setStartTime(tvStartTime.getText().toString());
//                    time.setEndTime(tvEndTime.getText().toString());
//
//                    PreferenceManager.setString(getContext(),"date", time.toString());
                }
            }
        });

        btnStudySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnStudyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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