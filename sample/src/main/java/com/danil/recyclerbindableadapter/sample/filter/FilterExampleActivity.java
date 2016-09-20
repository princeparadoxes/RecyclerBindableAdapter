package com.danil.recyclerbindableadapter.sample.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;

import com.danil.recyclerbindableadapter.library.filter.BindableAdapterFilter;
import com.danil.recyclerbindableadapter.sample.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterExampleActivity extends AppCompatActivity implements TextWatcher,
        Filter.FilterListener {

    @Bind(R.id.filter_example_recycle)
    protected RecyclerView filterExampleRecycler;

    @Bind(R.id.filter_example_edit_text)
    protected EditText filterEditText;

    @Bind(R.id.filter_example_no_result)
    protected View noResultView;

    private FilterExampleAdapter filterExampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_example);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        filterExampleRecycler.setLayoutManager(layoutManager);
        filterExampleRecycler.setItemAnimator(new DefaultItemAnimator());

        filterExampleAdapter = new FilterExampleAdapter();
        filterExampleRecycler.setAdapter(filterExampleAdapter);
        filterExampleAdapter.setFilter(new BindableAdapterFilter<Person>() {
            @Override
            public boolean onFilterItem(CharSequence constraint, Person item) {
                boolean first = item.getFirstName().toLowerCase().startsWith(constraint.toString());
                boolean last = item.getLastName().toLowerCase().startsWith(constraint.toString());
                return first || last;
            }
        });

        filterEditText.addTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Person> list = Person.getPersons();
        filterExampleAdapter.clear();
        filterExampleAdapter.addAll(list);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterExampleAdapter.getFilter().filter(s, FilterExampleActivity.this);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFilterComplete(int count) {
        noResultView.setVisibility(count > 0 ? View.GONE : View.VISIBLE);
    }
}
