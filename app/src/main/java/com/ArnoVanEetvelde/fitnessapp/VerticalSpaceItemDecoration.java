package com.ArnoVanEetvelde.fitnessapp;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{
    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int horizontalSpaceHeight) {
        this.verticalSpaceHeight = horizontalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.top = verticalSpaceHeight;
        outRect.bottom = verticalSpaceHeight;
    }
}