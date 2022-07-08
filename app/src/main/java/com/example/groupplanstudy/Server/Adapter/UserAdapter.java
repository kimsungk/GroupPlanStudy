package com.example.groupplanstudy.Server.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.Server.DTO.User;

import java.util.List;

public class UserAdapter
        extends RecyclerView.Adapter<UserAdapter.UserVeiwHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList){
        this.userList = userList;
    }

    //추가
    public void addUser(User user){
        userList.add(user);
        notifyDataSetChanged();
    }

    //수정
    public void updateUser(User user, int position){
        //u은 원본 user는 수정본
        User u = userList.get(position);
        u.setEmail(user.getEmail());
        u.setIntroduce(user.getIntroduce());
        u.setNickname(user.getNickname());
        u.setPassword(user.getPassword());
        notifyDataSetChanged();
    }

    //삭제
    public void removeUser(int position){
        userList.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserVeiwHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    class UserVeiwHolder extends RecyclerView.ViewHolder{

        public UserVeiwHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
