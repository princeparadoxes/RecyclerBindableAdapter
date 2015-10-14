package com.danil.recyclerbindableadapter.sample.linear;

import android.view.View;

import com.danil.recyclerbindableadapter.library.RecyclerBindableAdapter;
import com.danil.recyclerbindableadapter.sample.R;

/**
 * Created by Danil on 02.10.2015.
 */
public class LinearExampleAdapter extends RecyclerBindableAdapter<Integer, LinearViewHolder> {
    private LinearViewHolder.ActionListener actionListener;

    public LinearExampleAdapter() {
        super();
    }

    @Override
    protected int layoutId(int type) {
        return R.layout.linear_example_item;
    }

    @Override
    protected LinearViewHolder viewHolder(View view, int type) {
        return new LinearViewHolder(view);
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected void onBindItemViewHolder(LinearViewHolder viewHolder, final int position, int type) {
        viewHolder.bindView(getItem(position), position, actionListener);
    }

    public void setActionListener(LinearViewHolder.ActionListener actionListener) {
        this.actionListener = actionListener;
    }

}