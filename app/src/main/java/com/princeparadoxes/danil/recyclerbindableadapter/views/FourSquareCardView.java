package com.princeparadoxes.danil.recyclerbindableadapter.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by Danil on 07.10.2015.
 */
public class FourSquareCardView extends CardView {
    public FourSquareCardView(Context context) {
        super(context);
    }

    public FourSquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FourSquareCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
