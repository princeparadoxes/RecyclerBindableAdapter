package com.danil.recyclerbindableadapter.library.filter;

public interface BindableAdapterFilter<T> {
    boolean onFilterItem(CharSequence constraint, T item);
}