package com.danil.recyclerbindableadapter.library;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class RecyclerBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends HeaderFooterAbstractLayer<T, VH> {

    public void add(int position, T item) {
        getItems().add(position, item);
        notifyItemInserted(position);
        int positionStart = position + getHeadersCount();
        int itemCount = getItems().size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
    }

    public void add(T item) {
        getItems().add(item);
        notifyItemInserted(getItems().size() - 1 + getHeadersCount());
    }

    public void addAll(List<? extends T> items) {
        final int size = getItems().size();
        getItems().addAll(items);
        notifyItemRangeInserted(size + getHeadersCount(), items.size());
    }

    public void addAll(int position, List<? extends T> items) {
        getItems().addAll(position, items);
        notifyItemRangeInserted(position + getHeadersCount(), items.size());
    }

    public void set(int position, T item) {
        getItems().set(position, item);
        notifyItemChanged(position + getHeadersCount());
    }

    public void removeChild(int position) {
        getItems().remove(position);
        notifyItemRemoved(position + getHeadersCount());
        int positionStart = position + getHeadersCount();
        int itemCount = getItems().size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
    }

    public void clear() {
        final int size = getItems().size();
        getItems().clear();
        notifyItemRangeRemoved(getHeadersCount(), size);
    }

    public void moveChildTo(int fromPosition, int toPosition) {
        if (toPosition != -1 && toPosition < getItems().size()) {
            final T item = getItems().remove(fromPosition);
            getItems().add(toPosition, item);
            notifyItemMoved(getHeadersCount() + fromPosition, getHeadersCount() + toPosition);
            int positionStart = fromPosition < toPosition ? fromPosition : toPosition;
            int itemCount = Math.abs(fromPosition - toPosition) + 1;
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }
    }
}