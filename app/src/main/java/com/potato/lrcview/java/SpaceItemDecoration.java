package com.potato.lrcview.java;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int lineHeight;
    private int viewHeight;
    public SpaceItemDecoration(int lineHeight, int viewHeight) {
        this.lineHeight = lineHeight;
        this.viewHeight = viewHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int size = -1;
        if(parent.getAdapter() != null)
            size = parent.getAdapter().getItemCount();
        if(position == 0){
            outRect.top = (viewHeight - lineHeight) / 2;
            outRect.bottom = 0;
        }else if(position == size - 1){
            outRect.bottom = (viewHeight - lineHeight) / 2;
            outRect.top = 0;
        }else{
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }

}
