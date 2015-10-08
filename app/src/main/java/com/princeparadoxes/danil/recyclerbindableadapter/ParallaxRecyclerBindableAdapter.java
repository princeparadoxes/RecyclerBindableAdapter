package com.princeparadoxes.danil.recyclerbindableadapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

/**
 * Created by Danil on 08.10.2015.
 */
public abstract class ParallaxRecyclerBindableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerBindableAdapter<T, VH> {

    private static final float SCROLL_MULTIPLIER = 0.5f;
    private ParallaxContainer header;
    private ParallaxContainer footer;
    private OnParallaxScroll parallaxScroll;
    private boolean shouldClipView = true;

    /**
     * Translates the adapter in Y
     *
     * @param of offset in px
     */
    public void translateView(float of, ParallaxContainer view, boolean isFooter) {
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
        view.setClipY(Math.round(ofCalculated), isFooter);
        if (parallaxScroll != null) {
            float left = Math.min(1, ((ofCalculated) / (view.getHeight() * SCROLL_MULTIPLIER)));
            parallaxScroll.onParallaxScroll(left, of, view);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            return (VH) onCreteItemViewHolder(viewGroup, type);
            //else we have a header/footer
        } else if (type == TYPE_HEADER) {
            //create a new ParallaxContainer
            header = new ParallaxContainer(viewGroup.getContext(), true);
            //make sure it fills the space
            header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return (VH) new HeaderFooterViewHolder(header);
        } else {
            //create a new ParallaxContainer
            footer = new ParallaxContainer(viewGroup.getContext(), shouldClipView);
//            make sure it fills the space
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
                if (header != null) {
                    translateView(recyclerView.computeVerticalScrollOffset(), header, false);
//                    translateView(0, header);

                }
                if (footer != null) {
                    int range = recyclerView.computeVerticalScrollRange();
                    int extend = recyclerView.computeVerticalScrollExtent();
                    int offset = recyclerView.computeVerticalScrollOffset();
                    translateView(range - (extend + offset), footer, true);
//                    translateView(0, footer);
                }
            }
        });
    }

    public boolean isShouldClipView() {
        return shouldClipView;
    }

    /**
     *
     * @param shouldClickView
     */
    public void setShouldClipView(boolean shouldClickView) {
        shouldClipView = shouldClickView;
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
