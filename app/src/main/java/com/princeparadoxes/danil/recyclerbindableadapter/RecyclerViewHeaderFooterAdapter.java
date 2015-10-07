package com.princeparadoxes.danil.recyclerbindableadapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewHeaderFooterAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    public static final int TYPE_HEADER = 7898;
    public static final int TYPE_FOOTER = 7899;

    protected List<View> headers = new ArrayList<>();
    private List<View> footers = new ArrayList<>();

    private RecyclerView.LayoutManager manager;

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            return (VH) onCreteItemViewHolder(viewGroup, type);
            //else we have a header/footer
        } else {
            //create a new framelayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            //make sure it fills the space
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return (VH) new HeaderFooterViewHolder(frameLayout);
        }
    }

    @Override
    final public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        //check what type of view our position is
        if (isHeader(position)) {
            View v = headers.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (isFooter(position)) {
            View v = footers.get(position - getRealItemCount() - footers.size());
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
        vh.base.removeAllViews();
        vh.base.addView(view);
    }

    private boolean isHeader(int position) {
        return (position < headers.size());
    }

    private boolean isFooter(int position) {
        return footers.size() > 0 && (position >= getRealItemCount() + footers.size());
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (manager == null) {
            setManager(recyclerView.getLayoutManager());
        }
    }

    private void setManager(RecyclerView.LayoutManager manager) {
        this.manager = manager;
        if (this.manager instanceof GridLayoutManager) {
            ((GridLayoutManager) this.manager).setSpanSizeLookup(mSpanSizeLookup);
        } else if (this.manager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) this.manager).setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        }
    }

    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return getGridSpan(position);
        }
    };

    public int getGridSpan(int position) {
        if (isHeader(position) || isFooter(position)) {
            if (manager instanceof GridLayoutManager) {
                return ((GridLayoutManager) manager).getSpanCount();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) manager).getSpanCount();
            }
            return 1;
        }
        position -= headers.size();
        if (getItem(position) instanceof SpanItemInterface) {
            return ((SpanItemInterface) getItem(position)).getGridSpan();
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

    //remove a header from the adapter @TODO test
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

    //remove a footer from the adapter @TODO test
    public void removeFooter(View footer) {
        if (footers.contains(footer)) {
            //animate
            notifyItemRemoved(headers.size() + getItemCount() + footers.indexOf(footer));
            footers.remove(footer);
        }
    }

    //our header/footer RecyclerView.ViewHolder is just a FrameLayout
    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        FrameLayout base;

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
            base = (FrameLayout) itemView;
        }
    }

    public int getHeadersCount() {
        return headers.size();
    }

    abstract protected int getItemType(int position);

    abstract public T getItem(int position);

    abstract protected VH onCreteItemViewHolder(ViewGroup parent, int type);

    abstract protected void onBindItemViewHolder(VH viewHolder, int position, int type);

    abstract protected int getRealItemCount();

    public interface SpanItemInterface {
        int getGridSpan();
    }
}