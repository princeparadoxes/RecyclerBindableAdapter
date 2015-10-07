package com.princeparadoxes.danil.recyclerbindableadapter.grid;

import android.view.View;

import com.princeparadoxes.danil.recyclerbindableadapter.MainViewHolder;
import com.princeparadoxes.danil.recyclerbindableadapter.R;
import com.princeparadoxes.danil.recyclerbindableadapter.RecyclerBindableAdapter;

/**
 * Created by Danil on 02.10.2015.
 */
public class GridExampleAdapter extends RecyclerBindableAdapter<Integer, MainViewHolder> {
    private MainViewHolder.ActionListener actionListener;

    public GridExampleAdapter() {
        super();
    }

    @Override
    protected int layoutId(int type) {
        return R.layout.grid_example_item;
    }

    @Override
    protected MainViewHolder viewHolder(View view, int type) {
        return new MainViewHolder(view);
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected void onBindItemViewHolder(MainViewHolder viewHolder, final int position, int type) {
        viewHolder.bindView(getItem(position), position, actionListener);
    }

    public void setActionListener(MainViewHolder.ActionListener actionListener) {
        this.actionListener = actionListener;
    }

}