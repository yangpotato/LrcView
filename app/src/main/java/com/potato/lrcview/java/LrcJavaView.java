package com.potato.lrcview.java;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potato.lrcview.Lrc;

import java.util.List;

public class LrcJavaView extends FrameLayout {
    private RecyclerView mRecyclerView;
    private LrcAdapter mAdapter;
    private List<Lrc> mData;

    public LrcJavaView(Context context) {
        this(context, null);
    }

    public LrcJavaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcJavaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mRecyclerView = new RecyclerView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRecyclerView.setLayoutParams(layoutParams);
        mAdapter = new LrcAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        addView(mRecyclerView);
    }

    public void setLrcData(List<Lrc> data){
        if(mRecyclerView == null && mAdapter == null)
            return;
        mData = data;
        mAdapter.setData(data);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < mRecyclerView.getChildCount(); i++){
                    int height = mRecyclerView.getChildAt(i).getMeasuredHeight();
                    Log.i("YPOTATO", "第" + (i + 1) + "个高度为" + height);
                }

            }
        }, 1000);
    }

    public void updateTime(long time){
        if(mRecyclerView == null && mAdapter == null)
            return;
        if(mData == null)
            return;
        if(mData.get(mAdapter.getNowPosition() + 1).getTime() <= time){
            mAdapter.setNowPosition(mAdapter.getNowPosition() + 1);
        }
    }
}
