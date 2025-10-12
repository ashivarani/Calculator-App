package com.android.calculator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;

/**
 * MainActivity: Handles the calculator UI, button interactions,
 * integrates ExpressionEvaluator, and manages history & notifications.
 */
public class MainActivity extends AppCompatActivity {
    private final AppLogger logger = new AppLogger("MainActivity");
    private TextView textInputDisplay, textResultDisplay;
    private ExpressionEvaluator expressionEvaluator;
    private NotificationHelper notificationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        logger.d("MainActivity onCreate() — initializing UI");

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize displays
        textInputDisplay = findViewById(R.id.text_input_display);
        textResultDisplay = findViewById(R.id.text_output_display);

        // Initialize helpers
        expressionEvaluator = new ExpressionEvaluator(this);
        notificationHelper = new NotificationHelper(this);

        // Initialize calculator buttons
        initializeCalculatorButtons();

        // Setup notifications
        notificationHelper.createNotificationChannel();
        notificationHelper.checkPermissionAndNotify(this);

        logger.d("MainActivity setup completed");
    }

    /**
     * Initializes all calculator buttons with appropriate listeners.
     * Handles numeric, operator, decimal, clear, all-clear, OFF, and equals buttons.
     */
    private void initializeCalculatorButtons() {
        int[] numberButtonIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
                R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
        };

        // Number buttons
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(v -> {
                MaterialButton btn = (MaterialButton) v;
                expressionEvaluator.handleNumberInput(btn, textInputDisplay, textResultDisplay);
                logger.d("Number pressed: " + btn.getText());
            });
        }

        // Operator buttons
        findViewById(R.id.button_add).setOnClickListener(v ->
                expressionEvaluator.handleOperation('+', textInputDisplay));
        findViewById(R.id.button_subtract).setOnClickListener(v ->
                expressionEvaluator.handleOperation('-', textInputDisplay));
        findViewById(R.id.button_multiply).setOnClickListener(v ->
                expressionEvaluator.handleOperation('*', textInputDisplay));
        findViewById(R.id.button_divide).setOnClickListener(v ->
                expressionEvaluator.handleOperation('/', textInputDisplay));
        findViewById(R.id.button_percentage).setOnClickListener(v ->
                expressionEvaluator.handleOperation('%', textInputDisplay));

        // Decimal button
        findViewById(R.id.button_dot).setOnClickListener(v ->
                expressionEvaluator.handleDot(textInputDisplay, textResultDisplay));

        // Clear and All-Clear buttons
        findViewById(R.id.button_clear).setOnClickListener(v ->
                expressionEvaluator.handleClear(textInputDisplay, textResultDisplay));
        findViewById(R.id.button_all_clear).setOnClickListener(v ->
                expressionEvaluator.handleAllClear(textInputDisplay, textResultDisplay));

        // OFF button — clears history
        findViewById(R.id.button_off).setOnClickListener(v -> {
            expressionEvaluator.clearAllHistory();
            Toast.makeText(this, "All calculation history cleared!", Toast.LENGTH_SHORT).show();
            logger.d("OFF button pressed — history cleared");
        });

        // Equal button
        findViewById(R.id.button_equal).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                expressionEvaluator.handleEqual(textInputDisplay, textResultDisplay);
                logger.d("Equal button pressed");
            } else {
                Toast.makeText(this, "Requires Android O or higher", Toast.LENGTH_SHORT).show();
                logger.w("Equal pressed on unsupported Android version");
            }
        });
    }

    /**
     * Inflates the options menu (History menu).
     * @param menu the options menu
     * @return true if menu inflated
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Handles menu item selections.
     * Specifically, opens {@link DisplayActivity} to show calculation history.
     * @param item the selected menu item
     * @return true if handled, else super
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == R.id.menu_history) {
            // Fetch history safely
            String[] history;
            try {
                history = expressionEvaluator.databaseHelper.getAllRecordsList().toArray(new String[0]);
                logger.d("History fetched successfully for DisplayActivity");
            } catch (Exception e) {
                logger.e("Error fetching history for DisplayActivity", e);
                history = new String[0];
            }

            Intent intent = new Intent(this, DisplayActivity.class);
            intent.putExtra("output", history);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Delegates permission result handling to {@link NotificationHelper}.
     * @param requestCode  permission request code
     * @param permissions  requested permissions
     * @param grantResults grant results for requested permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        notificationHelper.handlePermissionResult(grantResults);
        logger.v("Permission result handled by NotificationHelper");
    }
}
