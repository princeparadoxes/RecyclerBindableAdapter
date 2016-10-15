package com.danil.recyclerbindableadapter.sample.filter;

import android.view.View;

import com.danil.recyclerbindableadapter.library.FilterBindableAdapter;
import com.danil.recyclerbindableadapter.library.RecyclerBindableAdapter;
import com.danil.recyclerbindableadapter.sample.R;

import java.util.List;

public class FilterExampleAdapter extends RecyclerBindableAdapter<Person, FilterViewHolder> {


    @Override
    protected int layoutId(int type) {
        return R.layout.filter_example_item;
    }

    @Override
    protected void onBindItemViewHolder(FilterViewHolder viewHolder, final int position, int type) {
        viewHolder.bindView(getItem(position));
    }

    @Override
    protected FilterViewHolder viewHolder(View view, int type) {
        return new FilterViewHolder(view);
    }

    @Override
    public void addAll(List<? extends Person> items) {
        clear();
        super.addAll(items);
    }
}