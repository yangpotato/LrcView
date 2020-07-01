package com.potato.lrcview.java;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.potato.lrcview.LogUtil;

public class TopSmoothScroller extends LinearSmoothScroller {
    private static final float SCROLL_TIME = 500f;
    protected Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private DisplayMetrics mDisplayMetrics2;

    private float time = SCROLL_TIME;
    public TopSmoothScroller(Context context) {
        super(context);
        mDisplayMetrics2 = context.getResources().getDisplayMetrics();
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_ANY;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_ANY;
    }

    public void setTime(float time) {
        this.time = time > 0 ? time : SCROLL_TIME;

    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        LogUtil.log("calculateSpeedPerPixel");
        return time / displayMetrics.densityDpi;
    }

    @Override
    protected int calculateTimeForScrolling(int dx) {
        if(time == SCROLL_TIME)
            return super.calculateTimeForScrolling(dx);
        else
            return (int) Math.ceil(Math.abs(dx) * (50f / mDisplayMetrics2.densityDpi));
    }

    protected float calculateSpeedPerPixel2() {
        return time / mDisplayMetrics2.densityDpi;
    }

    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
    }

    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
//        super.onTargetFound(targetView, state, action);
        final int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
        final int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
        final int distance = (int) Math.sqrt(dx * dx + dy * dy);
        final int time = calculateTimeForDeceleration(distance);
        LogUtil.log("滑动时间:" + time);
        if (time > 0) {
            action.update(-dx, -dy, time, mInterpolator);
        }
    }
}
