/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class NuevaTareaHorario extends AppCompatActivity {

    public int minHora = 12;
    public int minMinuto = 0;
    public int maxHora = 13;
    public int maxMinuto = 0;
    public String color_selec = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_tarea_horario);

        cargarColores();

        final Button boton = (Button) findViewById(R.id.guardar);
        final Spinner listaAsig = (Spinner) findViewById(R.id.lista_asignaturas);


        
        ArrayList<Subject> lista = Subject.LEE("MATERIAS", NuevaTareaHorario.this);


        ArrayList<String> list = new ArrayList<String>();
        list.add("Seleccionar Tarea");
        list.add("Estudiar");
        list.add("Otra tarea");

        for (int i = 0; i < lista.size(); i++) {
            list.add(lista.get(i).getName());
        }

        if (lista.size() == 0) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogo, int which) {

                    dialogo.dismiss();

                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Aún no has iniciado sesión o no tienes ninguna asignatura. Si inicias sesión antes de añadir una tarea podrás seleccionar directamente la asignatura. Si lo haces podremos ofrecerte un mejor servicio.").show();

        }

        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.lista_elemento, list);
        Adapter dataAdapter = new Adapter(NuevaTareaHorario.this, list);
        listaAsig.setAdapter(dataAdapter);


        TimePicker timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setHour(minHora);//.setCurrentHour(12);
        timePicker1.setMinute(minMinuto);//setCurrentMinute(15);
        timePicker1.setIs24HourView(true);

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                minHora = hourOfDay;
                minMinuto = minute;
            }
        });

        TimePicker timePicker2 = (TimePicker) findViewById(R.id.timePicker2);
        timePicker2.setHour(maxHora);//.setCurrentHour(12);
        timePicker2.setMinute(maxMinuto);//setCurrentMinute(15);
        timePicker2.setIs24HourView(true);

        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                maxHora = hourOfDay;
                maxMinuto = minute;
            }
        });


        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText nombre = (EditText) findViewById(R.id.nombre);
                EditText info = (EditText) findViewById(R.id.info);


                CheckBox lu = (CheckBox) findViewById(R.id.lunes);
                CheckBox ma = (CheckBox) findViewById(R.id.martes);
                CheckBox mi = (CheckBox) findViewById(R.id.miercoles);
                CheckBox ju = (CheckBox) findViewById(R.id.jueves);
                CheckBox vi = (CheckBox) findViewById(R.id.viernes);
                CheckBox sa = (CheckBox) findViewById(R.id.sabado);
                CheckBox dom = (CheckBox) findViewById(R.id.domingo);


                String m = String.valueOf(listaAsig.getSelectedItem());
                String n = nombre.getText().toString();
                String i = info.getText().toString();

                int inicio = 0;
                int finalizacion = 0;



                inicio = minHora * 60 + minMinuto;



                finalizacion = maxHora*60 + maxMinuto;

                if (finalizacion > inicio && finalizacion <= 1440 && inicio <= 1440) {

                    boolean c = true;

                    if (lu.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(0, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Lunes esta tarea se solapa con otras tareas");
                        }
                    }

                    if (ma.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(1, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Martes esta tarea se solapa con otras tareas");
                        }
                    }

                    if (mi.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(2, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Miércoles esta tarea se solapa con otras tareas");
                        }
                    }

                    if (ju.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(3, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Jueves esta tarea se solapa con otras tareas");
                        }
                    }

                    if (vi.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(4, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Viernes esta tarea se solapa con otras tareas");
                        }
                    }

                    if (sa.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(5, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Sábado esta tarea se solapa con otras tareas");
                        }
                    }

                    if (dom.isChecked()) {
                        boolean correcto = FragmentoHorario.tarea.nuevaTarea(6, new FragmentoHorario.tarea(n, m, inicio, finalizacion, i, color_selec), NuevaTareaHorario.this);
                        if (!correcto) {
                            c = false;
                            error("El Domingo esta tarea se solapa con otras tareas");
                        }
                    }

                    if (c) {
                        correcto();
                    }

                } else {

                    error("Error al procesar la acción");

                }

            }
        });
    }


    void error(String codigo) {
        Toast.makeText(NuevaTareaHorario.this, codigo,
                Toast.LENGTH_LONG).show();
    }

    void correcto() {
        Toast.makeText(NuevaTareaHorario.this, "Tarea guardada",
                Toast.LENGTH_LONG).show();
    }

    public class Adapter extends BaseAdapter {
        LayoutInflater inflator;
        ArrayList<String> l;

        public Adapter(Context context, ArrayList<String> lista) {
            inflator = LayoutInflater.from(context);
            l = lista;
        }

        @Override
        public int getCount() {
            return l.size();
        }

        @Override
        public Object getItem(int position) {
            return l.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflator.inflate(R.layout.lista_elemento, null);
            TextView tv = (TextView) convertView.findViewById(R.id.text1);
            tv.setText(l.get(position));
            return convertView;
        }
    }

    void cargarColores(){

        final String colores[] = {

                "#ffcdd2",
                "#ef9a9a",
                "#e57373",
                "#ef5350",
                "#f44336",
                "#e53935",
                "#d32f2f",
                "#c62828",
                "#b71c1c",
                "#F8BBD0",
                "#F48FB1",
                "#F06292",
                "#EC407A",
                "#E91E63",
                "#D81B60",
                "#C2185B",
                "#AD1457",
                "#880E4F",
                "#E1BEE7",
                "#CE93D8",
                "#BA68C8",
                "#AB47BC",
                "#9C27B0",
                "#8E24AA",
                "#7B1FA2",
                "#6A1B9A",
                "#4A148C",
                "#D1C4E9",
                "#B39DDB",
                "#9575CD",
                "#7E57C2",
                "#673AB7",
                "#5E35B1",
                "#512DA8",
                "#4527A0",
                "#311B92",
                "#C5CAE9",
                "#9FA8DA",
                "#7986CB",
                "#5C6BC0",
                "#3F51B5",
                "#3949AB",
                "#303F9F",
                "#283593",
                "#1A237E",
                "#BBDEFB",
                "#90CAF9",
                "#64B5F6",
                "#42A5F5",
                "#2196F3",
                "#1E88E5",
                "#1976D2",
                "#1565C0",
                "#0D47A1",
                "#B3E5FC",
                "#81D4FA",
                "#4FC3F7",
                "#29B6F6",
                "#03A9F4",
                "#039BE5",
                "#0288D1",
                "#0277BD",
                "#01579B",
                "#B2EBF2",
                "#80DEEA",
                "#4DD0E1",
                "#26C6DA",
                "#00BCD4",
                "#00ACC1",
                "#0097A7",
                "#00838F",
                "#006064",
                "#B2DFDB",
                "#80CBC4",
                "#4DB6AC",
                "#26A69A",
                "#009688",
                "#00897B",
                "#00796B",
                "#00695C",
                "#004D40",
                "#C8E6C9",
                "#A5D6A7",
                "#81C784",
                "#66BB6A",
                "#4CAF50",
                "#43A047",
                "#388E3C",
                "#2E7D32",
                "#1B5E20",
                "#DCEDC8",
                "#C5E1A5",
                "#AED581",
                "#9CCC65",
                "#8BC34A",
                "#7CB342",
                "#689F38",
                "#558B2F",
                "#33691E",
                "#F0F4C3",
                "#E6EE9C",
                "#DCE775",
                "#D4E157",
                "#CDDC39",
                "#C0CA33",
                "#AFB42B",
                "#9E9D24",
                "#827717",
                "#FFF9C4",
                "#FFF59D",
                "#FFF176",
                "#FFEE58",
                "#FFEB3B",
                "#FDD835",
                "#FBC02D",
                "#F9A825",
                "#F57F17",
                "#FFECB3",
                "#FFE082",
                "#FFD54F",
                "#FFCA28",
                "#FFC107",
                "#FFB300",
                "#FFA000",
                "#FF8F00",
                "#FF6F00",
                "#FFE0B2",
                "#FFCC80",
                "#FFB74D",
                "#FFA726",
                "#FF9800",
                "#FB8C00",
                "#F57C00",
                "#EF6C00",
                "#E65100",
                "#FFCCBC",
                "#FFAB91",
                "#FF8A65",
                "#FF7043",
                "#FF5722",
                "#F4511E",
                "#E64A19",
                "#D84315",
                "#BF360C",
                "#D7CCC8",
                "#BCAAA4",
                "#A1887F",
                "#8D6E63",
                "#795548",
                "#6D4C41",
                "#5D4037",
                "#4E342E",
                "#3E2723",
                "#F5F5F5",
                "#EEEEEE",
                "#E0E0E0",
                "#BDBDBD",
                "#9E9E9E",
                "#757575",
                "#616161",
                "#424242",
                "#212121",
                "#CFD8DC",
                "#B0BEC5",
                "#90A4AE",
                "#78909C",
                "#607D8B",
                "#546E7A",
                "#455A64",
                "#37474F",
                "#263238"

        };

        final LinearLayout colortitulo = (LinearLayout) findViewById(R.id.colorseleccionado);
        colortitulo.setBackgroundColor(Color.parseColor(colores[0]));
        color_selec = colores[0];

        LinearLayout item = (LinearLayout) findViewById(R.id.lista_colores);

        item.removeAllViews();



        for (int i = 0; i < colores.length; i++) {
            View child;
            RelativeLayout tar;


            child = NuevaTareaHorario.this.getLayoutInflater().inflate(R.layout.color, null);
            tar = (RelativeLayout) child.findViewById(R.id.color);
            tar.setBackgroundColor(Color.parseColor(colores[i]));

            final int i2 = i;

            tar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    final LinearLayout colortitulo = (LinearLayout) findViewById(R.id.colorseleccionado);


                    colortitulo.setBackgroundColor(Color.parseColor(colores[i2]));
                    color_selec = colores[i2];

                }
            });

            item.addView(child);

        }

    }


}
