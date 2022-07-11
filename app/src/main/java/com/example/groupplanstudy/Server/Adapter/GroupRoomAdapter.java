package com.example.groupplanstudy.Server.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.activities.GroupRoomActivity;

import java.util.List;

public class GroupRoomAdapter extends RecyclerView.Adapter<GroupRoomAdapter.GroupRoomHolder> {

    private List<GroupQnaDto> groupQnaDto;
    private Context mContext;

    public GroupRoomAdapter(Context mContext, List<GroupQnaDto> groupQnaDto) {
        this.mContext = mContext;
        this.groupQnaDto = groupQnaDto;
    }

    @NonNull
    @Override
    public GroupRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouproomqna_write , parent, false);
        return new GroupRoomAdapter.GroupRoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRoomAdapter.GroupRoomHolder holder, int position) {
//        GroupRoomDto groupRoomDto= groupRoomDtos.get(position);

//        holder.tvTitle.setText(groupRoomDto.getTitle());
//        holder.tvIntro.setText(groupRoomDto.getIntroduce());
//        holder.tvNickname.setText(groupRoomDto.getUserDto().getNickname());
//        holder.tvLimit.setText(Integer.toString(groupRoomDto.getMemberLimit()));

//        clickGroupRoom(holder,groupRoomDto);
    }
    private void clickGroupRoom(@NonNull GroupRoomAdapter.GroupRoomHolder holder, GroupRoomDto groupRoomDto){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("grouproom: ",groupRoomDto.getGrId()+"");
                mContext = view.getContext();

                // go to Group Room
                Intent intent = new Intent(mContext, GroupRoomActivity.class);
                intent.putExtra("grId", groupRoomDto.getGrId());
                mContext.startActivity(intent);

                //temp
//                Intent intent= new Intent(mContext, GroupMemberActivity.class);
//                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return groupQnaDto==null?0:groupQnaDto.size();
    }


    class GroupRoomHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvIntro, tvNickname, tvLimit;

        public GroupRoomHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.opengroup_item_title);
            tvIntro = itemView.findViewById(R.id.opengroup_item_intro);
            tvNickname = itemView.findViewById(R.id.opengroup_item_nickname);
            tvLimit = itemView.findViewById(R.id.opengroup_item_mem_limit);
        }
    }
}