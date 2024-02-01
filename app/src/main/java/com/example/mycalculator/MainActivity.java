package com.example.mycalculator;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.text.InputFilter;
import android.text.Spanned;
public class MainActivity extends AppCompatActivity {

    //variables for notifications
    private static final String CHANNEL_ID = "mychannel";
    private static final int NOTIFICATION_ID = 1;
    private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 2;




    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char DIVISION = '/';
    private static final char MULTIPLICATION = '*';
    private static final char MODULO = '%';



    private char currentsymbol;
    private double firstvalue = Double.NaN;
    private double secondvalue;
    private double resultvalue = 0;
    private String R1 = null;
    private String R2 = null;
    private String R3 = null;
    private String Sb = null;
    private TextView inputdisplay, outputdisplay;
    private DecimalFormat decimalformat;


    private MaterialButton button0;
    private MaterialButton button1;
    private MaterialButton button2;
    private MaterialButton button3;
    private MaterialButton button4;
    private MaterialButton button5;
    private MaterialButton button6;
    private MaterialButton button7;
    private MaterialButton button8;
    private MaterialButton button9;
    private MaterialButton buttonadd;
    private MaterialButton buttonsub;
    private MaterialButton buttonmultiply;
    private MaterialButton buttondivision;
    private MaterialButton buttonmodulo;
    private MaterialButton buttonclear;
    private MaterialButton buttondot;
    private MaterialButton buttonoff;
    private MaterialButton buttonequal;
    private MaterialButton buttonall;


    DabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("OnCreate","In the MainActivity");

        //creating a database
        db = new DabaseHelper(MainActivity.this, "MyDataBase",null,1);


        decimalformat = new DecimalFormat("#.##########");
        inputdisplay = findViewById(R.id.input);
        outputdisplay = findViewById(R.id.output);

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

        //calling methods for notification
        createNotificationChannel();
        checkNotificationPermissionAndNotify();

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","0 is clicked ");
                String currentInput = inputdisplay.getText().toString();

                // Prevent entering '0' as secondvalue for division
                if (currentsymbol == DIVISION && currentInput.isEmpty()) {
                    Log.d("button0","Trying to diving by zero");
                    return;
                }
                // Prevent entering '0' as secondvalue for modulo
                if (currentsymbol == MODULO && currentInput.isEmpty()) {
                    Log.d("button0","Trying modulus by zero");
                    return;
                }
                inputdisplay.setText(inputdisplay.getText() + "0");

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","1 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "1");

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","2 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "2");

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","3 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "3");

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","4 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "4");

            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","5 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "5");

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","6 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "6");

            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","7 is clicked ");
                inputdisplay.setText(inputdisplay.getText() + "7");


            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","8 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "8");

            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","9 is clicked");
                inputdisplay.setText(inputdisplay.getText() + "9");

            }
        });

        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate"," + is clicked");
                if(inputdisplay.getText().toString().isEmpty()) {
                    Log.d("OnCreate","InputDisplay is empty in buttonadd");
                    return;
                }

                    mycalculator();
                    Log.d("OnCreate","Calculator function is Called via addition");
                    currentsymbol = ADDITION;
                    outputdisplay.setText(decimalformat.format(firstvalue) + "+");
                    inputdisplay.setText(null);


            }
        });

        buttonsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","- is clicked");
                if(inputdisplay.getText().toString().isEmpty()) {
                    Log.d("OnCreate","InputDisplay is empty in buttonsub");
                    return;
                }
                mycalculator();
                Log.d("OnCreate","Calculator function is Called via subtraction");
                currentsymbol = SUBTRACTION;
                outputdisplay.setText(decimalformat.format(firstvalue) + "-");
                inputdisplay.setText(null);

            }
        });

        buttonmultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","* is clicked");
                if(inputdisplay.getText().toString().isEmpty()) {
                   Log.d("OnCreate","InputDisplay is empty in buttonmultiply");
                    return;
                }
                // Parse the input to check if it's zero
                double inputNumber = 0;
                try {
                    inputNumber = Double.parseDouble(inputdisplay.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e("OnCreate", "Number format exception", e);
                }

                // Check if attempting to multiply with zero
                if (inputNumber == 0 ) {
                    Log.d("buttonmultiply","Attmepting to multiply with zero");
                    return;
                }
                    mycalculator();
                    Log.d("OnCreate","Calculator function is Called via multiplication");
                    currentsymbol = MULTIPLICATION;
                    outputdisplay.setText(decimalformat.format(firstvalue) + "x");
                    inputdisplay.setText(null);
                }
        });

        buttondivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","/ is clicked");
                if(inputdisplay.getText().toString().isEmpty()) {
                    Log.d("OnCreate","InputDisplay is empty in buttondivision");
                    return;
                }

                // Parse the input to check if it's zero
                double inputNumber = 0;
                try {
                    inputNumber = Double.parseDouble(inputdisplay.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e("OnCreate", "Number format exception", e);
                }

                // Check if attempting to divide by zero
                if (inputNumber == 0 ) {
                    return;
                }


                mycalculator();
                Log.d("OnCreate","Calculator function is Called via division");
                currentsymbol = DIVISION;
                outputdisplay.setText(decimalformat.format(firstvalue) + "/");
                inputdisplay.setText(null);
            }
        });

        buttonmodulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","% is clicked");
                if(inputdisplay.getText().toString().isEmpty()) {
                    Log.d("OnCreate","InputDisplay is empty in buttonmodulo");
                    return;
                }
                // Parse the input to check if it's zero
                double inputNumber = 0;
                try {
                    inputNumber = Double.parseDouble(inputdisplay.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e("OnCreate", "Number format exception", e);
                }

                // Check if attempting to modulus by zero
                if (inputNumber == 0) {
                    return;
                }
                mycalculator();
                Log.d("OnCreate","Calculator function is Called via modulo");
                currentsymbol = MODULO;
                outputdisplay.setText(decimalformat.format(firstvalue) + "%");
                inputdisplay.setText(null);
            }
        });

        buttondot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate",". is clicked");
                dotcheck();
            }
        });

        buttonclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","Button Clear is clicked");
                    if (inputdisplay.getText().length() > 0) {
                        CharSequence currenttext = inputdisplay.getText();
                        inputdisplay.setText(currenttext.subSequence(0, currenttext.length() - 1));
                    } else {
                        firstvalue = Double.NaN;
                        secondvalue = Double.NaN;
                        inputdisplay.setText("");
                        outputdisplay.setText("");
                    }
            }
        });

        buttonoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Closing the App!", Toast.LENGTH_LONG).show();
                Log.d("OnCreate","OFF is Clicked");
                finish();
            }
        });

        buttonequal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d("OnCreate","= is clicked");
                if (inputdisplay.getText().toString().isEmpty()) {
                    Log.d("OnCreate","InputDisplay is empty in buttonequal");
                    return;
                } else {
                    mycalculator();
                    outputdisplay.setText(decimalformat.format(firstvalue));
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    //Restricting Database to store values only when all the values available
                    if (R1 !=null && Sb !=null && R2!=null) {

                        Log.d("OnCreate", R1 + Sb + R2 + "=" + R3 + "   " + dtf.format(now));
                        long record_id = db.caldata(R1, Sb, R2, R3, dtf.format(now));
                        if (record_id > 0) {
                            Log.d("OnCreate", "Record Updated In the MyDataBase Successfully");
                            Toast.makeText(getApplicationContext(), R1 + Sb + R2 + "= " + R3, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("buttonequal","Not saved in DB as Secondvalue is not entered");
                    }
                    firstvalue = Double.NaN;
                    R1 = null;
                    R2 = null;
                    Sb = null;
                    currentsymbol = '0';
                }
            }
        });

        buttonall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnCreate","All clear is Clicked");
                firstvalue = Double.NaN;
                secondvalue = Double.NaN;
                inputdisplay.setText("");
                outputdisplay.setText("");
            }
        });
    }

    //For menu bar items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        Log.d("OnCreateOptionsMenu","Menu options Created");
        return true;
    }

    //To open activity 2
    public void openActivity2() {
        Intent intent= new Intent(this,MainActivity2.class);
        startActivity(intent);
        Log.d("openActivity2","Second Activity is Created");
    }

    //set actions to toolbar
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                openActivity2();
                Intent intent= new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("output",(db.getallrecords()));
                startActivity(intent);
                Log.d("onOptionsItemSelected"," Through History second activity opened to view history");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkNotificationPermissionAndNotify() {
        Log.d("checkNotificationPermissionAndNotify","Checking Notification Permissions");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_POST_NOTIFICATIONS);
            } else {
                showNotification();
            }
        } else {
            showNotification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermissionsResult","Show Notification");
                showNotification();
            } else {
                // Handle the case where permission is denied
                Toast.makeText(this, "Notification permission is required to show notifications", Toast.LENGTH_LONG).show();
                Log.d("onRequestPermissionsResult","guide the user to the app settings");
                guideUserToAppSettings();

            }
        }
    }

    private void guideUserToAppSettings() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("Notification permission was denied. You can enable it in app settings.")
                .setPositiveButton("App Settings", (dialog, which) -> {
                    // Intent to open app settings
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    Log.d("guideUserToAppSettings","Notification permission was denied. You can enable it in app settings");

                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("createNotificationChannel","Notification Channel is created");
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notify)
                .setContentTitle("MyCalculator App")
                .setContentText("App Running in Background")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        Log.d("showNotification","Show the explicit notification in App bar");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    //Allowing dot once check condition
    private void dotcheck() {
        String currentText = inputdisplay.getText().toString();
        if (!currentText.contains(".")) {
            inputdisplay.setText(inputdisplay.getText() + ".");
        } else {
            Log.d("dotcheck","Tried to enter another dot");
        }
    }

    private void mycalculator() {

        if (!Double.isNaN(firstvalue)) {
            secondvalue = Double.parseDouble(inputdisplay.getText().toString());
            inputdisplay.setText(null);

            if (currentsymbol == ADDITION) {

                resultvalue = this.firstvalue + secondvalue;
                Sb = "+";

            } else if (currentsymbol == MULTIPLICATION) {

                resultvalue = this.firstvalue * secondvalue;
                Sb = "*";

            } else if (currentsymbol == DIVISION) {

                resultvalue = this.firstvalue / secondvalue;
                Sb = "/";

            } else if (currentsymbol == MODULO) {

                resultvalue = this.firstvalue % secondvalue;
                Sb = "%";


            } else if (currentsymbol == SUBTRACTION) {

                resultvalue = this.firstvalue - secondvalue;
                Sb = "-";
            }

            R1 = String.valueOf(firstvalue);
            R2 = String.valueOf(secondvalue);
            R3 = String.valueOf(resultvalue);

            firstvalue = resultvalue;
        } else {
            try {
                firstvalue = Double.parseDouble(inputdisplay.getText().toString());
            } catch (Exception e) {
                Log.e("mycalculator","Exception occurred");

            }
        }
    }


}



