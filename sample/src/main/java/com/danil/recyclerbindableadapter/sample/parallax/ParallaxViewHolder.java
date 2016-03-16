package com.danil.recyclerbindableadapter.sample.parallax;

import android.view.View;
import android.widget.TextView;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;
import com.danil.recyclerbindableadapter.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Danil on 07.10.2015.
 */
public class ParallaxViewHolder extends BindableViewHolder<String, BindableViewHolder.ActionListener<String>> {
    @Bind(R.id.parallax_example_item_text)
    TextView text;

    public ParallaxViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView(int position, String item, ActionListener<String> actionListener) {
        super.bindView(position, item, actionListener);
        text.setText(item);
    }
}
