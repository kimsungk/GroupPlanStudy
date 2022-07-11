package com.example.groupplanstudy.Server.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.User;

import java.util.List;

public class ApplyMemberAdapter extends RecyclerView.Adapter<ApplyMemberAdapter.ApplyMemberHolder>
{
    private List<User> applyUserDtos;
    private Context mContext;

    public ApplyMemberAdapter(Context mContext,List<User> applyUserDtos) {
        this.applyUserDtos = applyUserDtos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ApplyMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_apply_member_item, parent, false);
        return new ApplyMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplyMemberHolder holder, int position) {
        User userDto= applyUserDtos.get(position);

        holder.tvNickname.setText(userDto.getNickname());
        holder.btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowApplyMemberToServer();
            }
        });
        holder.btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refuseApplyMemberToServer();
            }
        });
    }

    private void allowApplyMemberToServer()
    {
        Log.d("allow","allow btn");
    }

    private void refuseApplyMemberToServer()
    {
        Log.d("refuse","refuse btn");
    }

    @Override
    public int getItemCount() {
        return applyUserDtos==null?0:applyUserDtos.size();
    }

    class ApplyMemberHolder extends RecyclerView.ViewHolder
    {
        private TextView tvNickname;
        private Button btnAllow, btnRefuse;

        public ApplyMemberHolder(@NonNull View itemView) {
            super(itemView);

            tvNickname= itemView.findViewById(R.id.groupapplymember_item_nickname);
            btnAllow= itemView.findViewById(R.id.groupapplymember_item_btn_allow);
            btnRefuse= itemView.findViewById(R.id.groupapplymember_item_btn_refuse);
        }
    }
}
