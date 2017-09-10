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

public class Comunicacion extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("COMUNICACION!!", intent.getAction());

        if (VALIDO(intent.getAction()) >= 0) {
            String dato = (intent.getStringExtra("DATO"));
            String accion = (intent.getStringExtra("ACCION"));


            Log.v("COMUNICACION_DATO", dato);
            Log.v("COMUNICACION_ACCION", accion);


            Intent i = new Intent(context, ComunicacionB.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("DATO", dato);
            i.putExtra("ACCION", accion);
            context.startService(i);


        }


    }

    static void ENVIA(String texto, String accion, int paquete, Context context) {

        if (EXISTE(PARTICIPANTES[paquete].paquete, context)) {

            final Intent intent = new Intent();
            intent.setAction(PARTICIPANTES[paquete].paquete);
            intent.putExtra("DATO", texto);
            intent.putExtra("ACCION", accion);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setComponent(
                    new ComponentName(PARTICIPANTES[paquete].paquete, PARTICIPANTES[paquete].paquete + ".Comunicacion"));
            context.sendBroadcast(intent);

        } else {
            GOOGLE_PLAY(PARTICIPANTES[paquete].paquete, context);
        }
    }


    static RECETA PARTICIPANTES[] = {
            //CONTIENE // NO CONTIENE // ACCION
            new RECETA("com.petra.persona.her", "buscar", "", "BUSCAR"),
            new RECETA("com.petra.persona.her", "leer", "", "LEER"),
            new RECETA("es.androidtr.web.androidtr", "androidtr", "", "GENERAR_NOTICIAS"),
            new RECETA("u.vigo.color.dev.com.red.color.dev.com.red", "color.dev.com.red", "", "ABRIR")
    };

    static int VALIDO(String t) {
        int r = -1;

        for (int i = 0; i < PARTICIPANTES.length; i++) {
            if (PARTICIPANTES[i].paquete.equals(t)) {
                r = i;
            }
        }

        return r;
    }

    static private boolean EXISTE(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    static void GOOGLE_PLAY(String uri, Context context) {

        NOTIFICAR(context);

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
                                .getDrawable(R.drawable.logo)).getBitmap()))
                        .setContentTitle("Error")
                        .setContentText("Es necesario que instales una aplicación antes de seguir")
                        .setContentInfo("")
                        .setTicker("No tienes la aplicación necesaria instalada");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1993, mBuilder.build());

    }

    static String APLICACIONES_EXTERNAS(String frase, Context context) {

        String resultado = frase;
        frase = " " + frase.toLowerCase() + " ";
        boolean r = false;


        for (int i = 0; i < PARTICIPANTES.length; i++) {
            if (!r) {

                if (PARTICIPANTES[i].contiene.length() > 0) {
                    if (frase.contains(" " + PARTICIPANTES[i].contiene + " ")) {

                        if ((PARTICIPANTES[i].nocontiene.length() == 0) || (PARTICIPANTES[i].nocontiene.length() > 0) && (!(frase.contains(" " + PARTICIPANTES[i].contiene + " ")))) {


                            ENVIA(resultado, PARTICIPANTES[i].accion, i, context);
                            r = true;
                            resultado = "Vale";

                        }

                    }
                }


            }
        }

        return resultado;
    }

    static public class RECETA {

        String paquete;
        String contiene;
        String nocontiene;
        String accion;

        RECETA(String paquete, String contiene, String nocontiene, String accion) {
            this.paquete = paquete.trim();
            this.contiene = contiene.trim();
            this.nocontiene = nocontiene.trim();
            this.accion = accion.trim();
        }
    }


}

