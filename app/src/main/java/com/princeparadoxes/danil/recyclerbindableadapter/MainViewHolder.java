package com.princeparadoxes.danil.recyclerbindableadapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Danil on 07.10.2015.
 */
public class MainViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.linear_example_item_move_to_top)
    View moveToTop;
    @Bind(R.id.linear_example_item_remove)
    View remove;
    @Bind(R.id.linear_example_item_up)
    View up;
    @Bind(R.id.linear_example_item_down)
    View down;
    @Bind(R.id.linear_example_item_text)
    TextView text;

    public MainViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindView(Integer item, int position, ActionListener listener) {
        moveToTop.setOnClickListener(new OnClick(OnClick.MOVE_TO_TOP, listener, position));
        remove.setOnClickListener(new OnClick(OnClick.REMOVE, listener, position));
        up.setOnClickListener(new OnClick(OnClick.UP, listener, position));
        down.setOnClickListener(new OnClick(OnClick.DOWN, listener, position));
        text.setText(String.valueOf(item));
    }

    public interface ActionListener {
        void onMoveToTop(int position);

        void remove(int position);

        void up(int position);

        void down(int position);
    }

    private static class OnClick implements View.OnClickListener {
        static final int MOVE_TO_TOP = 0;
        static final int REMOVE = 1;
        static final int UP = 2;
        static final int DOWN = 3;

        private int type;
        private ActionListener actionListener;
        private int position;

        public OnClick(@OnClickType int type, ActionListener listener, int position) {
            this.type = type;
            this.actionListener = listener;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case MOVE_TO_TOP:
                    actionListener.onMoveToTop(position);
                    break;
                case REMOVE:
                    actionListener.remove(position);
                    break;
                case UP:
                    actionListener.up(position);
                    break;
                case DOWN:
                    actionListener.down(position);
                    break;
                default:
                    break;
            }
        }

        @IntDef({MOVE_TO_TOP, REMOVE, UP, DOWN})
        public @interface OnClickType {
        }
    }
}
