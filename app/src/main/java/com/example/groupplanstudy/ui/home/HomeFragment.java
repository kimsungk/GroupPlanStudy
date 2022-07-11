package com.example.groupplanstudy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.activities.MyPageActivity;
import com.example.groupplanstudy.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static Context context;
    private TextView txMy;
    private String name;
    private User user;
    private Button btnMyPage;
    PieChart pieChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = container.getContext();

        txMy = root.findViewById(R.id.txMy);
        btnMyPage = root.findViewById(R.id.btnMyPage);

        user = new User();

        //JSON 파일 값 추출
        String text = PreferenceManager.getString(context, "user");
        try {
            JSONObject jsonObject = new JSONObject(text);
            user.setUid(jsonObject.getLong("uid"));
            user.setEmail(jsonObject.getString("email"));
            user.setPassword(jsonObject.getString("password"));
            user.setIntroduce(jsonObject.getString("introduce"));
            user.setNickname(jsonObject.getString("nickname"));
            //테스트 -> 유저클래스에 직접담거나, 타입에 맞는 곳에 직접넣어도됨
            name = jsonObject.getString("nickname");
            txMy.setText(user.getNickname()+"님의 학습 통계");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("이름 ",name);

        //원형그래프 차트
        pieChart = root.findViewById(R.id.pieChart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(android.R.color.white);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        //값넣기(테스트) 학습테이블 리스트 넣기
        yValues.add(new PieEntry(34f,"한식"));
        yValues.add(new PieEntry(23f,"중식"));
        yValues.add(new PieEntry(14f,"일식"));
        yValues.add(new PieEntry(35f,"양식"));
        yValues.add(new PieEntry(40f,"동남아"));
        yValues.add(new PieEntry(40f,"기타"));

        //원형그래프 이름
        Description description = new Description();
        description.setText(name+"의 학습 시간"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"과목 종류");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        //pieChart.invalidate(); // 회전 및 터치 효과 사라짐
        //pieChart.setTouchEnabled(false);

        pieChart.setData(data);

        //마이페이지 버튼클릭
        btnMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPageActivity.class);
                startActivity(intent);
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