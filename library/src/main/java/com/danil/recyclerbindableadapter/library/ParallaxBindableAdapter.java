package com.danil.recyclerbindableadapter.library;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.danil.recyclerbindableadapter.library.view.ParallaxContainer;

/**
 * Created by Danil on 08.10.2015.
 */
public abstract class ParallaxBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerBindableAdapter<T, VH> {

    private static final float SCROLL_MULTIPLIER = 0.5f;
    private ParallaxContainer header;
    private ParallaxContainer footer;
    private OnParallaxScroll parallaxScroll;
    private boolean isParallaxHeader = true;
    private boolean isParallaxFooter = true;

    //parallax adapter may have only one header
    @Override
    public void addHeader(View header) {
        if (getHeadersCount() == 0) {
            super.addHeader(header);
        } else {
            removeHeader(getHeader(0));
            super.addHeader(header);
        }
    }

    //parallax adapter may have only one header
    @Override
    public void addFooter(View footer) {
        if (getFootersCount() == 0) {
            super.addFooter(footer);
        } else {
            removeFooter(getFooter(0));
            super.addFooter(footer);
        }
    }

    private void translateView(float of, ParallaxContainer view, boolean isFooter) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        ofCalculated = isFooter ? -ofCalculated : ofCalculated;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setTranslationY(ofCalculated);
        } else {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            view.startAnimation(anim);
        }
        view.setClipY(Math.round(ofCalculated));
        if (parallaxScroll != null && !isFooter) {
            float left = Math.min(1, ((ofCalculated) / (view.getHeight() * SCROLL_MULTIPLIER)));
            parallaxScroll.onParallaxScroll(left, of, view);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            return (VH) onCreateItemViewHolder(viewGroup, type);
            //else if we have a header
        } else if (type == TYPE_HEADER) {
            //create a new ParallaxContainer
            header = new ParallaxContainer(viewGroup.getContext(), isParallaxHeader, false);
            //make sure it fills the space
            header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return (VH) new HeaderFooterViewHolder(header);
            //else we have a footer
        } else {
            //create a new ParallaxContainer
            footer = new ParallaxContainer(viewGroup.getContext(), isParallaxFooter, true);
            //make sure it fills the space
            footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return (VH) new HeaderFooterViewHolder(footer);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (header != null && isParallaxHeader) {
                    translateView(recyclerView.computeVerticalScrollOffset(), header, false);
                }
                if (footer != null && isParallaxFooter) {
                    int range = recyclerView.computeVerticalScrollRange();
                    int extend = recyclerView.computeVerticalScrollExtent();
                    int offset = recyclerView.computeVerticalScrollOffset();
                    translateView(range - (extend + offset), footer, true);
                }
            }
        });
    }

    public void setParallaxHeader(boolean isParallaxHeader) {
        this.isParallaxHeader = isParallaxHeader;
    }

    public void setParallaxFooter(boolean isParallaxFooter) {
        this.isParallaxFooter = isParallaxFooter;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        this.parallaxScroll = parallaxScroll;
        this.parallaxScroll.onParallaxScroll(0, 0, header);
    }

    public interface OnParallaxScroll {
        /**
         * Event triggered when the parallax is being scrolled.
         *
         * @param percentage
         * @param offset
         * @param parallax
         */
        void onParallaxScroll(float percentage, float offset, View parallax);
    }
}
