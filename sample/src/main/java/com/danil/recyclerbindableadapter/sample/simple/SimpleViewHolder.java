package com.danil.recyclerbindableadapter.sample.simple;

import android.view.View;
import android.widget.TextView;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;
import com.danil.recyclerbindableadapter.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleViewHolder extends BindableViewHolder<Integer, SimpleViewHolder.SimpleActionListener> {

    @Bind(R.id.simple_example_item_tittle)
    TextView tittle;

    private int position;
    private SimpleActionListener simpleActionListener;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView(int position, Integer item, SimpleActionListener actionListener) {
        super.bindView(position, item, actionListener);
        this.position = position;
        simpleActionListener = actionListener;
        tittle.setText(String.valueOf(item));
    }

    @OnClick(R.id.simple_example_item_move_to_top)
    protected void OnMoveToTopClick() {
        if (simpleActionListener != null) {
            simpleActionListener.onMoveToTop(position);
        }
    }

    @OnClick(R.id.simple_example_item_remove)
    protected void OnRemoveClick() {
        if (simpleActionListener != null) {
            simpleActionListener.OnRemove(position);
        }
    }

    @OnClick(R.id.simple_example_item_up)
    protected void OnUpClick() {
        if (simpleActionListener != null) {
            simpleActionListener.OnUp(position);
        }
    }

    @OnClick(R.id.simple_example_item_down)
    protected void OnDownClick() {
        if (simpleActionListener != null) {
            simpleActionListener.OnDown(position);
        }
    }

    public interface SimpleActionListener extends BindableViewHolder.ActionListener<Integer> {
        void onMoveToTop(int position);

        void OnRemove(int position);

        void OnUp(int position);

        void OnDown(int position);
    }
}
