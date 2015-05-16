package com.bejibx.android.recyclerview.decoration;

import android.support.v7.widget.GridLayoutManager;

public class GridSimpleSpacingDecoration extends GridSpacingDecoration
{
    public GridSimpleSpacingDecoration(int horizSpacing, int vertSpacing)
    {
        super(horizSpacing, vertSpacing);
    }

    @Override
    protected int getSpanStart(int layoutPosition, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return layoutPosition;
    }

    @Override
    protected int getSpanEnd(int layoutPosition, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return layoutPosition;
    }

    @Override
    protected int getSpanSize(int layoutPosition, GridLayoutManager.SpanSizeLookup lookup)
    {
        return 1;
    }

    @Override
    protected int getSpanGroup(int layoutPosition, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return layoutPosition / spanCount;
    }

    @Override
    protected int getLastSpanGroup(int itemCount, int spanCount, GridLayoutManager.SpanSizeLookup lookup)
    {
        return (itemCount / spanCount) - 1;
    }
}
