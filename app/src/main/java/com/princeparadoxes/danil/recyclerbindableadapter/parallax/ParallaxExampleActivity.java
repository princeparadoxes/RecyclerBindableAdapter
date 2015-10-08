package com.princeparadoxes.danil.recyclerbindableadapter.parallax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.princeparadoxes.danil.recyclerbindableadapter.R;
import com.princeparadoxes.danil.recyclerbindableadapter.utils.BitmapUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ParallaxExampleActivity extends AppCompatActivity {

    private static final int COUNT_ITEMS = 10;
    @Bind(R.id.parallax_example_recycle)
    RecyclerView parallaxExampleRecycler;
    private ParallaxExampleAdapter parallaxExampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_example);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        parallaxExampleRecycler.setLayoutManager(layoutManager);
        parallaxExampleRecycler.setItemAnimator(new DefaultItemAnimator());

        parallaxExampleAdapter = new ParallaxExampleAdapter();
        parallaxExampleAdapter.addHeader(inflateHeaderFooter());
        parallaxExampleRecycler.setAdapter(parallaxExampleAdapter);
    }

    private View inflateHeaderFooter() {
        ImageView imageView = (ImageView) getLayoutInflater().inflate(
                R.layout.parallax_example_header, parallaxExampleRecycler, false);
        imageView.setImageResource(R.mipmap.ic_launcher);
        return imageView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < COUNT_ITEMS; i++) {
            list.add(String.valueOf(i + 1));
        }
        parallaxExampleAdapter.addAll(list);
    }
}
