/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class XYZConecta extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("COMUNICACION!!", intent.getAction());

        if (VALIDO(intent.getAction())) {
            String dato = (intent.getStringExtra("DATO"));
            String dato2 = (intent.getStringExtra("DATO2"));
            String accion = (intent.getStringExtra("ACCION"));
            String paquete = (intent.getStringExtra("PAQUETE"));


            Log.v("COMUNICACION_DATO", dato + "::" + accion + "::" + paquete);


            Intent i = new Intent(context, XYZConectaB.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (dato.equalsIgnoreCase("LISTA")) {
                i.putStringArrayListExtra("LISTA", intent.getStringArrayListExtra("LISTA"));
            }

            i.putExtra("DATO", dato);
            i.putExtra("DATO2", dato2);
            i.putExtra("ACCION", accion);
            i.putExtra("PAQUETE", paquete);
            context.startService(i);


        }


    }

    static void ENVIA(String texto, String dato2, String accion, String paqueteYo, String paquete, Context context) {

        if (EXISTE(paquete, context)) {

            final Intent intent = new Intent();
            intent.setAction(paquete);
            intent.putExtra("DATO", texto);
            intent.putExtra("DATO2", dato2);
            intent.putExtra("ACCION", accion);
            intent.putExtra("PAQUETE", paqueteYo);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setComponent(
                    new ComponentName(paquete, paquete + ".XYZConecta"));
            context.sendBroadcast(intent);

            Log.v("ENVIAD0", texto + "::" + accion + "::" + paqueteYo + "::" + paquete);

        } else {
            //GOOGLE_PLAY(paquete,context);
        }
    }

    static void ENVIA(ArrayList<String> texto, String dato2, String accion, String paqueteYo, String paquete, Context context) {

        if (EXISTE(paquete, context)) {

            final Intent intent = new Intent();
            intent.setAction(paquete);
            intent.putExtra("DATO", "LISTA");
            intent.putExtra("DATO2", dato2);
            intent.putStringArrayListExtra("LISTA", texto);
            intent.putExtra("ACCION", accion);
            intent.putExtra("PAQUETE", paqueteYo);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setComponent(
                    new ComponentName(paquete, paquete + ".XYZConecta"));
            context.sendBroadcast(intent);

            Log.v("ENVIAD0", texto + "::" + accion + "::" + paqueteYo + "::" + paquete);

        } else {
            //GOOGLE_PLAY(paquete,context);
        }
    }


    static String PARTICIPANTES[] = {
            "color.dev.com.blue",
            "es.androidtr.web.androidtr",
            "color.dev.com.red",
            "color.dev.com.green"
    };

    static boolean VALIDO(String t) {
        boolean f = false;

        for (int i = 0; i < PARTICIPANTES.length; i++) {
            if (PARTICIPANTES[i].equals(t)) {
                f = true;
            }
        }

        return f;
    }

    static boolean EXISTE(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    static void GOOGLE_PLAY(String uri, Context context) {

        //NOTIFICAR(context);

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + uri)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + uri)));
        }

    }

    static void NOTIFICAR(Context context) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setLargeIcon((((BitmapDrawable) context.getResources()
                                .getDrawable(R.drawable.parada)).getBitmap()))
                        .setContentTitle("Error")
                        .setContentText("Es necesario que instales una aplicación antes de seguir")
                        .setContentInfo("")
                        .setTicker("No tienes la aplicación necesaria instalada");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1993, mBuilder.build());

    }


}

