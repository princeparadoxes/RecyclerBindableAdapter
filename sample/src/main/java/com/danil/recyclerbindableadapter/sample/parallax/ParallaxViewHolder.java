package com.danil.recyclerbindableadapter.sample.parallax;

import android.view.View;
import android.widget.TextView;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;
import com.danil.recyclerbindableadapter.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParallaxViewHolder extends BindableViewHolder<String, BindableViewHolder.ActionListener<String>> {
    @BindView(R.id.parallax_example_item_text)
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
