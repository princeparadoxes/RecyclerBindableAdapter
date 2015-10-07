package com.princeparadoxes.danil.recyclerbindableadapter.grid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.princeparadoxes.danil.recyclerbindableadapter.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GridExampleActivity extends AppCompatActivity implements GridViewHolder.ActionListener {

    private static final int COUNT_ITEMS = 20;
    @Bind(R.id.grid_example_recycler)
    RecyclerView gridExampleRecycler;
    private GridExampleAdapter gridExampleAdapter;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < COUNT_ITEMS; i++) {
            list.add(i + 1);
        }
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
        gridExampleAdapter.moveChildTo(position, position - 1);
    }

    @Override
    public void OnDown(int position) {
        gridExampleAdapter.moveChildTo(position, position + 1);
    }
}
