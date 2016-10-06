package com.danil.recyclerbindableadapter.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public abstract class ParallaxBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerBindableAdapter<T, VH> {

    private static final float SCROLL_MULTIPLIER = 0.5f;

    WeakReference<RecyclerView> recyclerViewWeakReference;

    private boolean isParallaxHeader = true;
    private boolean isParallaxFooter = true;

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            return (VH) onCreateItemViewHolder(viewGroup, type);
            //else if we have a header
        } else if (type == TYPE_HEADER) {
            //create a new ParallaxContainer
            HeaderFooterContainer header = new HeaderFooterContainer(viewGroup.getContext(), isParallaxHeader, false);
            //make sure it fills the space
            setHeaderFooterLayoutParams(header);
            RecyclerView recyclerView = recyclerViewWeakReference.get();
            if (recyclerView != null)
                recyclerView.addOnScrollListener(header.getOnScrollListener());

            return (VH) new HeaderFooterViewHolder(header);
            //else we have a footer
        } else {
            //create a new ParallaxContainer
            HeaderFooterContainer footer = new HeaderFooterContainer(viewGroup.getContext(), isParallaxFooter, true);
            //make sure it fills the space
            setHeaderFooterLayoutParams(footer);
            RecyclerView recyclerView = recyclerViewWeakReference.get();
            if (recyclerView != null)
                recyclerView.addOnScrollListener(footer.getOnScrollListener());
            return (VH) new HeaderFooterViewHolder(footer);
        }
    }

    @Override
    final public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        //check what type of view our position is
        if (isHeader(position)) {
            View v = getHeader(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);

        } else if (isFooter(position)) {
            View v = getFooter(position - getRealItemCount() - getHeadersCount());
            //add our view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else {
            //it's one of our items, display as required
            onBindItemViewHolder((VH) vh, position - getHeadersCount(), getItemType(position));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerViewWeakReference = new WeakReference<>(recyclerView);
    }

    public void setParallaxHeader(boolean isParallaxHeader) {
        this.isParallaxHeader = isParallaxHeader;
    }

    public void setParallaxFooter(boolean isParallaxFooter) {
        this.isParallaxFooter = isParallaxFooter;
    }

}
