package com.bejibx.android.recyclerview.selection;

import android.database.Observable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashSet;

@SuppressWarnings("Convert2streamapi")
public final class SelectionHelper
{
    private final HashSet<Integer> mSelectedItems = new HashSet<>();
    private final HolderClickObservable mHolderClickObservable = new HolderClickObservable();
    private final SelectionObservable mSelectionObservable = new SelectionObservable();

    private final WeakHolderTracker mTracker;

    private boolean mIsSelectable = false;

    public SelectionHelper()
    {
        this(new WeakHolderTracker());
    }

    @SuppressWarnings("UnusedDeclaration")
    public SelectionHelper(@NonNull WeakHolderTracker tracker)
    {
        mTracker = tracker;
    }

    public <H extends RecyclerView.ViewHolder> H wrapSelectable(H holder)
    {
        new ViewHolderMultiSelectionWrapper(holder);
        return holder;
    }

    public <H extends RecyclerView.ViewHolder> H wrapClickable(H holder)
    {
        new ViewHolderClickWrapper(holder);
        return holder;
    }

    public void bindHolder(RecyclerView.ViewHolder holder, int position)
    {
        mTracker.bindHolder(holder, position);
    }

    public void toggleItemSelected(RecyclerView.ViewHolder holder)
    {
        setItemSelected(holder, !isItemSelected(holder));
    }

    public boolean setItemSelected(RecyclerView.ViewHolder holder, boolean isSelected)
    {
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION)
        {
            boolean isAlreadySelected = isItemSelected(position);
            if (isSelected)
            {
                mSelectedItems.add(position);
            }
            else
            {
                mSelectedItems.remove(position);
            }
            if (isSelected ^ isAlreadySelected)
            {
                mSelectionObservable.notifySelectionChanged(holder, isSelected);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isItemSelected(RecyclerView.ViewHolder holder)
    {
        return mSelectedItems.contains(holder.getAdapterPosition());
    }

    public boolean isItemSelected(int position)
    {
        return mSelectedItems.contains(position);
    }

    public int getSelectedItemsCount()
    {
        return mSelectedItems.size();
    }

    public boolean isSelectable()
    {
        return mIsSelectable;
    }

    public void setSelectable(boolean isSelectable)
    {
        mIsSelectable = isSelectable;
        if (!isSelectable) clearSelection();
        mSelectionObservable.notifySelectableChanged(isSelectable);
    }

    public void clearSelection()
    {
        mSelectedItems.clear();
        for (RecyclerView.ViewHolder holder : mTracker.getTrackedHolders())
        {
            if (holder != null)
            {
                mSelectionObservable.notifySelectionChanged(holder, false);
            }
        }
    }

    public final void registerHolderClickObserver(@NonNull HolderClickObserver observer)
    {
        mHolderClickObservable.registerObserver(observer);
    }

    @SuppressWarnings("UnusedDeclaration")
    public final void unregisterSelectionObserver(@NonNull SelectionObserver observer)
    {
        mSelectionObservable.unregisterObserver(observer);
    }

    public final void registerSelectionObserver(@NonNull SelectionObserver observer)
    {
        mSelectionObservable.registerObserver(observer);
    }

    @SuppressWarnings("UnusedDeclaration")
    public final void unregisterHolderClickObserver(@NonNull HolderClickObserver observer)
    {
        mHolderClickObservable.unregisterObserver(observer);
    }

    private class HolderClickObservable extends Observable<HolderClickObserver>
    {
        public final void notifyOnHolderClick(RecyclerView.ViewHolder holder)
        {
            synchronized (mObservers)
            {
                for (HolderClickObserver observer : mObservers)
                {
                    observer.onHolderClick(holder);
                }
            }
        }

        public final boolean notifyOnHolderLongClick(RecyclerView.ViewHolder holder)
        {
            boolean isConsumed = false;
            synchronized (mObservers)
            {
                for (HolderClickObserver observer : mObservers)
                {
                    isConsumed = isConsumed || observer.onHolderLongClick(holder);
                }
            }
            return isConsumed;
        }
    }

    private class SelectionObservable extends Observable<SelectionObserver>
    {
        private void notifySelectionChanged(RecyclerView.ViewHolder holder, boolean isSelected)
        {
            synchronized (mObservers)
            {
                for (SelectionObserver observer : mObservers)
                {
                    observer.onSelectedChanged(holder, isSelected);
                }
            }
        }

        private void notifySelectableChanged(boolean isSelectable)
        {
            synchronized (mObservers)
            {
                for (SelectionObserver observer : mObservers)
                {
                    observer.onSelectableChanged(isSelectable);
                }
            }
        }
    }

    private abstract class ViewHolderWrapper implements android.view.View.OnClickListener
    {
        protected final WeakReference<RecyclerView.ViewHolder> mWrappedHolderRef;

        protected ViewHolderWrapper(RecyclerView.ViewHolder holder)
        {
            mWrappedHolderRef = new WeakReference<>(holder);
        }
    }

    private class ViewHolderMultiSelectionWrapper extends ViewHolderWrapper
            implements View.OnLongClickListener
    {
        private ViewHolderMultiSelectionWrapper(RecyclerView.ViewHolder holder)
        {
            super(holder);
            View itemView = holder.itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
        }

        @Override
        public final void onClick(View v)
        {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (holder != null)
            {
                if (isSelectable())
                {
                    toggleItemSelected(holder);
                }
                else
                {
                    mHolderClickObservable.notifyOnHolderClick(mWrappedHolderRef.get());
                }
            }
        }

        @Override
        public final boolean onLongClick(View v)
        {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (!isSelectable())
            {
                setSelectable(true);
                if (holder != null)
                {
                    setItemSelected(holder, true);
                }
                return true;
            }
            else
            {
                return holder == null || mHolderClickObservable.notifyOnHolderLongClick(holder);
            }
        }
    }

    private class ViewHolderClickWrapper extends ViewHolderWrapper
    {
        private ViewHolderClickWrapper(RecyclerView.ViewHolder holder)
        {
            super(holder);
            View itemView = holder.itemView;
            itemView.setOnClickListener(this);
            itemView.setClickable(true);
        }

        @Override
        public final void onClick(View v)
        {
            RecyclerView.ViewHolder holder = mWrappedHolderRef.get();
            if (holder != null)
            {
                mHolderClickObservable.notifyOnHolderClick(mWrappedHolderRef.get());
            }
        }
    }
}
