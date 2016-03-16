package com.danil.recyclerbindableadapter.library;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Danil on 08.10.2015.
 */
public final class SimpleParallaxBindableAdapter<T>
        extends ParallaxBindableAdapter<T, BindableViewHolder> {

    @LayoutRes
    private final int layoutId;
    Class<? extends BindableViewHolder<T, ? extends BindableViewHolder.ActionListener<T>>> vhClass;
    BindableViewHolder.ActionListener<T> actionListener;

    public SimpleParallaxBindableAdapter(@LayoutRes int layoutId,
                                 Class<? extends BindableViewHolder<T, ? extends
                                         BindableViewHolder.ActionListener<T>>> vhClass) {
        this.layoutId = layoutId;
        this.vhClass = vhClass;
    }

    @Override
    protected void onBindItemViewHolder(BindableViewHolder viewHolder,
                                        int position, int type) {
        //noinspection unchecked
        viewHolder.bindView(position, getItem(position), actionListener);
    }

    @Override
    protected BindableViewHolder viewHolder(View view, int type) {
        try {
            return vhClass.getConstructor(View.class).newInstance(view);
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

    public void setActionListener(BindableViewHolder.ActionListener<T> actionListener) {
        this.actionListener = actionListener;
    }

}
