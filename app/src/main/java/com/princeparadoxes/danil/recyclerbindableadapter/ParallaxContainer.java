package com.princeparadoxes.danil.recyclerbindableadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.FrameLayout;

/**
 * Created by Danil on 08.10.2015.
 */
public class ParallaxContainer extends FrameLayout {

    private int mOffset;
    private boolean mShouldClip;

    public ParallaxContainer(Context context, boolean shouldClick) {
        super(context);
        mShouldClip = shouldClick;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mShouldClip) {
            canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
        }
        super.dispatchDraw(canvas);
    }

    public void setClipY(int offset) {
        mOffset = offset;
        invalidate();
    }
}
