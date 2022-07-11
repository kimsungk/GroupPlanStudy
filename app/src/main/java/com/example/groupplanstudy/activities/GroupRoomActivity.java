package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.example.groupplanstudy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupRoomActivity extends AppCompatActivity {
    private RecyclerView grouproom_recyclerView;
    LinearLayoutManager manager;
    private Long grId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton grouproom_floatBtn = findViewById(R.id.grouproom_floatBtn);
        setContentView(R.layout.activity_group_room);
        Intent intent = getIntent();
        grId = intent.getLongExtra("grId", 0);

        Log.d("grId",grId+"");

//        PhoneService phoneService
//                = PhoneClient.getClient().create(PhoneService.class);
//        Call<List<Phone>> call = phoneService.findAll();
//
//        call.enqueue(new Callback<List<Phone>>() {
//            @Override
//            public void onResponse(Call<List<Phone>> call, Response<List<Phone>> response) {
//                List<Phone> phoneList = response.body();
//                recyclerView = findViewById(R.id.recyclerView);
//                manager = new LinearLayoutManager(MainActivity.this,
//                        RecyclerView.VERTICAL, false);
//                recyclerView.setLayoutManager(manager);
//
//                phoneAdapter = new PhoneAdapter(phoneList);
//                recyclerView.setAdapter(phoneAdapter);
//                Log.d("size : ", phoneAdapter.getItemCount() + "");
//            }
//
//            @Override
//            public void onFailure(Call<List<Phone>> call, Throwable t) {
//
//            }
//        });

//        floatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View dialogView = LayoutInflater.from(getApplicationContext())
//                        .inflate(R.layout.layout_concat, null);
//                final EditText etName = dialogView.findViewById(R.id.etname);
//                final EditText etTel = dialogView.findViewById(R.id.ettel);
//                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                dlg.setTitle("등록");
//                dlg.setView(dialogView);
//                dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        PhoneService phoneService
//                                = PhoneClient.getClient().create(PhoneService.class);
//
//                        final EditText etName = dialogView.findViewById(R.id.etname);
//                        final EditText etTel = dialogView.findViewById(R.id.ettel);
//
//                        Phone p = new Phone(etName.getText().toString(),
//                                etTel.getText().toString());
//
//                        Call<Phone> call = phoneService.save(p);
//
//                        call.enqueue(new Callback<Phone>() {
//                            @Override
//                            public void onResponse(Call<Phone> call, Response<Phone> response) {
//                                phoneAdapter.addItem(response.body());
//                            }
//
//                            @Override
//                            public void onFailure(Call<Phone> call, Throwable t) {
//
//                            }
//                        });
//                    }
//                });
//                dlg.setNegativeButton("닫기", null);
//                dlg.show();
//            }
//        });

    }
}