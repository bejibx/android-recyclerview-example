package com.bejibx.android.recyclerview.example.gallery;

import com.bejibx.android.recyclerview.example.gallery.model.Image;

import java.util.ArrayList;
import java.util.List;

public class DummyImagesDataSet implements DataSet<Image>
{
    private List<Image> mImages = new ArrayList<>();

    public DummyImagesDataSet()
    {
        for (int i = 0; i < 30; i++)
        {
            Image image = new Image();
            image.filename = "ic_person.png";
            mImages.add(image);
        }
    }

    @Override
    public Image getItem(int position)
    {
        return mImages.get(position);
    }

    @Override
    public int getCount()
    {
        return mImages.size();
    }

}
