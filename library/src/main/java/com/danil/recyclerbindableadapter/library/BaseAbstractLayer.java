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
    private static final String P_ITEMS = "RecyclerBindableAdapter.items";

    private ArrayList<T> items = new ArrayList<>();
    protected LayoutInflater inflater;

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

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (inflater == null) {
            this.inflater = LayoutInflater.from(recyclerView.getContext());
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (items.size() > 0 && (items.get(0) instanceof Parcelable
                || items.get(0) instanceof Serializable)) {
            bundle.putSerializable(P_ITEMS, items);
        }
        return bundle;
    }

    @SuppressWarnings("unchecked")
    public void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.containsKey(P_ITEMS)) {
                items = (ArrayList<T>) bundle.getSerializable(P_ITEMS);
            }
        }
    }
}
