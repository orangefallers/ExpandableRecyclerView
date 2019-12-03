package com.ofcat.expandablerecyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ofcat.expandablerecyclerview.ExpandableRecyclerView;

public class MainActivity extends AppCompatActivity {

    private ExpandableRecyclerView expandableRecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableRecyclerView = findViewById(R.id.expandableRecycler);
        expandableRecyclerView.setAdapter(new ExpandableDemoRecyclerAdapter());
    }
}
