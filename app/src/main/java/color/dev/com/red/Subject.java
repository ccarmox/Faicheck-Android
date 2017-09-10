/*
 * 	Faicheck - A NON OFFICIAL application to manage the Faitic Platform
 * 	Copyright (C) 2016, 2017 David Ricardo Araújo Piñeiro
 * 	
 * 	This program is free software: you can redistribute it and/or modify
 * 	it under the terms of the GNU General Public License as published by
 * 	the Free Software Foundation, either version 3 of the License, or
 * 	(at your option) any later version.
 * 	
 * 	This program is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 	GNU General Public License for more details.
 * 	
 * 	You should have received a copy of the GNU General Public License
 * 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package color.dev.com.red;

import android.content.Context;

import java.util.ArrayList;

public class Subject {

    private String inturl, intname;

    public Subject(String url, String name) { // 0 1

        inturl = url;
        intname = name;

    }

    public String getURL() {
        return inturl;
    }

    public String getName() {
        return intname;
    }

    public String[] getDatos() {
        return new String[]{inturl, intname};
    }

    public Subject(String[] d) {
        inturl = d[0];
        intname = d[1];
    }

    static void GUARDA(String nombre, ArrayList<Subject> l, Context context) {

        Memoria.guarda(nombre + "//TAM", l.size(), context);
        for (int i = 0; i < l.size(); i++) {

            String d[] = l.get(i).getDatos();

            Memoria.guarda(nombre + "//ARRAY//" + i, d.length, context);

            for (int j = 0; j < d.length; j++) {
                Memoria.guarda(nombre + "//ARRAY//" + i + "//" + j, d[j], context);
            }
        }

    }

    static ArrayList<Subject> LEE(String nombre, Context context) {
        ArrayList<Subject> lista = new ArrayList<Subject>();

        int tam = Memoria.lee(nombre + "//TAM", 0, context);

        for (int i = 0; i < tam; i++) {

            int tam2 = Memoria.lee(nombre + "//ARRAY//" + i, 0, context);
            String[] a = new String[tam2];

            for (int j = 0; j < tam2; j++) {

                String b = Memoria.lee(nombre + "//ARRAY//" + i + "//" + j, "", context);
                a[j] = b;

            }


            lista.add(new Subject(a));

        }

        return lista;
    }

}
