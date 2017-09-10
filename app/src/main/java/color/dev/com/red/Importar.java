/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Importar extends AppCompatActivity {

    public String imp = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importar);


        cargar();

    }

    void cargar() {

        Uri data1 = getIntent().getData();

        if (data1 != null) {
            Log.v("DATA", "=" + data1);

            if (PERMISO()) {
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(data1);//new FileInputStream(new File(data1));


                    Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                    String t = s.hasNext() ? s.next() : "";
                    Log.v("t", "=" + t);

                    imp = t;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.v("DATA", "NULL");
        }

        if (imp.length() > 0) {

            final String schedu[] = imp.split("\"schedulename\":\"");

            final Spinner lista = (Spinner) findViewById(R.id.lista_asignaturas);

            ArrayList<String> list = new ArrayList<String>();
            list.add("Importar todo");

            for (int i = 1; i < schedu.length; i++) {
                list.add("Importar: " + schedu[i].split("\"")[0].trim());
            }

            Adapter dataAdapter = new Adapter(Importar.this, list);
            lista.setAdapter(dataAdapter);

            final RelativeLayout cargando = (RelativeLayout) findViewById(R.id.cargando);
            final RelativeLayout opciones = (RelativeLayout) findViewById(R.id.modos);

            cargando.setVisibility(View.GONE);

            final Button boton = (Button) findViewById(R.id.guardar);
            boton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cargando.setVisibility(View.VISIBLE);
                    opciones.setVisibility(View.GONE);
                    boton.setEnabled(false);
                    new Thread(new Runnable() {
                        public void run() {

                            if (lista.getSelectedItemPosition() == 0) {
                                for (int i = 1; i < schedu.length; i++) {
                                    anhadirSchedu(schedu[i]);
                                }
                            } else {
                                anhadirSchedu(schedu[lista.getSelectedItemPosition()]);
                            }

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    boton.setEnabled(true);
                                    error("LISTO!");

                                    opciones.setVisibility(View.VISIBLE);
                                    cargando.setVisibility(View.GONE);
                                }
                            });

                        }
                    }).start();

                }
            });

        }
    }


    int INT(String a) {
        return Integer.parseInt(a);
    }

    void anhadirSchedu(String s) {


        String l[] = s.split("\\{");
        for (int i = 1; i < l.length; i++) {

            int r = INT(sacarEtiqueta(l[i], "colorr"));
            int g = INT(sacarEtiqueta(l[i], "colorg"));
            int b = INT(sacarEtiqueta(l[i], "colorb"));
            String color = rgbToHex(r, g, b);
            int inicio = INT(sacarEtiqueta(l[i], "minutestart"));
            int fin = INT(sacarEtiqueta(l[i], "minuteend"));
            int dia = INT(sacarEtiqueta(l[i], "day"));
            final String evento = sacarEtiqueta(l[i], "eventname");

            Log.v("anhadir", "=" + evento);

            boolean correcto = FragmentoHorario.tarea.nuevaTarea(dia, new FragmentoHorario.tarea(evento, "", inicio, fin, "", color), Importar.this);
            if (!correcto) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        error("No se ha podido guardar: " + evento);

                    }
                });
            }

        }
        try {
        } catch (Exception e) {
            Log.v("error", "2345");
        }

    }

    void error(String codigo) {
        Toast.makeText(Importar.this, codigo,
                Toast.LENGTH_LONG).show();
    }

    String sacarEtiqueta(String t, String etiqueta) {
        String r = "";
        try {

            String l = t.split("\"" + etiqueta + "\":")[1];

            Log.v("L", "=" + l);

            if (("" + l.charAt(0)).equals("\"")) {
                return l.split("\"")[1];
            } else {
                return (l + "}").split("\\}")[0].split("\"")[0].split(",")[0].trim();
            }


        } catch (Exception e) {

        }
        return r;
    }

    String rgbToHex(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    boolean PERMISO() {

        boolean permiso = false;

        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Faicheck", "Permission is granted");
                permiso = true;
            } else {

                Log.v("Faicheck", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Faicheck", "Permission is granted");
            permiso = true;
        }

        return permiso;
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

    static class tareaAmpliada {
        FragmentoHorario.tarea tarea;
        int dia;

        tareaAmpliada(FragmentoHorario.tarea tarea, int dia) {
            this.dia = dia;
            this.tarea = tarea;
        }
    }

    public static String exportar(Context context) {

        String r = "";
        ArrayList<tareaAmpliada> l = new ArrayList<tareaAmpliada>();
        for (int i = 0; i < 7; i++) {
            ArrayList<FragmentoHorario.tarea> l2 = FragmentoHorario.tarea.lee(i, context);
            for (int u = 0; u < l2.size(); u++) {
                l.add(new tareaAmpliada(l2.get(u), i));
            }
        }

        if (l.size() > 0) {
            for (int i = 0; i < (l.size() - 1); i++) {
                int cr = Integer.valueOf(l.get(i).tarea.color.substring(1, 3), 16);
                int cg = Integer.valueOf(l.get(i).tarea.color.substring(3, 5), 16);
                int cb = Integer.valueOf(l.get(i).tarea.color.substring(5, 7), 16);

                r = r + "{\"colora\":255,\"colorr\":" + cr + ",\"colorb\":" + cb + ",\"minuteend\":" + l.get(i).tarea.fin + ",\"minutestart\":" + l.get(i).tarea.inicio + ",\"colorg\":" + cg + ",\"day\":" + l.get(i).dia + ",\"eventname\":\"" + l.get(i).tarea.nombre + "\"},";
            }
            int cr = Integer.valueOf(l.get(l.size() - 1).tarea.color.substring(1, 3), 16);
            int cg = Integer.valueOf(l.get(l.size() - 1).tarea.color.substring(3, 5), 16);
            int cb = Integer.valueOf(l.get(l.size() - 1).tarea.color.substring(5, 7), 16);

            r = r + "{\"colora\":255,\"colorr\":" + cr + ",\"colorb\":" + cb + ",\"minuteend\":" + l.get(l.size() - 1).tarea.fin + ",\"minutestart\":" + l.get(l.size() - 1).tarea.inicio + ",\"colorg\":" + cg + ",\"day\":" + l.get(l.size() - 1).dia + ",\"eventname\":\"" + l.get(l.size() - 1).tarea.nombre + "\"}";

            r = r + "]}]}";

            r = "{\"schedules\":[{\"schedulename\":\"Horario\",\"events\":[" + r;


            boolean permiso = false;

            if (Build.VERSION.SDK_INT >= 23) {
                if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("Faicheck", "Permission is granted");
                    permiso = true;
                } else {

                    permiso = false;
                    return "No tengo permiso";

                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v("Faicheck", "Permission is granted");
                permiso = true;
            }


            if (permiso) {
                try {

                    File folder = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "Faicheck");
                    boolean success = true;
                    if (!folder.exists()) {
                        File folder2 = new File(Environment.getExternalStorageDirectory() +
                                File.separator + "Faicheck");
                        if (!folder2.exists()) {
                            success = folder.mkdirs();
                        }
                    }


                    folder = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "Faicheck" + File.separator + "Horario");

                    if (!folder.exists()) {
                        File folder2 = new File(Environment.getExternalStorageDirectory() +
                                File.separator + "Faicheck" + File.separator + "Horario");
                        if (!folder2.exists()) {
                            success = folder.mkdirs();
                        }
                    }


                    String path = Environment.getExternalStorageDirectory() +
                            File.separator + "Faicheck" + File.separator + "Horario" + File.separator;


                    FileWriter out = new FileWriter(new File(path, "horario_exportado.faicheck"));
                    out.write(r);
                    out.close();

                    Log.v("LOCALIZACION", "=" + path + "horario_exportado.faicheck");

                    /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri screenshotUri = Uri.parse(path + "horario_exportado.faicheck");
                    sharingIntent.setType("text/+");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    context.startActivity(Intent.createChooser(sharingIntent, "Compartir el horario con:"));
*/

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    File file = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "Faicheck" + File.separator + "Horario" + File.separator + "horario_exportado.faicheck");
                    Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // Workaround for Android bug.
                    // grantUriPermission also needed for KITKAT,
                    // see https://code.google.com/p/android/issues/detail?id=76683
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }

                    return "";

                } catch (IOException e) {
                    Log.v("ERROR", "ERROR EXPORTANDO");
                    return "Error exportando la información";
                }

            }

        }

        return "No hay nada que exportar";

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.v("Permiso", "concedido");

            cargar();


        } else {

            Log.e("error", "Permiso denegado");
        }


    }

}
