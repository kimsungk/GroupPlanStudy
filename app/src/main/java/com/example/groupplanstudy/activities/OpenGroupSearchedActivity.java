package com.example.groupplanstudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.OpenGroupAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.Service.OpenGroupService;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenGroupSearchedActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;

    private Retrofit retrofit;
    private OpenGroupService openGroupService;

    private List<GroupRoomDto> groupRoomDtos;
    private OpenGroupAdapter openGroupAdapter;

    private LinearLayoutManager linearLayoutManager;

    private ModelMapper modelMapper = new ModelMapper();
    private String query="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_group_searched);

        searchView = findViewById(R.id.opengroupsearched_search_view);
        recyclerView = findViewById(R.id.opengroupsearched_recyclerview);

        Intent intent = getIntent();

        query = intent.getStringExtra("query");
        searchView.setQuery(query,true);

        initRetrofit();

        getOpenGroupsFromServer();

        doSeachOpenGroup();
    }

    private void initRetrofit()
    {
        retrofit = Client.getClient();

        openGroupService= retrofit.create(OpenGroupService.class);
    }

    private void setRecyclerView(List<GroupRoomDto> groupRoomDtos)
    {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        openGroupAdapter= new OpenGroupAdapter(this,groupRoomDtos);
        recyclerView.setAdapter(openGroupAdapter);
    }

    private void getOpenGroupsFromServer()
    {
        Call<APIMessage> openGroupCall= openGroupService.getGroupRoomsByTitle(query);
        openGroupCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful()){
                    APIMessage apiMessage = modelMapper.map( response.body(), APIMessage.class);

                    List<Object> objects = (List<Object>) apiMessage.getData();

                    groupRoomDtos = new ArrayList<>();
                    for(int i =0 ; i < objects.size(); i++) {
                        groupRoomDtos.add(modelMapper.map(objects.get(i),GroupRoomDto.class));
                    }

                    // recylerview
                    setRecyclerView(groupRoomDtos);
                }
                else{
                    Log.d("result","onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Log.d("resultag","실패: "+t.getMessage());

            }
        });

    }

    private void doSeachOpenGroup()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("search :" , query);
                // call OpenGroupSearchedActivity
                Intent intent = new Intent(OpenGroupSearchedActivity.this, OpenGroupSearchedActivity.class);
                intent.putExtra("query", query);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}