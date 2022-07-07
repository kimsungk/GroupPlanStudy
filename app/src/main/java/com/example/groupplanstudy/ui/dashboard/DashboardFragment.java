package com.example.groupplanstudy.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupplanstudy.DB.DBHelper;
import com.example.groupplanstudy.R;
import com.example.groupplanstudy.databinding.FragmentDashboardBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public String readDay = null;
    public CalendarView calendarView;
    public Button upd_Btn, del_Btn, save_Btn;
    public TextView diaryTextView;
    public ListView listView;
    public EditText contextEditText;
    public Context context;
    public String uid = null;
    public ScrollView scrollView;

    SQLiteDatabase sqLiteDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = container.getContext();

        DBHelper dbHelper = new DBHelper(context);

        calendarView = root.findViewById(R.id.calendarView);
        diaryTextView = root.findViewById(R.id.diaryTextView);
        listView = root.findViewById(R.id.listView);
        contextEditText = root.findViewById(R.id.contextEditText);
        scrollView = root.findViewById(R.id.scrollView);

        save_Btn = root.findViewById(R.id.save_Btn);
        del_Btn = root.findViewById(R.id.del_Btn);
        upd_Btn = root.findViewById(R.id.upd_Btn);

        //캘린더 선택
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                readDay = year+"-"+(month+1)+"-"+dayOfMonth;
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);

                listView.setVisibility(View.INVISIBLE);
                upd_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);

                diaryTextView.setText(readDay);
                contextEditText.setText("");
                
                //데이터 확인
                checkDay(readDay);

                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //값설정
                        String uid = "1";
                        String date = readDay;
                        String content = contextEditText.getText().toString();

                        dbHelper.insert(uid,date,content);
                        Toast.makeText(getContext(),"저장되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return root;
    }

    //날짜 체크
    public void checkDay(String readDay)
    {

        String getReadDay = readDay;
        ArrayList<String> resultList = null;
        DBHelper dbHelper = new DBHelper(context);


        //날짜 체크비교 데이터 유무 판결

        listView.setVisibility(View.VISIBLE);
        uid = "1";
        //textview2에는 저장되어있는 content 값넣기
        resultList = dbHelper.getResult(getReadDay,uid);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, resultList);

        listView.setAdapter(adapter);

        upd_Btn.setVisibility(View.VISIBLE);
        del_Btn.setVisibility(View.VISIBLE);

        //수정할경우
        upd_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        //삭제할경우
        del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.delete(uid, readDay);
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}