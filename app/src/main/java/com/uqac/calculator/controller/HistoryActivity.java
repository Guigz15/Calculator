package com.uqac.calculator.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.uqac.calculator.model.Calculation;
import com.uqac.calculator.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView historyRecyclerView = findViewById(R.id.calculation_list);

        SharedPreferences mPrefs = getSharedPreferences("History", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        List<Calculation> calculations = new ArrayList<>();
        mPrefs.getAll().forEach((key, value) -> {
            Calculation calculation = gson.fromJson(value.toString(), Calculation.class);
            calculations.add(calculation);
        });

        CalculationAdapter adapter = new CalculationAdapter(this, calculations);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_history) {
            prefsEditor.clear().apply();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
