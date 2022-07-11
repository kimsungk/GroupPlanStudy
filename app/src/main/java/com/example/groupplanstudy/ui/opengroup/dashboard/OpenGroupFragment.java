package com.example.groupplanstudy.ui.opengroup.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Adapter.OpenGroupAdapter;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.Service.OpenGroupService;
import com.example.groupplanstudy.activities.OpenGroupSearchedActivity;
import com.example.groupplanstudy.databinding.FragmentOpengroupBinding;

import org.modelmapper.ModelMapper;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenGroupFragment extends Fragment {

    private FragmentOpengroupBinding binding;

    private SearchView searchView;
    private RecyclerView recyclerView;

    private Retrofit retrofit;
    private OpenGroupService openGroupService;
    private List<GroupRoomDto> groupRoomDtos;
    private OpenGroupAdapter openGroupAdapter;

    private LinearLayoutManager linearLayoutManager;
    private Context context;

    private ModelMapper modelMapper = new ModelMapper();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OpenGroupViewModel openGroupViewModel =
                new ViewModelProvider(this).get(OpenGroupViewModel.class);

        binding = FragmentOpengroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textOpengroup;
//        openGroupViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        searchView = root.findViewById(R.id.opengroup_search_view);
        recyclerView = root.findViewById(R.id.opengroup_recyclerview);
        context = root.getContext();


        initRetrofit();

        //레트로핏으로 데이터 가져오기
        getOpenGroupsFromServer();

        doSeachOpenGroup();

        return root;
    }

    private void initRetrofit()
    {
//        retrofit= new Retrofit.Builder()
//                .baseUrl("http://192.168.45.243:8866")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        retrofit = Client.getClient();

        openGroupService= retrofit.create(OpenGroupService.class);
    }

    private void setRecyclerView(List<GroupRoomDto> groupRoomDtos)
    {
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        openGroupAdapter= new OpenGroupAdapter(context,groupRoomDtos);
        recyclerView.setAdapter(openGroupAdapter);
    }

    private void getOpenGroupsFromServer()
    {
        Call<APIMessage> openGroupCall= openGroupService.getGroupRooms();
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
                Intent intent = new Intent(getActivity(), OpenGroupSearchedActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}