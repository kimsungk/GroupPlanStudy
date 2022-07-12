package com.example.groupplanstudy.Server.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto;

import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.GroupMemberHolder>
{
    private List<GroupMemberDto> groupMemberDtos;
    private Context context;

    public GroupMemberAdapter(Context context,List<GroupMemberDto> groupMemberDtos) {
        this.groupMemberDtos = groupMemberDtos;
        this.context = context;
    }

    //추가
    public void addGroupMemberItem(GroupMemberDto groupMemberDto){
        groupMemberDtos.add(groupMemberDto);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupMemberHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_member_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberHolder holder, int position) {
        GroupMemberDto groupMemberDto = groupMemberDtos.get(position);

        holder.tvNickname.setText(groupMemberDto.getName());
        holder.tvRole.setText(groupMemberDto.getRole()+"");
        holder.tvIntro.setText(groupMemberDto.getIntro());
    }



    @Override
    public int getItemCount() {
        return groupMemberDtos==null?0:groupMemberDtos.size();
    }



    class GroupMemberHolder extends RecyclerView.ViewHolder
    {
        private TextView tvNickname, tvRole,tvIntro;
        public GroupMemberHolder(@NonNull View itemView) {
            super(itemView);

            tvNickname= itemView.findViewById(R.id.groupmember_item_nickname);
            tvRole= itemView.findViewById(R.id.groupmember_item_role);
            tvIntro= itemView.findViewById(R.id.groupmember_item_intro);
        }
    }
}
