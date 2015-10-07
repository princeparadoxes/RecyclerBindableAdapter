package com.princeparadoxes.danil.recyclerbindableadapter.grid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.princeparadoxes.danil.recyclerbindableadapter.MainViewHolder;
import com.princeparadoxes.danil.recyclerbindableadapter.R;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GridExampleActivity extends AppCompatActivity implements MainViewHolder.ActionListener {

    @Bind(R.id.linear_example_recycle)
    RecyclerView linearExampleRecycler;

    private static final int COUNT_ITEMS = 20;

    private GridExampleAdapter gridExampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_example);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        linearExampleRecycler.setLayoutManager(layoutManager);
        linearExampleRecycler.setItemAnimator(new DefaultItemAnimator());

        gridExampleAdapter = new GridExampleAdapter(this, layoutManager);
        gridExampleAdapter.setActionListener(this);
        linearExampleRecycler.setAdapter(gridExampleAdapter);
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
    public void remove(int position) {
        gridExampleAdapter.removeChild(position);
    }

    @Override
    public void up(int position) {
        gridExampleAdapter.moveChildTo(position, position - 1);
    }

    @Override
    public void down(int position) {
        gridExampleAdapter.moveChildTo(position, position + 1);
    }
}
