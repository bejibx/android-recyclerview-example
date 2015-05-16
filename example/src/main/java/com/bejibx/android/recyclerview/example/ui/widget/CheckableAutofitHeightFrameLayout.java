package com.bejibx.android.recyclerview.example.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

public class CheckableAutofitHeightFrameLayout extends FrameLayout implements Checkable
{
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mIsChecked;

    public CheckableAutofitHeightFrameLayout(Context context)
    {
        super(context);
    }

    public CheckableAutofitHeightFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CheckableAutofitHeightFrameLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckableAutofitHeightFrameLayout(Context context, AttributeSet attrs,
                                             int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace)
    {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
        {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean isChecked)
    {
        boolean wasChecked = isChecked();
        mIsChecked = isChecked;

        if (wasChecked ^ mIsChecked)
        {
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked()
    {
        return mIsChecked;
    }

    @Override
    public void toggle()
    {
        setChecked(!mIsChecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
