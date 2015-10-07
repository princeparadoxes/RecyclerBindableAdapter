package com.princeparadoxes.danil.recyclerbindableadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerViewHeaderFooterAdapter<T, VH> {

    private LayoutInflater inflater;
    private List<T> items = new ArrayList<>();

    public RecyclerBindableAdapter() {
        super();
    }

    @Override
    public int getRealItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    //@TODO test
    public void add(int position, T item) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void add(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1 + getHeadersCount());
    }

    public void addAll(List<? extends T> items) {
        final int size = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(size + getHeadersCount(), items.size());
    }

    //@TODO test
    public void set(int position, T item) {
        items.set(position, item);
        notifyItemInserted(position + getHeadersCount());
    }

    public void removeChild(int position) {
        items.remove(position);
        notifyItemRemoved(position + getHeadersCount());
        int positionStart = position + getHeadersCount();
        int itemCount = items.size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
    }

    public void clear() {
        final int size = items.size();
        items.clear();
        notifyItemRangeRemoved(getHeadersCount(), size);
    }

    public void moveChildTo(int fromPosition, int toPosition) {
        if (toPosition != -1 && toPosition < items.size()) {
            final T item = items.remove(fromPosition);
            items.add(toPosition, item);
            notifyItemMoved(getHeadersCount() + fromPosition, getHeadersCount() + toPosition);
            int positionStart = fromPosition < toPosition ? fromPosition : toPosition;
            int itemCount = Math.abs(fromPosition - toPosition) + 1;
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }
    }

    @Override
    protected VH onCreteItemViewHolder(ViewGroup parent, int type) {
        if (inflater == null) {
            this.inflater = LayoutInflater.from(parent.getContext());
        }
        return viewHolder(inflater.inflate(layoutId(type), parent, false), type);
    }

    protected abstract
    @LayoutRes
    int layoutId(int type);

    protected abstract VH viewHolder(View view, int type);

}
