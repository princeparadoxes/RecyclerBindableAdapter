package com.danil.recyclerbindableadapter.library.filter;

/**
 * Created by Danil on 20.09.2016.
 */
@Deprecated
public class SupportDefaultFilter implements SupportBindableAdapterFilter {

    @Override
    public boolean onFilterItem(CharSequence constraint, String valueText) {
        valueText = valueText.toLowerCase();
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
