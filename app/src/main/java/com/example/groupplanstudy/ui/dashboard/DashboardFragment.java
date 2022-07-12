package com.example.groupplanstudy.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groupplanstudy.DB.DBHelper;
import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.databinding.FragmentDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public String readDay = null;
    public CalendarView calendarView;
    public Button save_Btn;
    public TextView diaryTextView;
    public ListView listView;
    public EditText contextEditText;
    public Context context;
    public long userid = 0;
    public ScrollView scrollView;
    public ArrayAdapter adapter;
    int selectedPos = -1;

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

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMONTH = cal.get(Calendar.MONTH)+1;
        int cDAY = cal.get(Calendar.DAY_OF_MONTH);

        readDay = cYear+"-"+cMONTH+"-"+cDAY;

        Log.v("날짜:",readDay);

        checkDay(readDay);

        String text = PreferenceManager.getString(context, "user");
        try {
            JSONObject jsonObject = new JSONObject(text);
            userid = jsonObject.getLong("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("유저id",String.valueOf(userid));

        //캘린더 선택
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                readDay = year+"-"+(month+1)+"-"+dayOfMonth;
                diaryTextView.setText(readDay);
                contextEditText.setText("");

                //데이터 확인
                checkDay(readDay);
                setListViewHeightBasedOnChildren(listView);

                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //값설정
                        String date = readDay;
                        String content = contextEditText.getText().toString();

                        dbHelper.insert(userid,date,content);
                        Toast.makeText(getContext(),"저장되었습니다", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        checkDay(readDay);
                        setListViewHeightBasedOnChildren(listView);
                    }
                });
            }
        });
        return root;
    }

    //날짜 체크
    public void checkDay(String readDay)
    {
        diaryTextView.setText(readDay);

        listView.setVisibility(View.VISIBLE);

        String getReadDay = readDay;
        ArrayList<String> resultList;//
        DBHelper dbHelper = new DBHelper(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //날짜 체크비교 데이터 유무 판결

        //textview2에는 저장되어있는 content 값넣기
        resultList = dbHelper.getResult(getReadDay,userid);
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, resultList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);

        //클릭해서 수정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedPos = position;

                AlertDialog.Builder alertDig = new AlertDialog.Builder(view.getContext());

                final EditText editText = new EditText(context);
                editText.setText(resultList.get(selectedPos));

                alertDig.setMessage("수정하시겠습니까?")
                .setView(editText)
                .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //수정하기
                        String newContent = editText.getText().toString();//수정할 텍스트 값 넣기
                        
                        dbHelper.update(resultList.get(selectedPos),userid,readDay, newContent);
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                        checkDay(readDay);
                        setListViewHeightBasedOnChildren(listView);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDig.show();

            }
        });

        //길게클릭해서 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedPos = position;

                AlertDialog.Builder alertDig = new AlertDialog.Builder(view.getContext());
                alertDig.setMessage("삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //삭제하기
                        dbHelper.delete(userid, readDay, resultList.get(selectedPos));
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                        checkDay(readDay);
                        setListViewHeightBasedOnChildren(listView);
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
                return true;
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}