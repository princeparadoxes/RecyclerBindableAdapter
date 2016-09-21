package com.danil.recyclerbindableadapter.library;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;

import com.danil.recyclerbindableadapter.library.filter.BindableAdapterFilter;
import com.danil.recyclerbindableadapter.library.filter.DefaultFilter;
import com.danil.recyclerbindableadapter.library.filter.SupportBindableAdapterFilter;
import com.danil.recyclerbindableadapter.library.filter.SupportDefaultFilter;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerBindableAdapter<T, VH> {

    private final Object lock = new Object();
    private List<T> objects = new ArrayList<T>();
    private ArrayFilter filter = new ArrayFilter();
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

    public ArrayFilter setFilter(BindableAdapterFilter<T> filter) {
        return this.filter.setFilter(filter);
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

    /**
     * Use setFilter(BindableAdapterFilter<T> filter)
     */
    @Deprecated
    protected String itemToString(T item) {
        return null;
    }

    public Filter getFilter() {
        return filter;
    }

    public interface OnFilterObjectCallback {
        void handle(int countFilterObject);
    }

    protected boolean onFilter(String itemString, String filterString) {
        // First match against the whole, non-splitted value
        if (itemString.startsWith(filterString)) {
            return true;
        } else {
            final String[] words = itemString.split(" ");
            final int wordCount = words.length;

            // Start at index 0, in case itemString starts with space(s)
            for (String word : words) {
                if (word.startsWith(filterString)) {
                    return true;
                }
            }
        }
        return false;
    }

    private class ArrayFilter extends Filter {
        private BindableAdapterFilter<T> filter = new DefaultFilter<>();

        private SupportBindableAdapterFilter supportFilter = new SupportDefaultFilter();

        public ArrayFilter setFilter(BindableAdapterFilter<T> filter) {
            this.filter = filter;
            return this;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                ArrayList<T> list;
                synchronized (lock) {
                    list = new ArrayList<T>(getItems());
                }
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<T> values;
                synchronized (lock) {
                    values = new ArrayList<T>(getItems());
                }

                final ArrayList<T> newValues = new ArrayList<T>();
                for (T value : values) {
                    if (itemToString(value) != null) {
                        if (supportFilter.onFilterItem(constraint, itemToString(value))) {
                            newValues.add(value);
                        }
                    } else {
                        if (filter == null) {
                            throw new RuntimeException("filter must not be null");
                        }
                        if (filter.onFilterItem(constraint, value)) {
                            newValues.add(value);
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
