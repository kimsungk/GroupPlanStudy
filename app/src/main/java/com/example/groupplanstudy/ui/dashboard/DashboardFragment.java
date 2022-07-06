package com.example.groupplanstudy.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.databinding.FragmentDashboardBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    String fileName = null;
    String diaryDate=null;

    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button upd_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2;
    public EditText contextEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = root.findViewById(R.id.calendarView);
        diaryTextView = root.findViewById(R.id.diaryTextView);
        textView2 = root.findViewById(R.id.textView2);
        contextEditText = root.findViewById(R.id.contextEditText);

        save_Btn = root.findViewById(R.id.save_Btn);
        del_Btn = root.findViewById(R.id.del_Btn);
        upd_Btn = root.findViewById(R.id.upd_Btn);

        //처음 실행한 날짜
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        int cMonth = Calendar.getInstance().get(Calendar.MONTH);
        int cDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //초기화
        diaryDate = cYear + "_" + (cMonth + 1) + "_" + cDay;

        //일기 select하기


        //캘린더 선택
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                upd_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                contextEditText.setText("");
//                checkDay(year, month, dayOfMonth);
            }
        });


        //파일저장
//        save_Btn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                saveDiary(readDay);
//                str = contextEditText.getText().toString();
//                textView2.setText(str);
//                save_Btn.setVisibility(View.INVISIBLE);
//                upd_Btn.setVisibility(View.VISIBLE);
//                del_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.INVISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//
//            }
//        });
        return root;
    }

    //날짜 체크
//    public void checkDay(int cYear, int cMonth, int cDay)
//    {
//        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
//        FileInputStream fis;
//
//        try
//        {
//            fis = getActivity().openFileInput(readDay);
//
//            byte[] fileData = new byte[fis.available()];
//            fis.read(fileData);
//            fis.close();
//
//            str = new String(fileData);
//
//            contextEditText.setVisibility(View.INVISIBLE);
//            textView2.setVisibility(View.VISIBLE);
//            textView2.setText(str);
//
//            save_Btn.setVisibility(View.INVISIBLE);
//            upd_Btn.setVisibility(View.VISIBLE);
//            del_Btn.setVisibility(View.VISIBLE);
//
//            //수정
//            upd_Btn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    contextEditText.setVisibility(View.VISIBLE);
//                    textView2.setVisibility(View.INVISIBLE);
//                    contextEditText.setText(str);
//
//                    save_Btn.setVisibility(View.VISIBLE);
//                    upd_Btn.setVisibility(View.INVISIBLE);
//                    del_Btn.setVisibility(View.INVISIBLE);
//                    textView2.setText(contextEditText.getText());
//                }
//
//            });
//            //삭제
//            del_Btn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    textView2.setVisibility(View.INVISIBLE);
//                    contextEditText.setText("");
//                    contextEditText.setVisibility(View.VISIBLE);
//                    save_Btn.setVisibility(View.VISIBLE);
//                    upd_Btn.setVisibility(View.INVISIBLE);
//                    del_Btn.setVisibility(View.INVISIBLE);
//                    removeDiary(readDay);
//                }
//            });
//            if (textView2.getText() == null)
//            {
//                textView2.setVisibility(View.INVISIBLE);
//                diaryTextView.setVisibility(View.VISIBLE);
//                save_Btn.setVisibility(View.VISIBLE);
//                upd_Btn.setVisibility(View.INVISIBLE);
//                del_Btn.setVisibility(View.INVISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    //저장삭제수정 sqlLite 로 변경//
//    @SuppressLint("WrongConstant")
//    public void removeDiary(String readDay)
//    {
//        FileOutputStream fos;
//        try
//        {
//            fos = getActivity().openFileOutput(readDay, Context.MODE_NO_LOCALIZED_COLLATORS);
//            String content = "";
//            fos.write((content).getBytes());
//            fos.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }//
//    }
//
//    @SuppressLint("WrongConstant")
//    public void saveDiary(String readDay)
//    {
//        FileOutputStream fos;
//        try
//        {
//            fos = requireContext().openFileOutput(readDay, Context.MODE_NO_LOCALIZED_COLLATORS);
//            String content = contextEditText.getText().toString();
//            fos.write((content).getBytes());
//            fos.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}