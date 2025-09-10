package com.android.calculator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    // Notification constants
    private static final String CHANNEL_ID = "mychannel";
    private static final int NOTIFICATION_ID = 1;
    private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 2;

    // Calculator operators
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char DIVISION = '/';
    private static final char MULTIPLICATION = '*';
    private static final char MODULO = '%';

    private char currentsymbol;
    private double firstvalue = Double.NaN;
    private double secondvalue;
    private double resultvalue = 0;

    // For DB storage
    private String R1, R2, R3, Sb;

    private TextView inputdisplay, outputdisplay;
    private DecimalFormat decimalformat;

    // Buttons
    private MaterialButton button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9,
            buttonadd, buttonsub, buttonmultiply, buttondivision,
            buttonmodulo, buttonclear, buttondot, buttonoff,
            buttonequal, buttonall;

    // Database
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d("MainActivity", "onCreate started");

        db = new DatabaseHelper(this);
        decimalformat = new DecimalFormat("#.##########");

        inputdisplay = findViewById(R.id.input);
        outputdisplay = findViewById(R.id.output);

        initButtons();
        setupButtonListeners();

        createNotificationChannel();
        checkNotificationPermissionAndNotify();
    }

    /** Initialize all buttons */
    private void initButtons() {
        button0 = findViewById(R.id.btn0);
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        button4 = findViewById(R.id.btn4);
        button5 = findViewById(R.id.btn5);
        button6 = findViewById(R.id.btn6);
        button7 = findViewById(R.id.btn7);
        button8 = findViewById(R.id.btn8);
        button9 = findViewById(R.id.btn9);
        buttonadd = findViewById(R.id.add);
        buttonsub = findViewById(R.id.sub);
        buttonmodulo = findViewById(R.id.percent);
        buttonmultiply = findViewById(R.id.multiply);
        buttondivision = findViewById(R.id.division);
        buttonclear = findViewById(R.id.clear);
        buttondot = findViewById(R.id.btnpoint);
        buttonequal = findViewById(R.id.equal);
        buttonoff = findViewById(R.id.off);
        buttonall = findViewById(R.id.all);
    }

    /** Setup button click listeners */
    private void setupButtonListeners() {
        View.OnClickListener numberClick = view -> {
            String value = ((MaterialButton) view).getText().toString();
            if ((currentsymbol == DIVISION || currentsymbol == MODULO) &&
                    inputdisplay.getText().toString().isEmpty() && value.equals("0")) {
                Toast.makeText(this, "Cannot divide/mod by zero", Toast.LENGTH_SHORT).show();
                return;
            }
            inputdisplay.append(value);
        };

        button0.setOnClickListener(numberClick);
        button1.setOnClickListener(numberClick);
        button2.setOnClickListener(numberClick);
        button3.setOnClickListener(numberClick);
        button4.setOnClickListener(numberClick);
        button5.setOnClickListener(numberClick);
        button6.setOnClickListener(numberClick);
        button7.setOnClickListener(numberClick);
        button8.setOnClickListener(numberClick);
        button9.setOnClickListener(numberClick);

        buttonadd.setOnClickListener(v -> performOperation(ADDITION));
        buttonsub.setOnClickListener(v -> performOperation(SUBTRACTION));
        buttonmultiply.setOnClickListener(v -> performOperation(MULTIPLICATION));
        buttondivision.setOnClickListener(v -> performOperation(DIVISION));
        buttonmodulo.setOnClickListener(v -> performOperation(MODULO));

        buttondot.setOnClickListener(v -> addDot());

        buttonclear.setOnClickListener(v -> {
            if (inputdisplay.getText().length() > 0) {
                CharSequence current = inputdisplay.getText();
                inputdisplay.setText(current.subSequence(0, current.length() - 1));
            } else {
                firstvalue = Double.NaN;
                secondvalue = Double.NaN;
                inputdisplay.setText("");
                outputdisplay.setText("");
            }
        });

        buttonoff.setOnClickListener(v -> {
            Toast.makeText(this, "Closing the App!", Toast.LENGTH_LONG).show();
            finish();
        });

        buttonall.setOnClickListener(v -> {
            firstvalue = Double.NaN;
            secondvalue = Double.NaN;
            inputdisplay.setText("");
            outputdisplay.setText("");
        });

        buttonequal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (inputdisplay.getText().toString().isEmpty()) return;

                calculator();
                outputdisplay.setText(decimalformat.format(firstvalue));

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                if (R1 != null && Sb != null && R2 != null && R3 != null) {
                    long recordId = db.insertCalculation(R1, Sb, R2, R3, dtf.format(now));
                    if (recordId > 0) {
                        Toast.makeText(MainActivity.this, R1 + Sb + R2 + " = " + R3, Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Saved record: " + R1 + Sb + R2 + "=" + R3);
                    }
                }

                firstvalue = Double.NaN;
                R1 = R2 = R3 = Sb = null;
                currentsymbol = '0';
            }
        });
    }

    /** Menu setup */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history) {
            Intent intent = new Intent(this, DisplayActivity.class);
            intent.putExtra("output", db.getAllRecordsString());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /** Perform operation */
    private void performOperation(char operation) {
        if (inputdisplay.getText().toString().isEmpty()) return;

        double inputNumber = 0;
        try {
            inputNumber = Double.parseDouble(inputdisplay.getText().toString());
        } catch (NumberFormatException e) {
            Log.e("MainActivity", "Invalid number", e);
        }

        calculator();
        currentsymbol = operation;
        outputdisplay.setText(decimalformat.format(firstvalue) + operation);
        inputdisplay.setText(null);
        Sb = String.valueOf(operation);
    }

    /** Core calculation logic */
    private void calculator() {
        if (!Double.isNaN(firstvalue)) {
            secondvalue = Double.parseDouble(inputdisplay.getText().toString());
            inputdisplay.setText(null);

            switch (currentsymbol) {
                case ADDITION:
                    resultvalue = firstvalue + secondvalue;
                    break;
                case SUBTRACTION:
                    resultvalue = firstvalue - secondvalue;
                    break;
                case MULTIPLICATION:
                    resultvalue = firstvalue * secondvalue;
                    break;
                case DIVISION:
                    resultvalue = firstvalue / secondvalue;
                    break;
                case MODULO:
                    resultvalue = firstvalue % secondvalue;
                    break;
            }

            R1 = String.valueOf(firstvalue);
            R2 = String.valueOf(secondvalue);
            R3 = String.valueOf(resultvalue);

            firstvalue = resultvalue;
        } else {
            try {
                firstvalue = Double.parseDouble(inputdisplay.getText().toString());
            } catch (Exception e) {
                Log.e("MainActivity", "calculator exception", e);
            }
        }
    }

    /** Dot handling */
    private void addDot() {
        if (!inputdisplay.getText().toString().contains(".")) {
            inputdisplay.append(".");
        }
    }

    /** Notifications */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void checkNotificationPermissionAndNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_POST_NOTIFICATIONS);
            } else showNotification();
        } else showNotification();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showNotification();
            } else {
                guideUserToAppSettings();
            }
        }
    }

    private void guideUserToAppSettings() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("Notification permission was denied. Enable it in app settings.")
                .setPositiveButton("App Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notify)
                .setContentTitle("MyCalculator App")
                .setContentText("App Running in Background")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
