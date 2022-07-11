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
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.activities.GroupMemberActivity;

import java.util.List;

public class OpenGroupAdapter extends RecyclerView.Adapter<OpenGroupAdapter.OpenGroupHolder>
{
    private List<GroupRoomDto> groupRoomDtos;
    private Context mContext;
    public OpenGroupAdapter(Context mContext, List<GroupRoomDto> groupRoomDtos) {
        this.mContext = mContext;
        this.groupRoomDtos = groupRoomDtos;
    }

    @NonNull
    @Override
    public OpenGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opengroups_item,parent,false);
        return new OpenGroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenGroupHolder holder, int position) {
        GroupRoomDto groupRoomDto= groupRoomDtos.get(position);

        holder.tvTitle.setText(groupRoomDto.getTitle());
        holder.tvIntro.setText(groupRoomDto.getIntroduce());
        holder.tvNickname.setText(groupRoomDto.getUserDto().getNickname());
        holder.tvLimit.setText(Integer.toString(groupRoomDto.getMemberLimit()));

        clickGroupRoom(holder,groupRoomDto);
    }

    private void clickGroupRoom(@NonNull OpenGroupHolder holder,GroupRoomDto groupRoomDto)
    {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("grouproom: ",groupRoomDto.getGrId()+"");
                mContext = view.getContext();

                // go to Group Room

                //temp
                Intent intent= new Intent(mContext, GroupMemberActivity.class);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return groupRoomDtos==null?0:groupRoomDtos.size();
    }


    class OpenGroupHolder extends RecyclerView.ViewHolder
    {
        private TextView tvTitle, tvIntro, tvNickname, tvLimit;

        public OpenGroupHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.opengroup_item_title);
            tvIntro = itemView.findViewById(R.id.opengroup_item_intro);
            tvNickname = itemView.findViewById(R.id.opengroup_item_nickname);
            tvLimit = itemView.findViewById(R.id.opengroup_item_mem_limit);
        }
    }
}
