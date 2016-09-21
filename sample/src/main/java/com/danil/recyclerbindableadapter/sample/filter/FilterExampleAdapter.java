package com.danil.recyclerbindableadapter.sample.filter;

import android.view.View;

import com.danil.recyclerbindableadapter.library.FilterBindableAdapter;
import com.danil.recyclerbindableadapter.sample.R;

public class FilterExampleAdapter extends FilterBindableAdapter<Person, FilterViewHolder> {


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
}