package com.example.groupplanstudy.Server.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.ApplyMemberDto;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.ApplyMemberService;
import com.example.groupplanstudy.activities.GroupMemberActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApplyMemberAdapter extends RecyclerView.Adapter<ApplyMemberAdapter.ApplyMemberHolder>
{
    private Context mContext;
    private List<User> applyUserDtos;
    private long grId;

    private Retrofit retrofit;
    private ApplyMemberService applyMemberService;

    public ApplyMemberAdapter(Context mContext, List<User> applyUserDtos, long grId) {
        this.applyUserDtos = applyUserDtos;
        this.mContext = mContext;
        this.grId = grId;
        retrofit = Client.getClient();
        applyMemberService= retrofit.create(ApplyMemberService.class);
    }

    ////////////////
    //인터페이스
    public interface  OnItemClickListener{
        void onAllowClick(ApplyMemberDto applyMemberDto, int pos);
        void onRefuseClick(ApplyMemberDto applyMemberDto, int pos);
    }
    private OnItemClickListener onItemClickListener;
    //setter
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    ////////////////

    public void removeApplyMemberItem(int pos ){
        applyUserDtos.remove(pos);
        notifyDataSetChanged();
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
        ApplyMemberDto applyMemberDto = new ApplyMemberDto(grId,userDto.getUid());

        final int sPosition= position;

        holder.tvNickname.setText(userDto.getNickname());
    }

    // 신청멤버에 - 1,
    // 현재 멤버에 + 1
    private void allowApplyMemberToServer(ApplyMemberDto applyMemberDto, int position)
    {
        Call<APIMessage> allowMemberCall = applyMemberService.allowGroupMember(applyMemberDto);
        allowMemberCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful())
                {
                    APIMessage apiMessage= response.body();
                    String message = apiMessage.getMessage();
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                    Intent intent= new Intent(mContext, GroupMemberActivity.class);
                    intent.putExtra("grId", grId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }else{
                    Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refuseApplyMemberToServer(ApplyMemberDto applyMemberDto, int position)
    {
        Log.d("refuse: ", applyMemberDto.getGrId()+", "+applyMemberDto.getUid()+"");
        Call<APIMessage> refuseMemberCall = applyMemberService.refuseGroupMember(applyMemberDto);
        refuseMemberCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                if(response.isSuccessful())
                {
                    APIMessage apiMessage= response.body();
                    String message = apiMessage.getMessage();
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                    applyUserDtos.remove(position);
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Toast.makeText(mContext, "네트워크 에러", Toast.LENGTH_SHORT).show();
            }
        });
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

            btnAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User userDto= applyUserDtos.get(getAdapterPosition());
                    ApplyMemberDto applyMemberDto = new ApplyMemberDto(grId,userDto.getUid());

                    onItemClickListener.onAllowClick(applyMemberDto, getAdapterPosition());
                }
            });

            btnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User userDto= applyUserDtos.get(getAdapterPosition());
                    ApplyMemberDto applyMemberDto = new ApplyMemberDto(grId,userDto.getUid());

                    onItemClickListener.onRefuseClick(applyMemberDto, getAdapterPosition());
                }
            });
        }
    }
}
