package com.ArnoVanEetvelde.fitnessapp;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration{
    private final int horizontalSpaceHeight;

    public HorizontalSpaceItemDecoration(int horizontalSpaceHeight) {
        this.horizontalSpaceHeight = horizontalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizontalSpaceHeight;
        outRect.right = horizontalSpaceHeight;
    }
}
