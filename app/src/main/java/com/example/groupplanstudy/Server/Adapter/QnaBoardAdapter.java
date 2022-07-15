package com.example.groupplanstudy.Server.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.Client;
import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.PreferenceManager;
import com.example.groupplanstudy.Server.DTO.QnaBoardCommentDto;
import com.example.groupplanstudy.Server.Service.QnaBoardCommentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnaBoardAdapter extends RecyclerView.Adapter<QnaBoardAdapter.QnaBoardHolder>
{
    private List<QnaBoardCommentDto> qnaBoardCommentDtos;
    private Activity activity;
    private QnaBoardCommentService qnaBoardCommentService;
    private long grId,bid;
    private long uid;

    public QnaBoardAdapter(List<QnaBoardCommentDto> qnaBoardCommentDtos, Activity activity, long grId, long bid) {
        this.qnaBoardCommentDtos = qnaBoardCommentDtos;
        this.activity = activity;
        this.grId = grId;
        this.bid = bid;
        uid = getUid();
    }

    @NonNull
    @Override
    public QnaBoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qnaboard_coment_item, parent, false);
        return new QnaBoardAdapter.QnaBoardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QnaBoardHolder holder, int position) {
        QnaBoardCommentDto qnaBoardCommentDto= qnaBoardCommentDtos.get(position);

        holder.tvNickname.setText(qnaBoardCommentDto.getUserDto().getNickname());
        holder.tvIntro.setText(qnaBoardCommentDto.getUserDto().getIntroduce());
        holder.tvComment.setText(qnaBoardCommentDto.getContent());

        final int nPosition = position;
        holder.tvComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(uid == qnaBoardCommentDto.getUserDto().getUid())
                    doUpdateOrDelete(grId, bid, qnaBoardCommentDto.getCid() , nPosition);
                return false;
            }
        });
    }

    private long getUid()
    {
        long loginUserId=0;

        String text= PreferenceManager.getString(activity, "user");

        int a= 0;
        String val="";
        try
        {
            JSONObject userJsonObject = new JSONObject(text);

            val =userJsonObject.getString("uid");

            a= Integer.valueOf(val);

        } catch (JSONException |NumberFormatException e) {
            e.printStackTrace();
            loginUserId= (long)Double.parseDouble(val);
        }
        return loginUserId;
    }

    private void doUpdateOrDelete(long grId, long bid, long cid, int position)
    {
        AlertDialog.Builder alertDig = new AlertDialog.Builder(activity);
        alertDig.setMessage("삭제하기").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeComment(grId, bid, cid, position);
            }
        })
        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //취소하기
                dialogInterface.dismiss();
            }
        });
        alertDig.show();
    }

    private void removeComment(long grId, long bid, long cid, int position)
    {
        qnaBoardCommentService = Client.getClient().create(QnaBoardCommentService.class);

        Call<APIMessage> removeCommentCall = qnaBoardCommentService.deleteQnacommentByCid(grId, bid, cid);
        removeCommentCall.enqueue(new Callback<APIMessage>() {
            @Override
            public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                qnaBoardCommentDtos.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<APIMessage> call, Throwable t) {
                Toast.makeText(activity, "네트워크 에러", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addComment(QnaBoardCommentDto qnaBoardCommentDto)
    {
        qnaBoardCommentDtos.add(qnaBoardCommentDto);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return qnaBoardCommentDtos.size();
    }

    class QnaBoardHolder extends RecyclerView.ViewHolder
    {
        private TextView tvNickname, tvIntro, tvComment;

        public QnaBoardHolder(@NonNull View itemView) {
            super(itemView);

            tvNickname = itemView.findViewById(R.id.qnaboard_item_tv_nickname);
            tvIntro = itemView.findViewById(R.id.qnaboard_item_tv_intro);
            tvComment = itemView.findViewById(R.id.qnaboard_item_tv_comment);
        }


    }
}
