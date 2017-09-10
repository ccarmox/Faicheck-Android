/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.graphics.ColorUtils.calculateContrast;


public class FragmentoHorario extends Fragment {


    Context context;
    View view;
    public int diaActual = 0;
    public int zoom = 4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // = inflater.inflate(R.layout.first_fragment, container, false);


        context = getActivity();

        zoom = lee("ZOOM",4,context);

        view = inflater.inflate(R.layout.fragmento_horario, container, false);

        final RelativeLayout lu = (RelativeLayout) view.findViewById(R.id.lunes);
        final RelativeLayout ma = (RelativeLayout) view.findViewById(R.id.martes);
        final RelativeLayout mi = (RelativeLayout) view.findViewById(R.id.miercoles);
        final RelativeLayout ju = (RelativeLayout) view.findViewById(R.id.jueves);
        final RelativeLayout vi = (RelativeLayout) view.findViewById(R.id.viernes);
        final RelativeLayout sa = (RelativeLayout) view.findViewById(R.id.sabado);
        final RelativeLayout dom = (RelativeLayout) view.findViewById(R.id.domingo);

        lu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(0);

            }
        });

        ma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(1);


            }
        });

        mi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(2);

            }
        });

        ju.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(3);

            }
        });

        vi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(4);

            }
        });

        sa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(5);

            }
        });

        dom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                cargarDia(6);

            }
        });


        cargarDia(dia());


        return view;
    }


    void cargarDia(int dia) {

        diaActual = dia;

        final RelativeLayout lu = (RelativeLayout) view.findViewById(R.id.lunes);
        final RelativeLayout ma = (RelativeLayout) view.findViewById(R.id.martes);
        final RelativeLayout mi = (RelativeLayout) view.findViewById(R.id.miercoles);
        final RelativeLayout ju = (RelativeLayout) view.findViewById(R.id.jueves);
        final RelativeLayout vi = (RelativeLayout) view.findViewById(R.id.viernes);
        final RelativeLayout sa = (RelativeLayout) view.findViewById(R.id.sabado);
        final RelativeLayout dom = (RelativeLayout) view.findViewById(R.id.domingo);

        lu.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));
        ma.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));
        mi.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));
        ju.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));
        vi.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));
        sa.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));
        dom.setBackground(ContextCompat.getDrawable(context, R.drawable.transparente));

        if (dia == 0) {
            lu.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }
        if (dia == 1) {
            ma.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }
        if (dia == 2) {
            mi.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }
        if (dia == 3) {
            ju.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }
        if (dia == 4) {
            vi.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }
        if (dia == 5) {
            sa.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }
        if (dia == 6) {
            dom.setBackground(ContextCompat.getDrawable(context, R.drawable.circulo_gris));
        }


        ArrayList<tarea> tareas = tarea.lee(dia, context);


        cargar(tareas, zoom);

        final RelativeLayout nuevo = (RelativeLayout) view.findViewById(R.id.nuevo);

        nuevo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i2 = new Intent();
                i2.setClass(context, NuevaTareaHorario.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i2);

            }
        });

        final RelativeLayout recargar = (RelativeLayout) view.findViewById(R.id.recargar);

        final int d = diaActual;

        recargar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cargarDia(d);

            }
        });

        final RelativeLayout zoommas = (RelativeLayout) view.findViewById(R.id.zoommas);


        zoommas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                zoom = zoom + 1;
                if(zoom > 100){
                    zoom = 100;
                }

                guarda("ZOOM",zoom,context);
                cargarDia(d);

            }
        });

        final RelativeLayout zoommenos = (RelativeLayout) view.findViewById(R.id.zoommenos);


        zoommenos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                zoom = zoom-1;
                if(zoom<2){
                    zoom = 2;
                }

                guarda("ZOOM",zoom,context);
                cargarDia(d);

            }
        });
    }

    static int dia(int day) {

        switch (day) {


            case Calendar.MONDAY:
                return 0;

            case Calendar.TUESDAY:
                return 1;

            case Calendar.WEDNESDAY:
                return 2;

            case Calendar.THURSDAY:
                return 3;

            case Calendar.FRIDAY:
                return 4;

            case Calendar.SATURDAY:
                return 5;

            case Calendar.SUNDAY:
                return 6;
        }

        return 0;
    }

    static int dia() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {


            case Calendar.MONDAY:
                return 0;

            case Calendar.TUESDAY:
                return 1;

            case Calendar.WEDNESDAY:
                return 2;

            case Calendar.THURSDAY:
                return 3;

            case Calendar.FRIDAY:
                return 4;

            case Calendar.SATURDAY:
                return 5;

            case Calendar.SUNDAY:
                return 6;
        }

        return 0;
    }

    View fondo = null;

    void cargar(ArrayList<tarea> l, int zoom) {

        LinearLayout hora = (LinearLayout) view.findViewById(R.id.horas);

        hora.removeAllViews();

        if (l.size() > 0) {

            int n = l.get(0).inicio;

            if (true) {
                int resto = l.get(0).inicio % 60;
                n = n + resto;
                resto = resto * zoom;
                View child = getActivity().getLayoutInflater().inflate(R.layout.hora, null);
                RelativeLayout tar = (RelativeLayout) child.findViewById(R.id.tarjeta);

                if (resto > 0) {
                    TextView te = (TextView) child.findViewById(R.id.hora);
                    te.setText(getHora(l.get(0).inicio));
                }

                ViewGroup.LayoutParams params = tar.getLayoutParams();
                params.height = (int) (resto);
                tar.setLayoutParams(params);

                hora.addView(child);
            }


            int max = l.get(l.size() - 1).fin;

            while (n <= max) {
                int resto = 60;
                resto = resto * zoom;
                View child = getActivity().getLayoutInflater().inflate(R.layout.hora, null);
                RelativeLayout tar = (RelativeLayout) child.findViewById(R.id.tarjeta);
                TextView te = (TextView) child.findViewById(R.id.hora);
                te.setText(getHora(n));
                ViewGroup.LayoutParams params = tar.getLayoutParams();
                params.height = (int) (resto);
                tar.setLayoutParams(params);

                hora.addView(child);
                n = n + 60;
            }


        }


        LinearLayout item = (LinearLayout) view.findViewById(R.id.lista);

        item.removeAllViews();


        //fondo = getActivity().getLayoutInflater().inflate(R.layout.linear_layout_vertical, null);

        //LinearLayout item = (LinearLayout)fondo.findViewById(R.id.linear);

        tarea anterior = new tarea("", "", 0, 0, "","");

        for (int i = 0; i < l.size(); i++) {
            View child;
            RelativeLayout tar;

            if (i != 0 && l.get(i).inicio != anterior.fin) {
                child = getActivity().getLayoutInflater().inflate(R.layout.tarea_descanso, null);
                tar = (RelativeLayout) child.findViewById(R.id.tarjeta);

                ViewGroup.LayoutParams params = tar.getLayoutParams();
                params.height = (int) ((l.get(i).inicio - anterior.fin) * zoom);
                tar.setLayoutParams(params);

                item.addView(child);

            }

            anterior = l.get(i);

            child = getActivity().getLayoutInflater().inflate(R.layout.tarea, null);
            tar = (RelativeLayout) child.findViewById(R.id.tarjeta);
            RelativeLayout fondo = (RelativeLayout) child.findViewById(R.id.fondo);
            fondo.setBackgroundColor(Color.parseColor(l.get(i).color));
            TextView titulo = (TextView) child.findViewById(R.id.titulo);
            titulo.setText(primeraLetraMayuscula(l.get(i).nombre));
            titulo.setTextColor(letraBlanca(l.get(i).color)?Color.parseColor("#ffffff"):Color.parseColor("#202020"));
            TextView informacion = (TextView) child.findViewById(R.id.informacion);
            informacion.setText(l.get(i).info);

            final RelativeLayout informa_tar = (RelativeLayout) child.findViewById(R.id.informacion_tarjeta);
            informa_tar.setVisibility(View.GONE);

            final RelativeLayout borrar = (RelativeLayout) child.findViewById(R.id.borrar);

            final tarea t = l.get(i);
            final int i2 = i;
            final int diaA = diaActual;

            tar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialogo_informacion_tarea);
                    dialog.setTitle("Tarea...");

                    TextView text = (TextView) dialog.findViewById(R.id.nombre);
                    text.setText(t.nombre);
                    TextView text4 = (TextView) dialog.findViewById(R.id.materia);
                    text4.setText(t.materia);
                    TextView text2 = (TextView) dialog.findViewById(R.id.info);
                    text2.setText(t.info);
                    TextView text3 = (TextView) dialog.findViewById(R.id.hora);
                    text3.setText("De " + getHora(t.inicio) + " a " + getHora(t.fin));

                    Button dialogButton = (Button) dialog.findViewById(R.id.cerrar);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    RelativeLayout dialogButton2 = (RelativeLayout) dialog.findViewById(R.id.borrar);
                    // if button is clicked, close the custom dialog
                    dialogButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogo, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked


                                            Log.v("borrar", "=" + i2);
                                            tarea.borra(diaA, i2, context);
                                            cargarDia(diaA);

                                            dialog.dismiss();

                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("¿Estás seguro de que quieres eliminar esta tarea?").setPositiveButton("Sí", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();


                        }
                    });

                    dialog.show();

                        /*

                        if(informa_tar.getVisibility()==View.GONE) {
                            informa_tar.setVisibility(View.VISIBLE);
                        }else{
                            informa_tar.setVisibility(View.GONE);
                        }

*/
                }
            });



                /*borrar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        Log.v("borrar","="+i2);
                        tarea.borra(diaA,i2,context);
                        cargarDia(diaA);

                    }
                });
*/


            ViewGroup.LayoutParams params = tar.getLayoutParams();
            params.height = (int) ((l.get(i).fin - l.get(i).inicio) * zoom);
            tar.setLayoutParams(params);

            item.addView(child);
        }

        //itemP.addView(fondo);

    }

    static String primeraLetraMayuscula(String original){
            if (original == null || original.length() == 0) {
                return original;
            }
            if(original.length()>1) {
                return original.substring(0, 1).toUpperCase() + original.substring(1);
            }else{
                return original.toUpperCase();
            }

    }

    static boolean letraBlanca(String color){
        double n = calculateContrast(Color.parseColor("#ffffff"), Color.parseColor(color));
        Log.v("n","="+n);
        if(n>4){
            return true;
        }else{
            return false;
        }
    }

    static String getHora(int i) {
        int a = i % 60;
        String a2 = "" + a;
        if (a2.length() != 2) {
            a2 = "0" + a2;
        }
        int b = (i - a) / 60;
        String b2 = "" + b;
        if (b2.length() != 2) {
            b2 = "0" + b2;
        }
        return b2 + ":" + a2;
    }

    public static class tarea {
        String nombre;
        String info;
        int inicio;
        int fin;
        String materia;
        String color;

        tarea(String nombre, String materia, int inicio, int fin, String info, String color) {
            this.fin = fin;
            this.inicio = inicio;
            this.nombre = nombre;
            this.info = info;
            this.materia = materia;
            this.color = color;
        }

        static int integer(String a) {
            return Integer.valueOf(a.split("[.]")[0]);
        }

        static String getXML(tarea a) {
            return "<n>" + a.nombre + "</n><m>" + a.materia + "</m><in>" + a.info + "</in><c>" + a.color + "</c><ii>" + a.inicio + "</ii><fi>" + a.fin + "</fi>";
        }

        String getXML() {
            return "<n>" + nombre + "</n><m>" + materia + "</m><in>" + info + "</in><c>" + color + "</c><ii>" + inicio + "</ii><fi>" + fin + "</fi>";
        }

        static tarea getTareaFromXML(String texto) {
            String n = getEtiqueta(texto, "n");
            String c = "#1975ff";

            try {
                c = getEtiqueta(texto, "c");
            }catch (Exception e){

            }

            String m = "";
            try {
                m = getEtiqueta(texto, "m");
            } catch (Exception e) {
            }
            String in = getEtiqueta(texto, "in");
            String ii = getEtiqueta(texto, "ii");
            String fi = getEtiqueta(texto, "fi");

            return new tarea(n, m, Integer.valueOf(ii), Integer.valueOf(fi), in,c);
        }

        static String getEtiqueta(String texto, String e) {
            return (" " + texto.split("<" + e + ">")[1]).split("</" + e + ">")[0].trim();
        }

        static ArrayList<tarea> getArrayListTareas(String texto) {
            ArrayList<tarea> l = new ArrayList<tarea>();
            String[] sp = texto.split("<n>");
            if (sp.length > 1) {
                for (int i = 1; i < sp.length; i++) {
                    l.add(getTareaFromXML("<n>" + sp[i]));
                }
            }

            return l;
        }

        static String getXML(ArrayList<tarea> l) {
            String r = "";

            for (int i = 0; i < l.size(); i++) {
                r = r + "" + l.get(i).getXML();
            }

            return r;
        }

        static void guarda(int dia, ArrayList<tarea> l, Context context) {
            Memoria.guarda("HORARIO" + dia, getXML(l), context);
        }

        static ArrayList<tarea> lee(int dia, Context context) {
            String t = Memoria.lee("HORARIO" + dia, "", context);
            return getArrayListTareas(t);
        }

        static void borra(int dia, int posicion, Context context) {
            ArrayList<tarea> l = lee(dia, context);
            l.remove(posicion);
            guarda(dia, l, context);
        }

        static boolean nuevaTarea(int dia, tarea tar, Context context) {
            ArrayList<tarea> l = lee(dia, context);
            ArrayList<tarea> r = new ArrayList<tarea>();

            Log.v("Nueva TAREA","INI" + tar.inicio + "FIN" + tar.fin);

            boolean g = false;
            for (int i = 0; i < l.size(); i++) {

                if (!g) {
                    if (i == 0) {

                        if (tar.fin <= l.get(i).inicio) {
                            r.add(tar);
                            g = true;
                        }

                    } else {

                        if (l.get(i - 1).fin <= tar.inicio && l.get(i).inicio >= tar.fin) {
                            r.add(tar);
                            g = true;
                        }
                    }

                }

                r.add(l.get(i));

            }

            if (!g) {
                if (l.size() == 0) {
                    r.add(tar);
                    g = true;
                } else {
                    if (tar.inicio >= l.get(l.size() - 1).fin) {
                        r.add(tar);
                        g = true;
                    } else {
                        return false;
                    }
                }
            }

            guarda(dia, r, context);

            renovarRecordatorios(context);

            return true;
        }

        public static void eliminarHorario(Context context){
            for(int i=0;i<7;i++){

                ArrayList<tarea> r = new ArrayList<tarea>();
                guarda(i, r, context);

            }
        }

        static final int id = 9876;

        public static void renovarRecordatorios(Context context) {

            for (int i = 0; i < 1000; i++) {
                Intent ai = new Intent(context, BroadcastAlarma.class);

                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, i, ai,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager ame = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                ame.cancel(pendingIntent2);

            }
            int id = 0;

            for (int j = 0; j < 7; j++) {

                ArrayList<tarea> l = lee(j, context);

                for (int i = 0; i < l.size(); i++) {

                    int a = l.get(i).inicio % 60;

                    int b = (l.get(i).inicio - a) / 60;


                    Calendar cal = Calendar.getInstance();
                    int t = dia(cal.get(Calendar.DAY_OF_WEEK));
                    int t2 = j;

                    int f = t2 - t;
                    if (f < 0) {
                        f = f + 7;
                    } else{
                        if(f==0){
                            if(b<cal.get(Calendar.HOUR_OF_DAY)){
                                f = f+7;
                            }else{
                                if(b==cal.get(Calendar.HOUR_OF_DAY)){
                                    if(a<=cal.get(Calendar.MINUTE)){
                                        f = f+7;
                                    }
                                }
                            }
                        }
                }

                    cal.add(Calendar.HOUR_OF_DAY, 24 * f);
                    //cal.set(Calendar.DAY_OF_WEEK,adaptadorCalendarioDia(j));
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.HOUR_OF_DAY, b);
                    cal.set(Calendar.MINUTE, a);

                    Log.v("Calendar", cal.toString());

                    Intent intent = new Intent(context, BroadcastAlarma.class);
                    intent.putExtra("TAREA", l.get(i).nombre);
                    intent.putExtra("INFO", l.get(i).info);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    am.setRepeating(am.RTC_WAKEUP, cal.getTimeInMillis(), am.INTERVAL_DAY * 7, pendingIntent);

                    id++;
                }

            }

        }

        static int adaptadorCalendarioDia(int d) {
            if (d == 0) {
                return Calendar.MONDAY;
            }
            if (d == 1) {
                return Calendar.TUESDAY;
            }
            if (d == 2) {
                return Calendar.WEDNESDAY;
            }
            if (d == 3) {
                return Calendar.THURSDAY;
            }
            if (d == 4) {
                return Calendar.FRIDAY;
            }
            if (d == 5) {
                return Calendar.SATURDAY;
            }
            if (d == 6) {
                return Calendar.SUNDAY;
            }

            return Calendar.MONDAY;
        }

    }

    static int lee(String variable, int predefinido, Context context) {

        int valor = 0;

        SharedPreferences prefs16 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        valor = prefs16.getInt(variable, predefinido);


        return valor;
    }

    static void guarda(String variable, int valor, Context context) {

        SharedPreferences prefs73 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor73 = prefs73.edit();

        editor73.putInt(variable, valor);
        editor73.commit();


    }



}
