package com.bejibx.android.recyclerview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingDecoration extends RecyclerView.ItemDecoration
{
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public GridSpacingDecoration(int horizSpacing, int vertSpacing)
    {
        mHorizontalSpacing = horizSpacing;
        mVerticalSpacing = vertSpacing;
    }

    protected int getSpanStart(int layoutPosition, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        int spanEndIndex = getSpanEnd(layoutPosition, spanCount, lookup);
        int spanSize = getSpanSize(layoutPosition, lookup);
        return spanEndIndex - spanSize + 1;
    }

    protected int getSpanGroup(int layoutPosition, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return lookup.getSpanGroupIndex(layoutPosition, spanCount);
    }

    protected int getLastSpanGroup(int itemCount, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return lookup.getSpanGroupIndex(itemCount - 1, spanCount);
    }

    protected int getSpanSize(int layoutPosition, GridLayoutManager.SpanSizeLookup lookup)
    {
        return lookup.getSpanSize(layoutPosition);
    }

    protected int getSpanEnd(int layoutPosition, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return lookup.getSpanIndex(layoutPosition, spanCount);
    }

    @Override
    public final void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();

        int spanCount = manager.getSpanCount();
        int layoutPosition = parent.getChildLayoutPosition(view);

        int spanEndIndex = getSpanEnd(layoutPosition, spanCount, lookup);
        int spanStartIndex = getSpanStart(layoutPosition, spanCount, lookup);

        /* Span laid first in line */
        if (spanStartIndex % spanCount == 0)
        {
            outRect.left = 0;
        }
        else
        {
            outRect.left = mHorizontalSpacing / 2;
        }

        /* Span laid last in line */
        if ((spanEndIndex + 1) % spanCount == 0)
        {
            outRect.right = 0;
        }
        else
        {
            outRect.right = mHorizontalSpacing / 2;
        }

        int spanGroupIndex = getSpanGroup(layoutPosition, spanCount, lookup);
        int lastSpanGroupIndex = getLastSpanGroup(state.getItemCount(), spanCount, lookup);

        /* Span laid on first grid line. */
        if (spanGroupIndex == 0)
        {
            outRect.top = 0;
        }
        else
        {
            outRect.top = mVerticalSpacing / 2;
        }

        /* Span laid on last grid line. */
        if (spanGroupIndex == lastSpanGroupIndex)
        {
            outRect.bottom = 0;
        }
        else
        {
            outRect.bottom = mVerticalSpacing / 2;
        }
    }
}