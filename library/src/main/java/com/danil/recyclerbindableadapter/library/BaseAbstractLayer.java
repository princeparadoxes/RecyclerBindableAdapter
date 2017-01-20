package com.danil.recyclerbindableadapter.library;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract class BaseAbstractLayer<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  CONSTANTS  //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Key for save items to Parcelable and restore them from Parcelable
     */
    private static final String KEY_ITEMS = "RecyclerBindableAdapter.items";

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  FIELDS  /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * List of showed items
     */
    private ArrayList<T> items = new ArrayList<>();
    /**
     * Layout inflater need for inflate view
     */
    protected LayoutInflater inflater;

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setLayoutInflaterIfNeeded(recyclerView);
    }

    /**
     * Set inflater field if it equals null.
     * Get LayoutInflater from recyclerView context
     *
     * @param recyclerView
     */
    private void setLayoutInflaterIfNeeded(RecyclerView recyclerView) {
        if (inflater == null) {
            this.inflater = LayoutInflater.from(recyclerView.getContext());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  ABSTRACT METHODS  ///////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a item to end of showed list
     *
     * @param item - item to be added
     */
    public abstract void add(T item);

    /**
     * Add a item to specific position
     *
     * @param position - specific position
     * @param item     - item to be added
     */
    public abstract void add(int position, T item);

    /**
     * Add a list of items to end of showed list
     *
     * @param items - list to be added
     */
    public abstract void addAll(List<? extends T> items);

    /**
     * Add a list of items to specific position
     *
     * @param position - specific position
     * @param items    - list to be added
     */
    public abstract void addAll(int position, List<? extends T> items);

    /**
     * Replace a item on specific position
     *
     * @param position - specific position
     * @param item     - item to be added
     */
    public abstract void set(int position, T item);

    /**
     * Remove a item from specific position
     *
     * @param position - specific position
     */
    public abstract void removeChild(int position);

    /**
     * Remove all items
     */
    public abstract void clear();

    /**
     * Move a item from 'fromPosition' to 'toPosition'
     *
     * @param fromPosition - position from which be moved item
     * @param toPosition   - position to which be moved item
     */
    public abstract void moveChildTo(int fromPosition, int toPosition);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  GETTERS  ////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public int getRealItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public int indexOf(T object) {
        return items.indexOf(object);
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  SAVE/RESTORE  ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (items.size() > 0 && (items.get(0) instanceof Parcelable
                || items.get(0) instanceof Serializable)) {
            bundle.putSerializable(KEY_ITEMS, items);
        }
        return bundle;
    }

    @SuppressWarnings("unchecked")
    public void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.containsKey(KEY_ITEMS)) {
                items = (ArrayList<T>) bundle.getSerializable(KEY_ITEMS);
            }
        }
    }
}
