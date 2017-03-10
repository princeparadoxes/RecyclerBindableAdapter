package com.danil.recyclerbindableadapter.sample.filter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.danil.recyclerbindableadapter.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.filter_example_first_name)
    TextView firstNameField;
    @BindView(R.id.filter_example_last_name)
    TextView lastNameField;


    public FilterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(Person person) {
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
    }
}
