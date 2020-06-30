package com.potato.lrcview.java;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potato.lrcview.Lrc;
import com.potato.lrcview.R;

import java.util.ArrayList;
import java.util.List;

public class LrcAdapter extends RecyclerView.Adapter<LrcAdapter.ViewHolder> {
    private List<Lrc> mDatas = new ArrayList<>();
    private Context mContext;
    private int mNowPosition = 0;

    public LrcAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LrcAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_lrc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LrcAdapter.ViewHolder holder, int position) {
        holder.tvLrc.setText(mDatas.get(position).getContent());
        holder.tvLrc.setTextColor(Color.parseColor(position == mNowPosition ? "#0000ff" : "#00ff00"));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public int getNowPosition() {
        return mNowPosition;
    }

    public void setNowPosition(int mNowPosition) {
        this.mNowPosition = mNowPosition;
        notifyDataSetChanged();
    }

    public void setData(List<Lrc> mDatas) {
        if(mDatas == null)
            return;
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLrc, tvLrcTranslate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLrc = itemView.findViewById(R.id.tv_lrc);
            tvLrcTranslate = itemView.findViewById(R.id.tv_lrc_translate);
        }
    }
}
