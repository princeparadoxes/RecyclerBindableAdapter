package com.danil.recyclerbindableadapter.library;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;

import java.util.List;

public abstract class RecyclerBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends FilterAbstractLayer<T, VH> {

    @Override
    public void add(int position, final T item) {
        notFilteredItems.add(position, item);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                int position = getItems().indexOf(item);
                if (position == -1) return;
                position = position + getHeadersCount();
                notifyItemInserted(position);
                int positionStart = position + getHeadersCount();
                int itemCount = getItems().size() - position;
                notifyItemRangeChanged(positionStart, itemCount);
            }
        });
    }

    @Override
    public void add(final T item) {
        notFilteredItems.add(item);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                int position = getItems().indexOf(item);
                if (position == -1) return;
                position = position + getHeadersCount();
                notifyItemInserted(position);
            }
        });
    }

    @Override
    public void addAll(final List<? extends T> items) {
        notFilteredItems.addAll(items);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                for (T item : items) {
                    int position = getItems().indexOf(item);
                    if (position == -1) return;
                    position = position + getHeadersCount();
                    notifyItemInserted(position);
                }
            }
        });
    }

    @Override
    public void addAll(int position, final List<? extends T> items) {
        notFilteredItems.addAll(position, items);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                for (T item : items) {
                    int position = getItems().indexOf(item);
                    if (position == -1) return;
                    position = position + getHeadersCount();
                    notifyItemInserted(position);
                }
            }
        });
    }

    @Override
    public void set(int position, final T item) {
        notFilteredItems.set(position, item);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                int position = getItems().indexOf(item);
                if (position == -1) return;
                position = position + getHeadersCount();
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public void removeChild(int position) {
        final T item = notFilteredItems.get(position);
        final int showedPosition = getHeadersCount() + getItems().indexOf(item);
        notFilteredItems.remove(position);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                int position = getItems().indexOf(item);
                if (position == -1) {
                    notifyItemRemoved(showedPosition);
                    int size = getItemCount() - showedPosition - getFootersCount();
                    notifyItemRangeChanged(showedPosition, size);
                }
            }
        });
    }

    @Override
    public void clear() {
        final int size = getItems().size();
        notFilteredItems.clear();
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                notifyItemRangeRemoved(getHeadersCount(), size);
            }
        });
    }

    @Override
    public void moveChildTo(int fromPosition, int toPosition) {
        final T item = notFilteredItems.remove(fromPosition);
        final int fromPositionShowed = getHeadersCount() + getItems().indexOf(item);
        notFilteredItems.add(toPosition, item);
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                int toPositionShowed = getHeadersCount() + getItems().indexOf(item);
                notifyItemMoved(fromPositionShowed, toPositionShowed);
                int positionStart = fromPositionShowed < toPositionShowed ? fromPositionShowed : toPositionShowed;
                int itemCount = Math.abs(fromPositionShowed - toPositionShowed) + 1;
                notifyItemRangeChanged(positionStart, itemCount);
            }
        });

    }
}