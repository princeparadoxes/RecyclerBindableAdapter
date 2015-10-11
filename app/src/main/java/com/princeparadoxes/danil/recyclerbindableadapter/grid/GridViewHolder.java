package com.princeparadoxes.danil.recyclerbindableadapter.grid;

import android.support.annotation.Nullable;
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

    public void bindViewFirstType(GridExampleItem item, int position, ActionListener listener) {
        actionListener = listener;
        this.position = position;
        tittle.setText(String.valueOf(item.getValue()));
    }

    @Nullable
    @OnClick(R.id.grid_example_item_move_to_top)
    protected void OnMoveToTopClick() {
        if (actionListener != null) {
            actionListener.onMoveToTop(position);
        }
    }

    @Nullable
    @OnClick(R.id.grid_example_item_remove)
    protected void OnRemoveClick() {
        if (actionListener != null) {
            actionListener.OnRemove(position);
        }
    }

    @Nullable
    @OnClick(R.id.grid_example_item_up)
    protected void OnUpClick() {
        if (actionListener != null) {
            actionListener.OnUp(position);
        }
    }

    @Nullable
    @OnClick(R.id.grid_example_item_down)
    protected void OnDownClick() {
        if (actionListener != null) {
            actionListener.OnDown(position);
        }
    }

    public void bindViewSecondType(GridExampleItem item, int position, ActionListener listener) {
        actionListener = listener;
        this.position = position;
        tittle.setText(String.valueOf(item.getValue()));
    }

    @Nullable
    @OnClick(R.id.grid_example_item_add_to)
    protected void OnAddToClick() {
        if (actionListener != null) {
            actionListener.OnAddTo(position);
        }
    }

    @Nullable
    @OnClick(R.id.grid_example_item_set)
    protected void OnSetClick() {
        if (actionListener != null) {
            actionListener.OnSet(position);
        }
    }

    public interface ActionListener {
        void onMoveToTop(int position);

        void OnRemove(int position);

        void OnUp(int position);

        void OnDown(int position);

        void OnAddTo(int position);

        void OnSet(int position);
    }
}
