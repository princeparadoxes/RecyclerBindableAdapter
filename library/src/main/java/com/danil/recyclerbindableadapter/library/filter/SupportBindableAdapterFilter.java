package com.danil.recyclerbindableadapter.library.filter;

@Deprecated
public interface SupportBindableAdapterFilter {
    boolean onFilterItem(CharSequence constraint, String item);
}