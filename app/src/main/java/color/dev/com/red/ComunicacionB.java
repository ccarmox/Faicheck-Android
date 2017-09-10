/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ComunicacionB extends Service {


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            String dato = (intent.getStringExtra("DATO"));
            String accion = (intent.getStringExtra("ACCION"));
            Context context = ComunicacionB.this;


            if (accion.equals("LEER")) {

                //Alma.MOSTRAR(new Alma.Hablar.CONVERSACION("",Alma.TIME(),dato,Alma.TIME(),true),context,Alma.LECTURA,true,false);

                //Hablar.MOSTRAR(new Hablar.CONVERSACION("", Alma.TIME(), Alma.LECTURA(dato), Alma.TIME(), true), ComunicacionB.this, true, true);

            }


        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

