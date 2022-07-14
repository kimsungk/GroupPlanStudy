package com.example.groupplanstudy.Server.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto2;
import com.example.groupplanstudy.Server.DTO.GroupRole;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.Server.Service.GroupRoomService;
import com.example.groupplanstudy.Server.Service.OpenGroupService;
import com.example.groupplanstudy.activities.GroupRoomActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyGroupRoomAdapter extends RecyclerView.Adapter<MyGroupRoomAdapter.MyGroupHolder> {

    private List<GroupMemberDto2> groupMemberDto2List;
    private Activity activity;
    private Context mContext;
    private long grId;
    private long gmId;
    private GroupRoomService groupRoomService;

    public MyGroupRoomAdapter(List<GroupMemberDto2> groupMemberDto2List, Activity activity, Context context) {
        this.groupMemberDto2List = groupMemberDto2List;
        this.activity = activity;
        this.mContext = context;
    }

    //레이아웃 잇기
    @NonNull
    @Override
    public MyGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mygroup_item, parent, false);
        return new MyGroupHolder(view);
    }

    //값넣기
    @Override
    public void onBindViewHolder(@NonNull MyGroupHolder holder, int position) {
        GroupMemberDto2 groupMemberDto2 = groupMemberDto2List.get(position);

        grId = groupMemberDto2.getGroupRoomDto().getGrId();
        gmId = groupMemberDto2.getGmId();

        holder.tvTitle.setText(groupMemberDto2.getGroupRoomDto().getTitle());
        holder.tvIntro.setText(groupMemberDto2.getGroupRoomDto().getIntroduce());
        holder.tvMaxPersonnel.setText("맴버정원 "+groupMemberDto2.getGroupRoomDto().getMemberLimit());
        if(groupMemberDto2.getRole().equals(GroupRole.LEADER)){
            holder.tvRole.setText("LEADER");
            holder.tvRole.setTextColor(0xAAef484a);
        }else if(groupMemberDto2.getRole().equals(GroupRole.MEMBER)){
            holder.tvRole.setText("MEMBER");
        }

        clickMyGroupRoom(holder,groupMemberDto2);

    }

    public void clickMyGroupRoom(MyGroupHolder holder, GroupMemberDto2 groupMemberDto2){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //grid로 grouproom 을 찾아 전체 넣기
                Long gr = groupMemberDto2.getGroupRoomDto().getGrId();

                Log.d("", ""+gr);
                getGroupRoomFromServer(gr);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Long gmId = groupMemberDto2.getGmId();

                final EditText editText = new EditText(mContext);

                AlertDialog.Builder alertDig = new AlertDialog.Builder(view.getContext());
                //맴버 삭제하기
                //리더일경우
                if(groupMemberDto2.getRole().equals(GroupRole.LEADER)){
                    alertDig.setMessage("당신은 이방의 방장입니다 정말로 방을 삭제하시겠습니까? (삭제하시려면 <삭제>를 입력하세요)")
                            .setView(editText)
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //삭제하기
                                    if(editText.getText().toString().equals("삭제")){
                                        //groupMemberDto2.getGroupRoomDto().getGrId() 로 그룹룸 삭제하기

                                        Toast.makeText(mContext,groupMemberDto2.getGroupRoomDto().getTitle()+"방이 삭제되었습니다",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mContext,"'삭제'를 입력하세요",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDig.show();

                }else if(groupMemberDto2.getRole().equals(GroupRole.MEMBER)){
                    alertDig.setMessage("당신은 이방의 맴버입니다 정말로 방을 탈퇴하시겠습니까? (탈퇴하시려면 <탈퇴>를 입력하세요)")
                            .setView(editText)
                            .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(editText.getText().toString().equals("탈퇴")){
                                        //groupMemberDto.getGmId()로 맴버삭제하기

                                        Toast.makeText(mContext,groupMemberDto2.getGroupRoomDto().getTitle()+"방에서 탈퇴하였습니다",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mContext,"'탈퇴'를 입력하세요",Toast.LENGTH_SHORT).show();
                                    }
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

                return true;
            }
        });
    }

    //GroupRoomDto 들고오기
    public void getGroupRoomFromServer(long grId){
        Log.d("grid",""+grId);

        groupRoomService = Client.getClient().create(GroupRoomService.class);

        Call<GroupRoomDto> call = groupRoomService.getGroupRoom(grId);
        call.enqueue(new Callback<GroupRoomDto>() {
            @Override
            public void onResponse(Call<GroupRoomDto> call, Response<GroupRoomDto> response) {
                if(response.isSuccessful()){

                    GroupRoomDto groupRoomDto = new GroupRoomDto();

                    groupRoomDto.setIntroduce(response.body().getIntroduce());
                    groupRoomDto.setTitle(response.body().getTitle());
                    groupRoomDto.setMemberLimit(response.body().getMemberLimit());
                    groupRoomDto.setGrId(response.body().getGrId());
                    groupRoomDto.setApplicable(response.body().getApplicable());
                    groupRoomDto.setUserDto(response.body().getUserDto());

                    Intent intent = new Intent(mContext, GroupRoomActivity.class);
                    intent.putExtra("groupRoomDto", groupRoomDto);
                    mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else{
                    Log.d("result","onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<GroupRoomDto> call, Throwable t) {
                Log.d("resultag","실패: "+t.getMessage());
            }
        });
    }

    //리스트 길이
    @Override
    public int getItemCount() {
        return groupMemberDto2List.size();
    }

    class  MyGroupHolder extends RecyclerView.ViewHolder{
        private TextView tvRole, tvTitle, tvIntro, tvMaxPersonnel;

        public MyGroupHolder(@NonNull View itemView) {
            super(itemView);

            //텍스트뷰
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvIntro = itemView.findViewById(R.id.tvIntro);
            tvMaxPersonnel = itemView.findViewById(R.id.tvMaxPersonnel);
            tvRole = itemView.findViewById(R.id.tvRole);
        }
    }



}
