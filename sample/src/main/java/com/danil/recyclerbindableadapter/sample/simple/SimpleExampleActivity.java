package com.danil.recyclerbindableadapter.sample.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.danil.recyclerbindableadapter.library.SimpleBindableAdapter;
import com.danil.recyclerbindableadapter.sample.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleExampleActivity extends AppCompatActivity implements SimpleViewHolder.SimpleActionListener {

    private static final int COUNT_ITEMS = 20;
    @Bind(R.id.simple_example_recycle)
    RecyclerView simpleExampleRecycler;
    private SimpleBindableAdapter<Integer> simpleExampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_example);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        simpleExampleRecycler.setLayoutManager(layoutManager);
        simpleExampleRecycler.setItemAnimator(new DefaultItemAnimator());

        simpleExampleAdapter = new SimpleBindableAdapter<>(
                R.layout.simple_example_item, SimpleViewHolder.class);
        simpleExampleAdapter.setActionListener(this);
        simpleExampleRecycler.setAdapter(simpleExampleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < COUNT_ITEMS; i++) {
            list.add(i + 1);
        }
        simpleExampleAdapter.clear();
        simpleExampleAdapter.addAll(list);
    }

    @Override
    public void onMoveToTop(int position) {
        simpleExampleAdapter.moveChildTo(position, 0);
    }

    @Override
    public void OnRemove(int position) {
        simpleExampleAdapter.removeChild(position);
    }

    @Override
    public void OnUp(int position) {
        simpleExampleAdapter.moveChildTo(position, position - 1);
    }

    @Override
    public void OnDown(int position) {
        simpleExampleAdapter.moveChildTo(position, position + 1);
    }

    @Override
    public void OnItemClickListener(int position, Object Item) {
    }
}
