package com.potato.lrcview.java;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potato.lrcview.LogUtil;
import com.potato.lrcview.Lrc;

import java.util.List;

public class LrcJavaView extends FrameLayout {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private LrcAdapter mAdapter;
    private List<Lrc> mData;
    private TopSmoothScroller smoothScroller;

    private Paint mLinePaint;
    /*是否手动过滑动歌词*/
    private boolean isGestureMove = false;
    /*手动滑动歌词的时间*/
    private long mGestureTime = 0;

    private int mRvScrollState = RecyclerView.SCROLL_STATE_IDLE;

    public LrcJavaView(Context context) {
        this(context, null);
    }

    public LrcJavaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcJavaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init();
    }

    private void init(){
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.parseColor("#95989D"));
        mLinePaint.setStrokeWidth(dp2px(10));

        smoothScroller = new TopSmoothScroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        log("widthMeasureSpec:" + widthMeasureSpec + "-heightMeasureSpec:" + heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawLine(0, getHeight() / 2f , getWidth(), getHeight() / 2f , mLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log("onTouchEvent:" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent:" + ev.getAction());
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                isGestureMove = true;
                mGestureTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setLrcData(List<Lrc> data){
        data.add(0, new Lrc(0, "歌词来源于网络"));
        data.add(new Lrc(0, "END"));
        mData = data;
        if(mRecyclerView == null){
            log("加载recyclerview");
            mRecyclerView = new RecyclerView(getContext());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mRecyclerView.setLayoutParams(layoutParams);
            mAdapter = new LrcAdapter(getContext());
            mLinearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration((int) dp2px(65), getMeasuredHeight()));
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LogUtil.log("onScrollStateChanged滑动状态:" + newState);
                    mRvScrollState = newState;
                }
            });
            addView(mRecyclerView);
        }
        if(mRecyclerView == null && mAdapter == null)
            return;
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
        int newPosition = mAdapter.getNowPosition() + 1;
        if(newPosition >= mData.size())
            return;
        if(mData.get(newPosition).getTime() <= time){
            mAdapter.setNowPosition(newPosition);
            if(isGestureMove){
                if(System.currentTimeMillis() - mGestureTime > 3000) {
                    scroll(mAdapter.getNowPosition(), 10f);
                    isGestureMove = false;
                }
            }else {
                scroll(mAdapter.getNowPosition());
            }
        }else if(isGestureMove && System.currentTimeMillis() - mGestureTime > 3000){
            scroll(mAdapter.getNowPosition(), 10f);
            isGestureMove = false;
        }
    }

    private void scroll(int position, float time){
        if(mLinearLayoutManager == null || mRecyclerView == null)
            return;
        smoothScroller.setTargetPosition(position);
        smoothScroller.setTime(time);
        mLinearLayoutManager.startSmoothScroll(smoothScroller);
    }

    private void scroll(int position) {
       scroll(position, 0);
    }

    private float dp2px(float dp){
        return (dp * this.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void log(String str){
        Log.i("YPOTATO", str);
    }

}
