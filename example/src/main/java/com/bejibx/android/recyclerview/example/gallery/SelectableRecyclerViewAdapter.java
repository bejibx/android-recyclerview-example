package com.bejibx.android.recyclerview.example.gallery;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;

import com.bejibx.android.recyclerview.example.R;
import com.bejibx.android.recyclerview.example.gallery.model.Image;
import com.bejibx.android.recyclerview.example.ui.activity.GalleryActivity;
import com.bejibx.android.recyclerview.selection.HolderClickObserver;
import com.bejibx.android.recyclerview.selection.SelectionHelper;
import com.bejibx.android.recyclerview.selection.SelectionObserver;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("Convert2streamapi")
public class SelectableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements HolderClickObserver, SelectionObserver
{
    private final DataSet<Image> mDataSet;
    private final SelectionHelper mSelectionHelper;
    private final GalleryActivity mActivity;

    public SelectableRecyclerViewAdapter(@NonNull GalleryActivity activity, @NonNull DataSet<Image> dataSet)
    {
        mActivity = activity;
        mDataSet = dataSet;
        mSelectionHelper = new SelectionHelper();
        mSelectionHelper.registerSelectionObserver(this);
    }

    public SelectionHelper getSelectionHelper()
    {
        return mSelectionHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ImageViewHolder holder = new ImageViewHolder(
                inflater.inflate(R.layout.gallery_item, viewGroup, false));
        return mSelectionHelper.wrapSelectable(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        Image image = mDataSet.getItem(position);
        ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
        imageViewHolder.bindInfo(image);

        Checkable view = (Checkable) viewHolder.itemView;
        view.setChecked(mSelectionHelper.isItemSelected(position));
        mSelectionHelper.bindHolder(imageViewHolder, position);
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.getCount();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected)
    {
        ((Checkable) holder.itemView).setChecked(isSelected);
    }

    @Override
    public void onSelectableChanged(boolean isSelectable)
    {
        if (isSelectable)
        {
            mActivity.startActionMode();
        }
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder holder)
    {

    }

    @Override
    public boolean onHolderLongClick(RecyclerView.ViewHolder holder)
    {
        return false;
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder
    {
        public Image image;
        public ImageView imageView;

        public ImageViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void bindInfo(Image image)
        {
            this.image = image;

            try {
                // get input stream
                InputStream ims = mActivity.getAssets().open(image.filename);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                imageView.setImageDrawable(d);
            }
            catch (IOException ignored) {}
        }
    }
}
