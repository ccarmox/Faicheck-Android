/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import java.util.ArrayList;

public class XYZConectaB extends Service {


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (intent != null) {
            String dato = (intent.getStringExtra("DATO"));
            String dato2 = (intent.getStringExtra("DATO2"));
            String accion = (intent.getStringExtra("ACCION"));
            String paquete = (intent.getStringExtra("PAQUETE"));
            ArrayList<String> lista = new ArrayList<String>();

            if (dato.equalsIgnoreCase("LISTA")) {
                lista = intent.getStringArrayListExtra("LISTA");
            }

            Log.v("COMUNICACION_B", dato + "::" + accion + "::" + paquete);


            if (accion.equalsIgnoreCase("CERCA")) {

				/*
                Location loc = Mapa.getLocation(10000, this);
				LatLng latlong = new LatLng(loc.getLatitude(), loc.getLongitude());
				final BaseDeDatos.parada parada = BaseDeDatos.getParadaCercana(latlong.latitude,latlong.longitude);
				final BaseDeDatos.marcador[] marcadores = BaseDeDatos.getLineas(parada, this);
				String devolver ="";

				for(int i=0;i<marcadores.length;i++){

					devolver = devolver + xml(marcadores[i]);

				}

				Log.v("devolver", devolver);

				XYZConecta.ENVIA(devolver,"RESULTADO",XYZConecta.PARTICIPANTES[0],this);*/

            }
            if (accion.equalsIgnoreCase("RESULTADO")) {

                if (paquete.equalsIgnoreCase(XYZConecta.PARTICIPANTES[3])) {
                    Log.v("TENGO LAS LINEAS ", "=" + dato);
                    enviarFrase(FragmentoLista.BROADCAST, dato, this);
                }

                if (paquete.equalsIgnoreCase(XYZConecta.PARTICIPANTES[0])) {
                    Log.v("Blue envia esto ", "=" + dato);

                    enviarFrase(lista, dato2, Archivos.BROADCAST, this);


                    //enviarFrase(dato,this);
                }

            }
            if (accion.equalsIgnoreCase("BUSQUEDA")) {

                if (paquete.equalsIgnoreCase(XYZConecta.PARTICIPANTES[2])) {
                    Log.v("Faicheck ", "=" + dato);
                }


            }


        }
        return START_STICKY;
    }

	/*static String xml(BaseDeDatos.marcador m){
		return "<m>"+m.linea.nombre+"</l>"+m.espera;
	}*/

    void enviarFrase(String broadcast, String mensaje, Context context) {
        Log.v("EnviarFrase", ">" + mensaje);

        Intent intent = new Intent();
        intent.setAction(broadcast);
        intent.putExtra("DATO", (mensaje));
        context.sendBroadcast(intent);
    }

    void enviarFrase(ArrayList<String> lista, String mensaje, String broadcast, Context context) {
        Log.v("EnviarFrase", ">" + mensaje);

        Intent intent = new Intent();
        intent.setAction(broadcast);
        intent.putExtra("DATO", "LISTA");
        intent.putExtra("DATO2", mensaje);
        intent.putStringArrayListExtra("LISTA", lista);
        context.sendBroadcast(intent);
    }

    static MarcadorAdapter.marcador marcadorFromXML(String xml) {
        return new MarcadorAdapter.marcador(xml.split("</l>")[0], Integer.parseInt(xml.split("</l>")[1]));
    }

    static ArrayList<MarcadorAdapter.marcador> marcadoresFromXML(String xml) {

        Log.v("XML", "=" + xml);
        String[] l = xml.split("<m>");
        ArrayList<MarcadorAdapter.marcador> li = new ArrayList<MarcadorAdapter.marcador>();

        if (xml.length() > 0) {

            li.add(new MarcadorAdapter.marcador(l[0], 0));

            if (l.length > 1) {

                for (int i = 1; i < l.length; i++) {
                    li.add(marcadorFromXML(l[i]));
                }
            }
        }


        return li;
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

