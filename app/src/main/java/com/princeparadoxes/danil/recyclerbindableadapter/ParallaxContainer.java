package com.princeparadoxes.danil.recyclerbindableadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.FrameLayout;

/**
 * Created by Danil on 08.10.2015.
 */
public class ParallaxContainer extends FrameLayout {

    private int offset;
    private boolean shouldClip;
    private boolean isFooter;

    public ParallaxContainer(Context context, boolean shouldClick) {
        super(context);
        shouldClip = shouldClick;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (shouldClip) {
            int left = getLeft();
            int right = getRight();
            int top = isFooter ? - offset : 0;
            int bottom = isFooter ? getBottom() : getBottom() + offset;
            Rect rect = new Rect(left, top, right, bottom);
            canvas.clipRect(rect);
        }
        super.dispatchDraw(canvas);
    }

    public void setClipY(int offset, boolean isFooter) {
        this.offset = offset;
        this.isFooter = isFooter;
        invalidate();
    }
}
