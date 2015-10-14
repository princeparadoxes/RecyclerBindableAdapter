package com.danil.recyclerbindableadapter.library.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Danil on 08.10.2015.
 */
public abstract class BindableViewHolder<T> extends RecyclerView.ViewHolder {

    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    public void bindView(final int position, final T item, final ActionListener actionListener) {
        if (actionListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.OnItemClickListener(position, item);
                }
            });
        }
    }

    public interface ActionListener {
        void OnItemClickListener(int position, Object Item);
    }
}
