package com.danil.recyclerbindableadapter.library;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    public static final int TYPE_HEADER = 7898;
    public static final int TYPE_FOOTER = 7899;
    private static final String P_ITEMS = "RecyclerBindableAdapter.items";
    private static final String P_HEADERS = "RecyclerBindableAdapter.headers";
    private static final String P_FOOTERS = "RecyclerBindableAdapter.footers";

    private ArrayList<View> headers = new ArrayList<>();
    private ArrayList<View> footers = new ArrayList<>();
    private ArrayList<T> items = new ArrayList<>();

    private RecyclerView.LayoutManager manager;
    private LayoutInflater inflater;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return getGridSpan(position);
        }
    };

    public int getRealItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void add(int position, T item) {
        items.add(position, item);
        notifyItemInserted(position);
        int positionStart = position + getHeadersCount();
        int itemCount = items.size() - position;
        notifyItemRangeChanged(positionStart, itemCount);
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

    public void addAll(int position, List<? extends T> items) {
        this.items.addAll(position, items);
        notifyItemRangeInserted(position + getHeadersCount(), items.size());
    }

    public void set(int position, T item) {
        items.set(position, item);
        notifyItemChanged(position + getHeadersCount());
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

    public int indexOf(T object) {
        return items.indexOf(object);
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            return (VH) onCreateItemViewHolder(viewGroup, type);
            //else we have a header/footer
        } else {
            //create a new framelayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            //make sure it fills the space
            setHeaderFooterLayoutParams(frameLayout);
            return (VH) new HeaderFooterViewHolder(frameLayout);
        }
    }

    protected void setHeaderFooterLayoutParams(ViewGroup viewGroup) {
        ViewGroup.LayoutParams layoutParams;
        if (manager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) manager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        } else {
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        viewGroup.setLayoutParams(layoutParams);
    }

    @Override
    final public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        //check what type of view our position is
        if (isHeader(position)) {
            View v = headers.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (isFooter(position)) {
            View v = footers.get(position - getRealItemCount() - getHeadersCount());
            //add our view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else {
            //it's one of our items, display as required
            onBindItemViewHolder((VH) vh, position - headers.size(), getItemType(position));
        }
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder vh, View view) {
        //if it's a staggered grid, span the whole layout
        if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            vh.itemView.setLayoutParams(layoutParams);
        }
        //if the view already belongs to another layout, remove it
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        //empty out our FrameLayout and replace with our header/footer
        ((ViewGroup) vh.itemView).removeAllViews();
        ((ViewGroup) vh.itemView).addView(view);
    }

    private boolean isHeader(int position) {
        return (position < headers.size());
    }

    private boolean isFooter(int position) {
        return footers.size() > 0 && (position >= getHeadersCount() + getRealItemCount());
    }

    protected VH onCreateItemViewHolder(ViewGroup parent, int type) {
        return viewHolder(inflater.inflate(layoutId(type), parent, false), type);
    }

    @Override
    public int getItemCount() {
        return headers.size() + getRealItemCount() + footers.size();
    }

    @Override
    final public int getItemViewType(int position) {
        //check what type our position is, based on the assumption that the order is headers > items > footers
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        int type = getItemType(position - getHeadersCount());
        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            throw new IllegalArgumentException("Item type cannot equal " + TYPE_HEADER + " or " + TYPE_FOOTER);
        }
        return type;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (manager == null) {
            setManager(recyclerView.getLayoutManager());
        }
        if (inflater == null) {
            this.inflater = LayoutInflater.from(recyclerView.getContext());
        }
    }

    private void setManager(RecyclerView.LayoutManager manager) {
        this.manager = manager;
        if (this.manager instanceof GridLayoutManager) {
            ((GridLayoutManager) this.manager).setSpanSizeLookup(spanSizeLookup);
        } else if (this.manager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) this.manager).setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        }
    }

    protected int getGridSpan(int position) {
        if (isHeader(position) || isFooter(position)) {
            return getMaxGridSpan();
        }
        position -= headers.size();
        if (getItem(position) instanceof SpanItemInterface) {
            return ((SpanItemInterface) getItem(position)).getGridSpan();
        }
        return 1;
    }

    protected int getMaxGridSpan() {
        if (manager instanceof GridLayoutManager) {
            return ((GridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) manager).getSpanCount();
        }
        return 1;
    }

    //add a header to the adapter
    public void addHeader(View header) {
        if (!headers.contains(header)) {
            headers.add(header);
            //animate
            notifyItemInserted(headers.size() - 1);
        }
    }

    //remove header from adapter
    public void removeHeader(View header) {
        if (headers.contains(header)) {
            //animate
            notifyItemRemoved(headers.indexOf(header));
            headers.remove(header);
        }
    }

    //add a footer to the adapter
    public void addFooter(View footer) {
        if (!footers.contains(footer)) {
            footers.add(footer);
            //animate
            notifyItemInserted(headers.size() + getItemCount() + footers.size() - 1);
        }
    }

    //remove footer from adapter
    public void removeFooter(View footer) {
        if (footers.contains(footer)) {
            //animate
            notifyItemRemoved(headers.size() + getItemCount() + footers.indexOf(footer));
            footers.remove(footer);
        }
    }

    public int getHeadersCount() {
        return headers.size();
    }

    public View getHeader(int location) {
        return headers.get(location);
    }

    public int getFootersCount() {
        return footers.size();
    }

    public View getFooter(int location) {
        return footers.get(location);
    }

    protected int getItemType(int position) {
        return 0;
    }

    abstract protected void onBindItemViewHolder(VH viewHolder, int position, int type);

    protected abstract VH viewHolder(View view, int type);

    protected abstract
    @LayoutRes
    int layoutId(int type);

    public interface SpanItemInterface {
        int getGridSpan();
    }

    //our header/footer RecyclerView.ViewHolder is just a FrameLayout
    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
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