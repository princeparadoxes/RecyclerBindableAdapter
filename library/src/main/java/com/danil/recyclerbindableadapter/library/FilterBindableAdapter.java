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
    private CharSequence constraint = "";

    @Override
    public void addAll(List<? extends T> data) {
        if (objects.containsAll(data)) {
            return;
        }
        objects.clear();
        objects.addAll(data);
        getItems().clear();
        getItems().addAll(data);
        if (constraint == null || constraint.equals("")) {
            notifyDataSetChanged();
        } else {
            filter(constraint, new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int count) {
                    notifyDataSetChanged();
                }
            });
        }
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


    public ArrayFilter setFilter(BindableAdapterFilter<T> filter) {
        return this.filter.setFilter(filter);
    }

    /**
     * Delegate Filter method
     * <p>
     * <p>Converts a value from the filtered set into a CharSequence. Subclasses
     * should override this method to convert their results. The default
     * implementation returns an empty String for null values or the default
     * String representation of the value.</p>
     *
     * @param resultValue the value to convert to a CharSequence
     * @return a CharSequence representing the value
     */
    public CharSequence convertResultToString(Object resultValue) {
        return filter.convertResultToString(resultValue);
    }

    /**
     * Delegate Filter method
     * <p>
     * Saves contrait to field for filter on add new items
     *
     * @param constraint the constraint used to filter the data
     * @see #addAll
     * <p>
     * <p>Starts an asynchronous filtering operation. Calling this method
     * cancels all previous non-executed filtering requests and posts a new
     * filtering request that will be executed later.</p>
     * @see #filter(CharSequence, android.widget.Filter.FilterListener)
     */
    public void filter(CharSequence constraint) {
        this.constraint = constraint;
        filter.filter(constraint);
    }

    /**
     * Delegate Filter method
     * <p>
     * <p>Starts an asynchronous filtering operation. Calling this method
     * cancels all previous non-executed filtering requests and posts a new
     * filtering request that will be executed later.</p>
     * <p>
     * <p>Upon completion, the listener is notified.</p>
     *
     * @param constraint the constraint used to filter the data
     * @param listener   a listener notified upon completion of the operation
     * @see #filter(CharSequence)
     */
    public void filter(CharSequence constraint, Filter.FilterListener listener) {
        this.constraint = constraint;
        filter.filter(constraint, listener);
    }

    /**
     * Deprecated
     * Use setFilter(BindableAdapterFilter<T> filter)
     * <p>
     * Override this method for convert you item to String.
     * This string will be used for filter your items
     */
    @Deprecated
    protected String itemToString(T item) {
        return null;
    }


    /**
     * Deprecated
     * Use delegates methods:
     *
     * @return a Filter object
     * @see #convertResultToString(Object resultValue)
     * @see #filter(CharSequence constraint)
     * @see #filter(CharSequence constraint, Filter.FilterListener listener)
     */
    @Deprecated
    public Filter getFilter() {
        return filter;
    }

    /**
     * Deprecated
     * Use delegates methods:
     *
     * @see #filter(CharSequence constraint, Filter.FilterListener listener)
     * @see Filter.FilterListener
     */
    @Deprecated
    public interface OnFilterObjectCallback {
        void handle(int countFilterObject);
    }

    /**
     * Deprecated
     * Use delegates methods:
     *
     * @see #filter(CharSequence constraint, Filter.FilterListener listener)
     * @see Filter.FilterListener
     */
    @Deprecated
    public void setOnFilterObjectCallback(OnFilterObjectCallback objectCallback) {
        onFilterObjectCallback = objectCallback;
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
