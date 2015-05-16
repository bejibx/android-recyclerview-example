package com.bejibx.android.recyclerview.example.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bejibx.android.recyclerview.example.R;
import com.bejibx.android.recyclerview.example.gallery.DataSet;
import com.bejibx.android.recyclerview.example.gallery.DummyImagesDataSet;
import com.bejibx.android.recyclerview.example.gallery.SelectableRecyclerViewAdapter;
import com.bejibx.android.recyclerview.example.gallery.model.Image;
import com.bejibx.android.recyclerview.decoration.GridSimpleSpacingDecoration;
import com.bejibx.android.recyclerview.layoutmanager.GridAutofitLayoutManager;
import com.bejibx.android.recyclerview.selection.SelectionHelper;
import com.bejibx.android.recyclerview.selection.SelectionObserver;


public class GalleryActivity extends Activity
{
    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();

    private SelectableRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        int columnWidth = getResources().getDimensionPixelSize(R.dimen.column_width);
        int vSpacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing_vertical);
        int hSpacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing_horizontal);
        recyclerView.setLayoutManager(new GridAutofitLayoutManager(this, columnWidth));
        recyclerView.addItemDecoration(new GridSimpleSpacingDecoration(hSpacing, vSpacing));
        DataSet<Image> dataSet = new DummyImagesDataSet();
        mAdapter = new SelectableRecyclerViewAdapter(this, dataSet);
        recyclerView.setAdapter(mAdapter);
    }

    public void startActionMode()
    {
        startActionMode(mActionModeCallback);
    }

    private class ActionModeCallback implements ActionMode.Callback, SelectionObserver
    {
        private ActionMode mActionMode;

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu)
        {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode)
        {
            SelectionHelper selectionHelper = mAdapter.getSelectionHelper();
            selectionHelper.unregisterSelectionObserver(this);
            mActionMode = null;
            selectionHelper.setSelectable(false);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu)
        {
            mActionMode = actionMode;
            mActionMode.getMenuInflater().inflate(R.menu.gallery_selection, menu);
            mAdapter.getSelectionHelper().registerSelectionObserver(this);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem)
        {
            switch (menuItem.getItemId())
            {
                case R.id.menu_toast:
                    Toast.makeText(GalleryActivity.this,
                            R.string.text_simple_toast, Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected)
        {
            if (mActionMode != null)
            {
                int checkedImagesCount = mAdapter.getSelectionHelper().getSelectedItemsCount();
                mActionMode.setTitle(String.valueOf(checkedImagesCount));
            }
        }

        @Override
        public void onSelectableChanged(boolean isSelectable)
        {
            if (!isSelectable)
            {
               mActionMode.finish();
            }
        }
    }
}
