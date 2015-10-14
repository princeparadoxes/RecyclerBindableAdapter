package com.danil.recyclerbindableadapter.sample.grid;

import android.view.View;

import com.danil.recyclerbindableadapter.library.RecyclerBindableAdapter;
import com.danil.recyclerbindableadapter.sample.R;

/**
 * Created by Danil on 02.10.2015.
 */
public class GridExampleAdapter extends RecyclerBindableAdapter<GridExampleItem, GridViewHolder> {
    public final static int FIRST_TYPE = 0;
    public final static int SECOND_TYPE = 1;

    private GridViewHolder.ActionListener actionListener;

    public GridExampleAdapter() {
        super();
    }

    @Override
    protected int layoutId(int type) {
        if (type == FIRST_TYPE) {
            return R.layout.grid_example_first_type_item;
        } else {
            return R.layout.grid_example_second_type_item;
        }
    }

    @Override
    protected GridViewHolder viewHolder(View view, int type) {
        return new GridViewHolder(view);
    }

    @Override
    protected int getItemType(int position) {
        return getItem(position).getType();
    }

    @Override
    protected void onBindItemViewHolder(GridViewHolder viewHolder, final int position, int type) {
        if (type == FIRST_TYPE) {
            viewHolder.bindViewFirstType(getItem(position), position, actionListener);
        } else if (type == SECOND_TYPE) {
            viewHolder.bindViewSecondType(getItem(position), position, actionListener);
        }
    }

    @Override
    protected int getGridSpan(int position) {
        if(SECOND_TYPE != getItemType(position)) {
            return super.getGridSpan(position);
        } else {
            return getMaxGridSpan();
        }
    }

    public void setActionListener(GridViewHolder.ActionListener actionListener) {
        this.actionListener = actionListener;
    }

}