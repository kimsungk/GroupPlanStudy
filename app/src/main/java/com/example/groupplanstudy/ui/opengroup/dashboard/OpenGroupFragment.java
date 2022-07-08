package com.example.groupplanstudy.ui.opengroup.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.Service.OpenGroupService;
import com.example.groupplanstudy.databinding.FragmentOpengroupBinding;

import org.modelmapper.ModelMapper;

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

    private Retrofit retrofit= null;
    private OpenGroupService openGroupService= null;

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

        initRetrofit();

        //레트로핏으로 데이터 가져오기
        getOpenGroupsFromServer();

        // 리싸이클러뷰로 데이터

        return root;
    }

    private void initRetrofit()
    {
        retrofit= new Retrofit.Builder()
                .baseUrl("http://192.168.45.243:8866")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openGroupService= retrofit.create(OpenGroupService.class);
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

                    List<GroupRoomDto> groupRoomDtos = new ArrayList<>();
                    for(int i =0 ; i < objects.size(); i++) {
                        groupRoomDtos.add(modelMapper.map(objects.get(i),GroupRoomDto.class));
                    }
                    Log.d("result", groupRoomDtos.get(0).getUserDto().getIntroduce());

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





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}