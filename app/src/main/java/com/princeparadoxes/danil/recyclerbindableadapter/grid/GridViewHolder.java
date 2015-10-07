package com.princeparadoxes.danil.recyclerbindableadapter.grid;

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
public class GridViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.grid_example_item_tittle)
    TextView tittle;

    private int position;
    private ActionListener actionListener;

    public GridViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(Integer item, int position, ActionListener listener) {
        actionListener = listener;
        this.position = position;
        tittle.setText(String.valueOf(item));
    }

    @OnClick(R.id.grid_example_item_move_to_top)
    protected void OnMoveToTopClick() {
        if (actionListener != null) {
            actionListener.onMoveToTop(position);
        }
    }

    @OnClick(R.id.grid_example_item_remove)
    protected void OnRemoveClick() {
        if (actionListener != null) {
            actionListener.OnRemove(position);
        }
    }

    @OnClick(R.id.grid_example_item_up)
    protected void OnUpClick() {
        if (actionListener != null) {
            actionListener.OnUp(position);
        }
    }

    @OnClick(R.id.grid_example_item_down)
    protected void OnDownClick() {
        if (actionListener != null) {
            actionListener.OnDown(position);
        }
    }

    public interface ActionListener {
        void onMoveToTop(int position);

        void OnRemove(int position);

        void OnUp(int position);

        void OnDown(int position);
    }
}
