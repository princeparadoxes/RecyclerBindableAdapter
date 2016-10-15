package com.danil.recyclerbindableadapter.sample.grid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.danil.recyclerbindableadapter.sample.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GridExampleActivity extends AppCompatActivity implements GridViewHolder.ActionListener {

    private static final int COUNT_ITEMS = 20;
    public static final String KEY = "GridExampleActivity.adapter.items";

    @Bind(R.id.grid_example_recycler)
    RecyclerView gridExampleRecycler;

    private GridExampleAdapter gridExampleAdapter;
    private int lastItemTittle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_example);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridExampleRecycler.setLayoutManager(layoutManager);
        gridExampleRecycler.setItemAnimator(new DefaultItemAnimator());

        gridExampleAdapter = new GridExampleAdapter();
        gridExampleAdapter.setActionListener(this);
        gridExampleRecycler.setAdapter(gridExampleAdapter);
        ArrayList<GridExampleItem> list = new ArrayList<>();
        for (int i = 0; i < COUNT_ITEMS; i++) {
            if (i == 4 || i == 7) {
                list.add(new GridExampleItem(i + 1, GridExampleAdapter.SECOND_TYPE));
            } else {
                list.add(new GridExampleItem(i + 1, GridExampleAdapter.FIRST_TYPE));
            }
        }
        lastItemTittle = COUNT_ITEMS;
        gridExampleAdapter.addAll(list);
    }


    @Override
    public void onMoveToTop(int position) {
        gridExampleAdapter.moveChildTo(position, 0);
    }

    @Override
    public void OnRemove(int position) {
        gridExampleAdapter.removeChild(position);
    }

    @Override
    public void OnUp(int position) {
        if (position - 1 >= 0) {
            gridExampleAdapter.moveChildTo(position, position - 1);
        }
    }

    @Override
    public void OnDown(int position) {
        if (position + 1 < gridExampleAdapter.getNotFilteredItems().size()) {
            gridExampleAdapter.moveChildTo(position, position + 1);
        }
    }

    @Override
    public void OnAddTo(int position) {
        lastItemTittle++;
        GridExampleItem item = new GridExampleItem(lastItemTittle, GridExampleAdapter.FIRST_TYPE);
        gridExampleAdapter.add(position, item);
    }

    @Override
    public void OnSet(int position) {
        lastItemTittle++;
        if (position != 0) {
            GridExampleItem item = new GridExampleItem(lastItemTittle, GridExampleAdapter.FIRST_TYPE);
            gridExampleAdapter.set(position - 1, item);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY, gridExampleAdapter.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gridExampleAdapter.onRestoreInstanceState(savedInstanceState.getParcelable(KEY));
    }
}
