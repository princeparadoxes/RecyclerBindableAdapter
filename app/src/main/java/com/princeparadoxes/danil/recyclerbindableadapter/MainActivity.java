package com.princeparadoxes.danil.recyclerbindableadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.princeparadoxes.danil.recyclerbindableadapter.grid.GridExampleActivity;
import com.princeparadoxes.danil.recyclerbindableadapter.linear.LinearExampleActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.main_activity_linear_example_button)
    protected void onLinearButtonClick() {
        this.startActivity(new Intent(this, LinearExampleActivity.class));
    }

    @OnClick(R.id.main_activity_grid_example_button)
    protected void onGridButtonClick() {
        this.startActivity(new Intent(this, GridExampleActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
    }
}
