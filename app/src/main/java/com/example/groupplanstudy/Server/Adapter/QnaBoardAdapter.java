package com.example.groupplanstudy.Server.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupplanstudy.R;
import com.example.groupplanstudy.Server.DTO.QnaBoardCommentDto;

import java.util.List;

public class QnaBoardAdapter extends RecyclerView.Adapter<QnaBoardAdapter.QnaBoardHolder>
{
    private List<QnaBoardCommentDto> qnaBoardCommentDtos;

    public QnaBoardAdapter(List<QnaBoardCommentDto> qnaBoardCommentDtos) {
        this.qnaBoardCommentDtos = qnaBoardCommentDtos;
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
