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
    private List<T> dataset = new ArrayList<>();

    public RecyclerBindableAdapter(Context context, RecyclerView.LayoutManager manager) {
        super(manager);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getRealItemCount() {
        return dataset.size();
    }

    public T getItem(int position) {
        return dataset.get(position);
    }

    public void add(int position, T item) {
        dataset.add(position, item);
        notifyItemInserted(position);
    }

    public void add(T item) {
        dataset.add(item);
        notifyItemInserted(dataset.size() - 1 + getHeadersCount());
    }

    public void addAll(List<? extends T> items) {
        final int size = dataset.size();
        dataset.addAll(items);
        notifyItemRangeInserted(size + getHeadersCount(), items.size());
    }

    public void set(int position, T item) {
        dataset.set(position, item);
        notifyItemInserted(position + getHeadersCount());
    }

    public void removeChild(int position) {
        dataset.remove(position);
        notifyItemRemoved(position + getHeadersCount());
        int positionStart = position + getHeadersCount();
        int itemCount = dataset.size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
    }

    public void clear() {
        final int size = dataset.size();
        dataset.clear();
        notifyItemRangeRemoved(getHeadersCount(), size);
    }

    public void moveChildTo(int fromPosition, int toPosition) {
        if (toPosition != -1 && toPosition < dataset.size()) {
            final T item = dataset.remove(fromPosition);
            dataset.add(toPosition, item);
            notifyItemMoved(getHeadersCount() + fromPosition, getHeadersCount() + toPosition);
            int positionStart = fromPosition < toPosition ? fromPosition : toPosition;
            int itemCount = Math.abs(fromPosition - toPosition) + 1;
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }
    }

    @Override
    protected VH onCreteItemViewHolder(ViewGroup parent, int type) {
        return viewHolder(inflater.inflate(layoutId(type), parent, false), type);
    }

    protected abstract
    @LayoutRes
    int layoutId(int type);

    protected abstract VH viewHolder(View view, int type);

}
