package com.princeparadoxes.danil.recyclerbindableadapter.parallax;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.princeparadoxes.danil.recyclerbindableadapter.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Danil on 07.10.2015.
 */
public class ParallaxViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.parallax_example_item_text)
    TextView text;

    public ParallaxViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String item, int position) {
        text.setText(item);
    }
}
