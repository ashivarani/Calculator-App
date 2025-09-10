package com.android.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Calculation History");
        }

        Log.d(TAG, "onCreate: DisplayActivity started");

        TextView out = findViewById(R.id.op);
        out.setMovementMethod(new ScrollingMovementMethod()); // Allow scrolling
        List<String> historyList;
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            historyList = dbHelper.getAllRecordsList();
        }

        if (!historyList.isEmpty()) {
            StringBuilder formatted = new StringBuilder();
            for (String record : historyList) {
                formatted.append("➡ ").append(record).append("\n-----------------------------\n");
            }
            out.setText(formatted.toString().trim());
        } else {
            out.setText("No history available.");
        }
        String output = getIntent().getStringExtra("output");

        if (output != null && !output.trim().isEmpty()) {
            String[] records = output.split("\n");
            StringBuilder formatted = new StringBuilder();

            for (String record : records) {
                if (!record.trim().isEmpty()) {
                    formatted.append("➡ ")
                            .append(record.trim().replace(" =", "\n= "))
                            .append("\n-----------------------------\n");
                }
            }

            out.setText(formatted.toString().trim());
            Log.d(TAG, "onCreate: Displaying " + records.length + " records");
        } else {
            out.setText("No history available.");
            Log.d(TAG, "onCreate: No history to display");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
