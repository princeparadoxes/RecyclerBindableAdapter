package com.princeparadoxes.danil.recyclerbindableadapter.parallax;

import android.view.View;

import com.princeparadoxes.danil.recyclerbindableadapter.ParallaxRecyclerBindableAdapter;
import com.princeparadoxes.danil.recyclerbindableadapter.R;

/**
 * Created by Danil on 02.10.2015.
 */
public class ParallaxExampleAdapter extends ParallaxRecyclerBindableAdapter<String, ParallaxViewHolder> {

    public ParallaxExampleAdapter() {
        super();
    }

    @Override
    protected int layoutId(int type) {
        return R.layout.parallax_example_item;
    }

    @Override
    protected ParallaxViewHolder viewHolder(View view, int type) {
        return new ParallaxViewHolder(view);
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected void onBindItemViewHolder(ParallaxViewHolder viewHolder, final int position, int type) {
        viewHolder.bindView(getItem(position), position);
    }
}