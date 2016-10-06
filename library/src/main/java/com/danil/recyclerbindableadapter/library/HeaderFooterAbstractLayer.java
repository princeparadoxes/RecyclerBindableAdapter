package com.danil.recyclerbindableadapter.library;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


abstract class HeaderFooterAbstractLayer<T, VH extends RecyclerView.ViewHolder>
        extends BaseAbstractLayer<T, VH> {

    public static final int TYPE_HEADER = 7898;
    public static final int TYPE_FOOTER = 7899;

    WeakReference<RecyclerView> recyclerViewWeakReference;

    private boolean isParallaxHeader = false;
    private boolean isParallaxFooter = false;


    protected ArrayList<View> headers = new ArrayList<>();
    protected ArrayList<View> footers = new ArrayList<>();

    private GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return getGridSpan(position);
        }
    };

    protected void setHeaderFooterLayoutParams(ViewGroup viewGroup) {
        ViewGroup.LayoutParams layoutParams;
        RecyclerView.LayoutManager manager = recyclerViewWeakReference.get().getLayoutManager();
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
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            return (VH) onCreateItemViewHolder(viewGroup, type);
            //else if we have a header
        } else if (type == TYPE_HEADER) {
            //create a new ParallaxContainer
            HeaderFooterContainer header = new HeaderFooterContainer(viewGroup.getContext(),
                    isParallaxHeader, false);
            //make sure it fills the space
            setHeaderFooterLayoutParams(header);
            RecyclerView recyclerView = recyclerViewWeakReference.get();
            if (recyclerView != null)
                recyclerView.addOnScrollListener(header.getOnScrollListener());

            return (VH) new HeaderFooterViewHolder(header);
            //else we have a footer
        } else {
            //create a new ParallaxContainer
            HeaderFooterContainer footer = new HeaderFooterContainer(viewGroup.getContext(),
                    isParallaxFooter, true);
            //make sure it fills the space
            setHeaderFooterLayoutParams(footer);
            RecyclerView recyclerView = recyclerViewWeakReference.get();
            if (recyclerView != null)
                recyclerView.addOnScrollListener(footer.getOnScrollListener());
            return (VH) new HeaderFooterViewHolder(footer);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        //check what type of view our position is
        if (isHeader(position)) {
            View v = headers.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((RecyclerBindableAdapter.HeaderFooterViewHolder) vh, v);
        } else if (isFooter(position)) {
            View v = footers.get(position - getRealItemCount() - getHeadersCount());
            //add our view to a footer view and display it
            prepareHeaderFooter((RecyclerBindableAdapter.HeaderFooterViewHolder) vh, v);
        } else {
            //it's one of our items, display as required
            onBindItemViewHolder((VH) vh, position - headers.size(), getItemType(position));
        }
    }


    protected void prepareHeaderFooter(RecyclerBindableAdapter.HeaderFooterViewHolder vh, View view) {
        //if it's a staggered grid, span the whole layout
        RecyclerView.LayoutManager manager = recyclerViewWeakReference.get().getLayoutManager();
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

    protected boolean isHeader(int position) {
        return (position < headers.size());
    }

    protected boolean isFooter(int position) {
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
        int type = getItemType(position);
        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            throw new IllegalArgumentException("Item type cannot equal " + TYPE_HEADER + " or " + TYPE_FOOTER);
        }
        return type;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerViewWeakReference = new WeakReference<>(recyclerView);
        updateLayoutManager();
    }

    public void setParallaxHeader(boolean isParallaxHeader) {
        this.isParallaxHeader = isParallaxHeader;
    }

    public void setParallaxFooter(boolean isParallaxFooter) {
        this.isParallaxFooter = isParallaxFooter;
    }

    public void updateLayoutManager() {
        RecyclerView.LayoutManager manager = recyclerViewWeakReference.get().getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(spanSizeLookup);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) manager).setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        }
    }

    protected int getGridSpan(int position) {
        if (isHeader(position) || isFooter(position)) {
            return getMaxGridSpan();
        }
        position -= headers.size();
        if (getItem(position) instanceof RecyclerBindableAdapter.SpanItemInterface) {
            return ((RecyclerBindableAdapter.SpanItemInterface) getItem(position)).getGridSpan();
        }
        return 1;
    }

    protected int getMaxGridSpan() {
        RecyclerView.LayoutManager manager = recyclerViewWeakReference.get().getLayoutManager();
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
}
