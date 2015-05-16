package com.bejibx.android.recyclerview.example.gallery;

public interface DataSet<ItemType>
{
    ItemType getItem(int position);

    int getCount();
}
