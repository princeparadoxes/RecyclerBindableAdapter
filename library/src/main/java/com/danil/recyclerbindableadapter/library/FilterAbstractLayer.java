package com.danil.recyclerbindableadapter.library;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;

import com.danil.recyclerbindableadapter.library.filter.BindableAdapterFilter;
import com.danil.recyclerbindableadapter.library.filter.DefaultFilter;

import java.util.ArrayList;
import java.util.List;

abstract class FilterAbstractLayer<T, VH extends RecyclerView.ViewHolder>
        extends HeaderFooterAbstractLayer<T, VH> {

    protected final Object notFilteredItemsLock = new Object();
    protected List<T> notFilteredItems = new ArrayList<T>();
    private ArrayFilter filter = new ArrayFilter();
    protected CharSequence constraint = "";

    protected void notifyChanged() {
        filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                notifyDataSetChanged();
            }
        });
    }

    public List<T> getNotFilteredItems() {
        return notFilteredItems;
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
        this.filter(constraint, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                notifyDataSetChanged();
            }
        });
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

    protected class ArrayFilter extends Filter {
        private BindableAdapterFilter<T> filter = new DefaultFilter<>();


        public ArrayFilter setFilter(BindableAdapterFilter<T> filter) {
            this.filter = filter;
            return this;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<T> values;
            synchronized (notFilteredItemsLock) {
                values = new ArrayList<T>(getNotFilteredItems());
            }
            if (constraint == null || constraint.length() == 0) {
                results.values = values;
                results.count = values.size();
            } else {


                final ArrayList<T> newValues = new ArrayList<T>();
                for (T value : values) {
                    if (filter == null) {
                        throw new RuntimeException("filter must not be null");
                    }
                    if (filter.onFilterItem(constraint, value)) {
                        newValues.add(value);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            getItems().clear();
            //noinspection unchecked
            List<T> resultList = (List<T>) results.values;
            getItems().addAll(resultList);
            notifyDataSetChanged();
//            notifyItemRangeInserted(0, resultList.size());
        }
    }
}
