package com.android.calculator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * NotificationHelper handles showing notifications when the app opens.
 * Features:
 * Creates notification channel for Android O+.
 * Checks and requests notification permission for Android 13+.
 * Displays a one-time, non-persistent welcome notification.
 * Shows a dialog directing user to app settings if permission is denied.
 */
public class NotificationHelper {
    private static final String CHANNEL_ID = "channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 2;
    private final Context context;
    private final AppLogger logger = new AppLogger("NotificationHelper");

    /**
     * Initializes the NotificationHelper with app context.
     * @param context Application context
     */
    public NotificationHelper(Context context) {
        this.context = context;
        logger.d("NotificationHelper initialized");
    }

    /** Creates notification channel for Android O and above. */
    public void createNotificationChannel() {
        logger.d("Creating notification channel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(description);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                logger.d("Notification channel created: " + CHANNEL_ID);
            } else {
                logger.e("NotificationManager is null — cannot create channel", (Throwable) null);
            }
        }
    }

    /**
     * Checks notification permission (for Android 13+), requests if not granted,
     * and shows notification once when app opens.
     * @param activity Activity context required for permission request
     */
    public void checkPermissionAndNotify(AppCompatActivity activity) {
        logger.d("Checking notification permission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                logger.d("Permission not granted — requesting permission");
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_POST_NOTIFICATIONS
                );
            } else {
                logger.d("Permission already granted — showing notification");
                showNotification();
            }
        } else {
            logger.d("Pre-Android 13 — permission not required, showing notification");
            showNotification();
        }
    }

    /**
     * Handles permission result from Activity request.
     * @param grantResults Permission grant results
     */
    public void handlePermissionResult(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            logger.d("Notification permission granted");
            showNotification();
        } else {
            logger.d("Notification permission denied — showing dialog");
            showPermissionDeniedDialog();
        }
    }

    /** Shows a dialog directing user to app settings if permission denied */
    private void showPermissionDeniedDialog() {
        logger.d("Displaying permission denied dialog");
        new AlertDialog.Builder(context)
                .setTitle("Permission Denied")
                .setMessage("Notification permission was denied. Enable it in app settings.")
                .setPositiveButton("App Settings", (dialog, which) -> {
                    logger.d("User clicked App Settings — opening app settings");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    logger.d("User cancelled permission dialog");
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    /** Shows a one-time, non-persistent welcome notification */
    public void showNotification() {
        logger.d("Preparing to show notification");

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notify)
                .setContentTitle("Calculator App")
                .setContentText("Welcome! The calculator is ready to use.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // dismiss when tapped

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
            logger.d("Notification displayed with ID: " + NOTIFICATION_ID);
        } else {
            logger.e("NotificationManager is null — cannot show notification", (Throwable) null);
        }
    }
}
