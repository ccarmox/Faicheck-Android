/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Memoria {

    static void GUARDA(String nombre, ArrayList<String[]> l, Context context) {

        guarda(nombre + "//TAM", l.size(), context);
        for (int i = 0; i < l.size(); i++) {
            guarda(nombre + "//ARRAY//" + i, l.get(i).length, context);

            for (int j = 0; j < l.get(i).length; j++) {
                guarda(nombre + "//ARRAY//" + i + "//" + j, l.get(i)[j], context);
            }
        }

    }

    static ArrayList<String[]> LEE(String nombre, Context context) {
        ArrayList<String[]> lista = new ArrayList<String[]>();

        int tam = lee(nombre + "//TAM", 0, context);

        for (int i = 0; i < tam; i++) {

            int tam2 = lee(nombre + "//ARRAY//" + i, 0, context);
            String[] a = new String[tam2];

            for (int j = 0; j < tam2; j++) {

                String b = lee(nombre + "//ARRAY//" + i + "//" + j, "", context);
                a[j] = b;

            }

            lista.add(a);

        }

        return lista;
    }

    static String lee(String variable, String predefinido, Context context) {

        String valor = "";

        SharedPreferences prefs16 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        valor = prefs16.getString(variable, predefinido);


        return valor;
    }

    static void guarda(String variable, String valor, Context context) {

        SharedPreferences prefs73 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor73 = prefs73.edit();

        editor73.putString(variable, valor);
        editor73.commit();


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
