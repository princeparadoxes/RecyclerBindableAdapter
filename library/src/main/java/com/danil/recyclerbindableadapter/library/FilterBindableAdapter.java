package com.danil.recyclerbindableadapter.library;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil on 13.10.2015.
 */
public abstract class FilterBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerBindableAdapter<T, VH> {

    private final Object lock = new Object();
    private List<T> objects = new ArrayList<T>();
    private ArrayFilter filter;
    private OnFilterObjectCallback onFilterObjectCallback;

    @Override
    public void addAll(List<? extends T> data) {
        if (objects.containsAll(data)) {
            return;
        }
        objects.clear();
        objects.addAll(data);
        getItems().clear();
        getItems().addAll(data);
        notifyItemRangeInserted(getHeadersCount(), data.size());
    }

    public void addShowed(List<? extends T> data) {
        objects.clear();
        objects.addAll(data);
        notifyItemRangeInserted(getHeadersCount(), data.size());
    }

    @Override
    public void removeChild(int position) {
        T item = getItems().remove(position);
        objects.remove(item);
        notifyItemRemoved(position + getHeadersCount());
        int positionStart = position + getHeadersCount();
        int itemCount = objects.size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
    }

    public void setOnFilterObjectCallback(OnFilterObjectCallback objectCallback) {
        onFilterObjectCallback = objectCallback;
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getRealItemCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract String itemToString(T item);

    public Filter getFilter() {
        if (filter == null) {
            filter = new ArrayFilter();
        }
        return filter;
    }

    public interface OnFilterObjectCallback {
        void handle(int countFilterObject);
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list;
                synchronized (lock) {
                    list = new ArrayList<T>(getItems());
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<T> values;
                synchronized (lock) {
                    values = new ArrayList<T>(getItems());
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = itemToString(value).toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.contains(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            objects = (List<T>) results.values;
            if (onFilterObjectCallback != null) {
                onFilterObjectCallback.handle(results.count);
            }
            notifyDataSetChanged();
        }
    }
}
