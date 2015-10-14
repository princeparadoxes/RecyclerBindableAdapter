package com.danil.recyclerbindableadapter.library;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Danil on 08.10.2015.
 */
public final class SimpleParallaxBindableAdapter<T, VH extends BindableViewHolder>
        extends ParallaxBindableAdapter<T, VH> {

    @LayoutRes
    private final int layoutId;
    Class<VH> vhClass;
    BindableViewHolder.ActionListener actionListener;

    public SimpleParallaxBindableAdapter(int layoutId, Class<VH> vhClass) {
        this.layoutId = layoutId;
        this.vhClass = vhClass;
    }

    @Override
    protected void onBindItemViewHolder(BindableViewHolder viewHolder, int position, int type) {
        viewHolder.bindView(position, getItem(position), actionListener);
    }

    @Override
    protected VH viewHolder(View view, int type) {
        try {
            return (VH) vhClass.getConstructor(View.class).newInstance(view);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected int layoutId(int type) {
        return layoutId;
    }

    public void setActionListener(BindableViewHolder.ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
