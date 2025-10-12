package com.android.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

/**
 * DisplayActivity shows the history of previous calculations
 * from the calculator app database.
 * Each history record is displayed in a CardView within a LinearLayout.
 * If no history is present, a placeholder message is shown.
 */
public class DisplayActivity extends AppCompatActivity {
    private final AppLogger logger = new AppLogger("DisplayActivity");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        // Setup toolbar with back navigation
        MaterialToolbar toolbar = findViewById(R.id.toolbar_display);
        toolbar.setTitle("Calculation History");
        toolbar.setNavigationOnClickListener(v -> {
            logger.d("Back arrow tapped — finishing DisplayActivity");
            finish();
        });

        LinearLayout historyContainer = findViewById(R.id.container_history_records);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Fetch history from database safely
        List<String> historyList;
        try (DatabaseHelper databaseHelper = new DatabaseHelper(this)) {
            historyList = databaseHelper.getAllRecordsList();
            logger.d("Fetched " + historyList.size() + " records from DB");
        } catch (Exception e) {
            logger.e("Error fetching history from DB", e);
            historyList = List.of();
        }

        String[] calculationHistory = historyList.toArray(new String[0]);

        // Display placeholder if no history
        if (calculationHistory.length == 0) {
            TextView emptyTextView = new TextView(this);
            emptyTextView.setText("No calculation history available.");
            emptyTextView.setTextSize(18);
            emptyTextView.setPadding(32, 48, 32, 48);
            emptyTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            historyContainer.addView(emptyTextView);
            logger.d("No history records found");
            return;
        }

        // Display each history record in a CardView
        logger.d("Displaying " + calculationHistory.length + " records");
        for (String record : calculationHistory) {
            if (record == null || record.trim().isEmpty()) {
                logger.w("Skipped empty or null record");
                continue;
            }

            try {
                CardView cardView = (CardView) inflater.inflate(
                        R.layout.item_history_card, historyContainer, false);
                TextView historyTextView = cardView.findViewById(R.id.text_history_record);
                historyTextView.setText(record.trim());

                // Set card background tint
                cardView.setCardBackgroundColor(getColor(R.color.card_tint_light));

                historyContainer.addView(cardView);
                logger.d("Added record to container: " + record);
            } catch (Exception e) {
                logger.e("Error inflating or adding history card for record: " + record, e);
            }
        }
    }
}
