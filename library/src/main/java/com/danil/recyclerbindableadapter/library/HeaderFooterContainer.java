package com.danil.recyclerbindableadapter.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

class HeaderFooterContainer extends FrameLayout {

    private boolean isParallax;
    private boolean isFooter;
    private int offset;

    public HeaderFooterContainer(Context context, boolean isParallax, boolean isFooter) {
        super(context);
        this.isParallax = isParallax;
        this.isFooter = isFooter;
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        if (isParallax) {
            int top = isFooter ? -offset : 0;
            int bottom = isFooter ? getBottom() - getTop() : getBottom() + offset;
            Rect rect = new Rect(getLeft(), top, getRight(), bottom);
            canvas.clipRect(rect);
        }
        super.dispatchDraw(canvas);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isParallax) return;
                float temp;
                if (isFooter) {
                    temp = (float) ((recyclerView.getHeight() - getBottom()) * 0.5);
                    temp = temp > 0 ? 0 : temp;
                } else {
                    temp = (float) (-getTop() * 0.5);
                    temp = temp < 0 ? 0 : temp;

                }
                if (getTranslationY() == temp) return;
                setTranslationY(temp);
                setOffset(Math.round(temp));
                invalidate();
            }
        };
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        offset = ss.offset;
        isParallax = ss.isParallax;
        isFooter = ss.isFooter;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.offset = offset;
        savedState.isParallax = isParallax;
        savedState.isFooter = isFooter;
        return savedState;
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int offset;
        boolean isParallax;
        boolean isFooter;

        SavedState(Parcelable superState) {
            super(superState);
        }

        protected SavedState(Parcel in) {
            super(in);
            this.offset = in.readInt();
            this.isParallax = in.readByte() != 0;
            this.isFooter = in.readByte() != 0;
        }

        @Override
        public String toString() {
            return "ClipContainer.SavedState{"
                    + "offset=" + offset
                    + "isParallax=" + isParallax
                    + "isFooter=" + isFooter
                    + '}';
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.offset);
            dest.writeByte(this.isParallax ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFooter ? (byte) 1 : (byte) 0);
        }
    }
}
