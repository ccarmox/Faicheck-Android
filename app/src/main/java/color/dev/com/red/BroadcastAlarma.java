/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class BroadcastAlarma extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String a = intent.getStringExtra("TAREA");
        String b = intent.getStringExtra("INFO");

        Log.v("NOTIFICACION", "=" + a);
        notificacion(a, b, context);
    }

    public void showNotification(String a, Context context) {
        Intent intent = new Intent(context, FragmentoPrincipal.class);
        PendingIntent pi = PendingIntent.getActivity(context, 12, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Faicheck")
                .setContentText(a);
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(12, mBuilder.build());
    }

    static void notificacion(String titulo, String texto, Context context) {

        Log.v("NOTIFICANDO", texto + "##");
        texto = texto;


        Intent splash2 = new Intent(context, FragmentoPrincipal.class);
        PendingIntent splash = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), splash2, 0);


        int actionBarBackground = context.getResources().getColor(R.color.principal);
        int color = Color.argb(
                250,
                Color.red(actionBarBackground),
                Color.green(actionBarBackground),
                Color.blue(actionBarBackground)
        );


        if (android.os.Build.VERSION.SDK_INT < 26) {

            Notification notification =
                    new NotificationCompat.Builder(context)
                            //.setStyle(new NotificationCompat.BigTextStyle().bigText(desc))
                            .setContentTitle(titulo)

                            .setContentText(texto)
                            .setSubText("Faicheck")
                            .setColor(color)
                            .setSmallIcon(R.drawable.logo)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                            .setPriority(Notification.PRIORITY_DEFAULT)
                            .setContentIntent(splash)
                            .build();


            // Get the notification manager
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);

            // Dispatch the extended notification
            int notificationId = 999;
            notificationManager.notify(notificationId, notification);


        } else {


            NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            String NOTIFICACION_CHANNEL_ID = "color.dev.com.red";

            // create android channel
            NotificationChannel androidChannel = new NotificationChannel(NOTIFICACION_CHANNEL_ID,
                    "Tareas", NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.enableLights(false);
            androidChannel.enableVibration(false);
            androidChannel.setShowBadge(false);
            androidChannel.setLightColor(Color.GREEN);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            mManager.createNotificationChannel(androidChannel);


            Notification.Builder nb = new Notification.Builder(context.getApplicationContext(), NOTIFICACION_CHANNEL_ID)
                    .setContentTitle(titulo)
                    .setContentText(texto)
                    //.setContentText(titulo)
                    //.setContentText(texto)
                    .setSubText("Faicheck")
                    //.setColorized(true)
                    .setColor(color)
                    .setSmallIcon(R.drawable.logo)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                    .setContentIntent(splash);

            mManager.notify(102, nb.build());

        }

    }
}