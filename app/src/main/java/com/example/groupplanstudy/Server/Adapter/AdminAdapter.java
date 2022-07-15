package com.example.groupplanstudy.Server.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.User;
import com.example.groupplanstudy.Server.Service.LoginService;
import com.example.groupplanstudy.activities.AdminActivity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminHolder> {

    private List<User> userList;
    private Context context;

    public AdminAdapter(List<User> userList, AdminActivity adminActivity, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminAdapter.AdminHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_item, parent, false);
        return new AdminHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminHolder holder, int position) {
        User user = userList.get(position);

        holder.tvAUserid.setText(String.valueOf(user.getUid()));
        holder.tvAEmail.setText(user.getEmail());
        holder.tvANickname.setText(user.getNickname());

        clickUser(holder, user);
    }

    public void clickUser(AdminHolder holder, User user) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, user.getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LoginService loginService
                        = Client.getClient().create(LoginService.class);

                final EditText editText = new EditText(context);

                AlertDialog.Builder alertDig = new AlertDialog.Builder(view.getContext());
                if (!user.getEmail().equals("admin")) {
                    alertDig.setMessage("정말로 이 유저를 탈퇴시키시겠습니까? (탈퇴하시려면 <탈퇴> 를 입력해주세요)")
                            .setView(editText)
                            .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (editText.getText().toString().equals("탈퇴")) {
                                        Call<ResponseBody> call = loginService.deleteUser(user.getUid());
                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                Toast.makeText(context, "성공적으로 삭제되엇습니다", Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(context, "탈퇴를 입력해주세요",Toast.LENGTH_SHORT).show();
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


    @Override
    public int getItemCount() {
        return userList.size();
    }

    class AdminHolder extends RecyclerView.ViewHolder {
        private TextView tvAUserid, tvAEmail, tvANickname;

        public AdminHolder(@NonNull View itemView) {
            super(itemView);

            tvAUserid = itemView.findViewById(R.id.tvAUserid);
            tvAEmail = itemView.findViewById(R.id.tvAEmail);
            tvANickname = itemView.findViewById(R.id.tvANickname);
        }
    }
}
