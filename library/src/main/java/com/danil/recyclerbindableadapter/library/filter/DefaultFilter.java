package com.danil.recyclerbindableadapter.library.filter;

/**
 * Created by Danil on 20.09.2016.
 */
public class DefaultFilter<T> implements BindableAdapterFilter<T>{

    @Override
    public boolean onFilterItem(CharSequence constraint, T item) {
        final String valueText = item.toString().toLowerCase();

        // First match against the whole, non-splitted value
        if (valueText.startsWith(constraint.toString())) {
            return true;
        } else {
            final String[] words = valueText.split(" ");

            // Start at index 0, in case valueText starts with space(s)
            for (String word : words) {
                if (word.contains(constraint)) {
                    return true;
                }
            }
        }
        return false;
    }
}
