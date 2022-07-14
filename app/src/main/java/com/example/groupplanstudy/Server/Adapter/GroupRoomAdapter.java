package com.example.groupplanstudy.Server.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupMemberDto;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;
import com.example.groupplanstudy.activities.GroupRoomActivity;
import com.example.groupplanstudy.activities.QnaBoardActivity;

import java.util.List;
import java.util.Map;

public class GroupRoomAdapter extends RecyclerView.Adapter<GroupRoomAdapter.GroupQnaHolder> {

    private Context mContext;
    private List<GroupQnaDto> groupQnaDtos;
    private Long uid;
    private Map<Long, GroupMemberDto> groupMemberDtoMap;

    public GroupRoomAdapter(Context mContext, List<GroupQnaDto> groupQnaDtos, Long uid, Map<Long, GroupMemberDto> groupMemberDtoMap) {
        this.mContext = mContext;
        this.groupQnaDtos = groupQnaDtos;
        this.uid = uid;
        this.groupMemberDtoMap = groupMemberDtoMap;
    }

    @NonNull
    @Override
    public GroupQnaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grouproom , parent, false);
        return new GroupRoomAdapter.GroupQnaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRoomAdapter.GroupQnaHolder holder, int position) {
        GroupQnaDto groupQnaDto= groupQnaDtos.get(position);

        holder.groupqna_title.setText(groupQnaDto.getTitle());
        holder.groupqna_content.setText(groupQnaDto.getContent());


        clickQnaBoard(holder,groupQnaDto);
    }

    private void clickQnaBoard(@NonNull GroupQnaHolder holder, GroupQnaDto groupQnaDto){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groupMemberDtoMap.containsKey(uid)) {
                    Toast.makeText(mContext, "멤버가 아닙니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent= new Intent(mContext, QnaBoardActivity.class);
                intent.putExtra("groupQnaDto", groupQnaDto);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return groupQnaDtos==null?0:groupQnaDtos.size();
    }


    
    //Qna 쓰기
    public void addItem(GroupQnaDto body) {
        groupQnaDtos.add(body);

        notifyDataSetChanged();
    }


    //리사이클러뷰
    class GroupQnaHolder extends RecyclerView.ViewHolder{
        private TextView groupqna_title, groupqna_content;

        public GroupQnaHolder(@NonNull View itemView) {
            super(itemView);

            groupqna_title = itemView.findViewById(R.id.groupqna_title);
            groupqna_content = itemView.findViewById(R.id.groupqna_content);
        }
    }
}