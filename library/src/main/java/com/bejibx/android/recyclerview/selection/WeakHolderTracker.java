package com.bejibx.android.recyclerview.selection;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WeakHolderTracker
{
    private final SparseArray<WeakReference<RecyclerView.ViewHolder>> mHoldersByPosition =
            new SparseArray<>();

    public void bindHolder(RecyclerView.ViewHolder holder, int position)
    {
        mHoldersByPosition.put(position, new WeakReference<>(holder));
    }

    @Nullable
    private RecyclerView.ViewHolder getHolder(int position)
    {
        WeakReference<RecyclerView.ViewHolder> holderRef = mHoldersByPosition.get(position);
        if (holderRef == null)
        {
            mHoldersByPosition.remove(position);
            return null;
        }

        RecyclerView.ViewHolder holder = holderRef.get();
        if (holder == null || (holder.getAdapterPosition() != position && holder.getAdapterPosition() != RecyclerView.NO_POSITION))
        {
            mHoldersByPosition.remove(position);
            return null;
        }


        return holder;
    }

    public List<RecyclerView.ViewHolder> getTrackedHolders()
    {
        List<RecyclerView.ViewHolder> holders = new ArrayList<>();

        for (int i = 0; i < mHoldersByPosition.size(); i++)
        {
            int key = mHoldersByPosition.keyAt(i);
            RecyclerView.ViewHolder holder = getHolder(key);

            if (holder != null)
            {
                holders.add(holder);
            }
        }

        return holders;
    }
}
