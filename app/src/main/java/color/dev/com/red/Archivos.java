/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Archivos extends AppCompatActivity {

    COMUNICACION comunicacion = null;
    final static String BROADCAST = "u.vigo.color.dev.com.red.color.dev.com.red.busqueda";


    ListaAdaptador adaptador;
    ListView listView;
    Faitic faitic;
    String materia;
    Boolean offline;
    List<ARCHIVO> lista_archivos = new ArrayList<ARCHIVO>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.archivos);

        Intent intent = getIntent();

        int tam = intent.getIntExtra("ATAM", 0);
        offline = intent.getBooleanExtra("OFFLINE", false);
        materia = intent.getStringExtra("MATERIA").replace("-", " ");
        Log.v("MAAATERIA", materia);
        faitic = (Faitic) intent.getSerializableExtra("FAITIC");
        ArrayList<FileFromURL> lista = new ArrayList<FileFromURL>();

        for (int i = 0; i < tam; i++) {
            lista.add(new FileFromURL(intent.getStringArrayExtra("AMAT" + i)));

            Log.v("REC ARCHIVO", intent.getStringArrayExtra("AMAT" + i)[0]);
        }


        listView = (ListView) findViewById(R.id.lista);

        adaptador = new ListaAdaptador(getApplicationContext(), R.layout.archivo, offline);
        listView.setAdapter(adaptador);

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adaptador);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.v("HE PINCHADO AQUII","DE VERDAD");
                final ARCHIVO arch = adaptador.getItem(position);
                final String[] card = arch.datos;

                if(arch.descargado){
                    ABRIR(card[0]);
                }else {
                    if (PERMISO()) {
                        DESCARGAR_ARCHIVO(card, faitic, materia);
                    } else {
                        SNACKBAR("No tengo permiso para escribir en el dispositivo");
                    }
                }
            }
        });
        */

        //for(int i=0;i<lista.size();i++){
        //    adaptador.add(new ARCHIVO(lista.get(i),ESTA_DESCARGADO(lista.get(i),materia)));
        //}

        //List<ARCHIVO> lista_archivos = new ArrayList<ARCHIVO>();
        for (int i = 0; i < lista.size(); i++) {
            lista_archivos.add(new ARCHIVO(lista.get(i), ESTA_DESCARGADO(lista.get(i), materia)));
        }

        adaptador.carga_lista(lista_archivos);

        ImageButton buscar = (ImageButton) findViewById(R.id.buscar);
        final EditText texto_buscar = (EditText) findViewById(R.id.texto_busqueda);

        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                BUSQUEDA(texto_buscar.getText().toString());

            }
        });

        texto_buscar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    BUSQUEDA(texto_buscar.getText().toString());

                    return true;
                }
                return false;

            }
        });

        texto_buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code

                BUSQUEDA_BASICA(texto_buscar.getText().toString());


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        if (comunicacion == null) {
            comunicacion = new COMUNICACION();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST);
            this.registerReceiver(comunicacion, intentFilter);
        }

    }

    void BUSQUEDA_BASICA(final String b) {

        for (int i = 0; i < lista_archivos.size(); i++) {
            Log.v("ARCHIV0", "[" + i + "]=" + lista_archivos.get(i).datos.getURL());
        }

        List<ARCHIVO> l = BUSCAR(lista_archivos, b);


        adaptador.carga_lista(l);


        RelativeLayout buscar_api = (RelativeLayout) findViewById(R.id.buscar_api);
        buscar_api.setVisibility(View.GONE);


        if (l.size() > 0) {
        } else {
            if (!XYZConecta.EXISTE(XYZConecta.PARTICIPANTES[0], this)) {
                buscar_api.setVisibility(View.VISIBLE);
                ImageView api = (ImageView) findViewById(R.id.abrir_google_play);
                api.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //Comunicacion.APLICACIONES_EXTERNAS("buscar "+b,Archivos.this);
                        XYZConecta.GOOGLE_PLAY(XYZConecta.PARTICIPANTES[0], Archivos.this);
                    }
                });
            }
        }

    }

    void BUSQUEDA(final String b) {

        final EditText texto_buscar = (EditText) findViewById(R.id.texto_busqueda);

        texto_buscar.setText("");

        if (XYZConecta.EXISTE(XYZConecta.PARTICIPANTES[0], this)) {

            ArrayList<String> l = new ArrayList<String>();

            for (int i = 0; i < lista_archivos.size(); i++) {
                Log.v("Archivo", "=" + lista_archivos.get(i).datos.getURL() + "###" + lista_archivos.get(i).datos.getFileDestination());
                l.add(lista_archivos.get(i).datos.getURL());
                l.add(lista_archivos.get(i).datos.getFileDestination());
            }

            XYZConecta.ENVIA(l, b, "BUSQUEDA", XYZConecta.PARTICIPANTES[2], XYZConecta.PARTICIPANTES[0], this);

        } else {
            BUSQUEDA_BASICA(b);
        }


    }

    void BUSQUEDA_deprecated(final String b) {

        for (int i = 0; i < lista_archivos.size(); i++) {
            Log.v("ARCHIV0", "[" + i + "]=" + lista_archivos.get(i).datos.getURL());
        }

        List<ARCHIVO> l = BUSCAR(lista_archivos, b);

        RelativeLayout buscar_api = (RelativeLayout) findViewById(R.id.buscar_api);

        if (l.size() > 0) {
            buscar_api.setVisibility(View.GONE);
        } else {
            buscar_api.setVisibility(View.VISIBLE);
            /*Button api = (Button) findViewById(R.id.busqueda_api);
            api.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    Comunicacion.APLICACIONES_EXTERNAS("buscar "+b,Archivos.this);

                }
            });*/
        }

        adaptador.carga_lista(l);


    }

    List<ARCHIVO> BUSCAR(List<ARCHIVO> l, String busca) {
        List<ARCHIVO> l2 = new ArrayList<ARCHIVO>();
        busca = busca.toLowerCase();

        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).datos.getURL().toLowerCase().contains(busca)) {
                l2.add(l.get(i));
            }
        }

        return l2;
    }

    void ABRIR(String url) {

        url = materia + File.separator + url;
        File file = new File(PATH() + File.separator + url);
        Uri uri = FileProvider.getUriForFile(Archivos.this, Archivos.this.getApplicationContext().getPackageName() + ".provider", file);

        try {


            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Log.v("URI", uri.toString());

            Archivos.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            SNACKBAR("No se reconoce el tipo de archivo");
        }
    }

    class ListaAdaptador extends ArrayAdapter<ARCHIVO> {

        private TextView chatText;
        private List<ARCHIVO> lista_materias = new ArrayList<ARCHIVO>();
        private Context context;
        private boolean listar_offline = false;

        @Override
        public void add(ARCHIVO object) {

            lista_materias.add(object);
            listView.smoothScrollToPosition(0);
            super.add(object);
        }

        public void actualizar(ARCHIVO arch) {
            int n = 0;
            for (int i = 0; i < lista_materias.size(); i++) {
                if (lista_materias.get(i).datos == arch.datos) {
                    lista_materias.set(i, arch);
                    n = i;
                }
            }

            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            //
            listView.smoothScrollToPosition(n);
            // listView.setSelection(n);
        }

        public void actualizar(List<ARCHIVO> l) {

            lista_materias.clear();
            for (int i = 0; i < l.size(); i++) {
                lista_materias.add(l.get(i));
            }

            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            //
            listView.smoothScrollToPosition(0);
            // listView.setSelection(n);
        }

        public void carga_lista(List<ARCHIVO> l) {
            lista_materias = l;
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            listView.smoothScrollToPosition(0);

        }

        public ListaAdaptador(Context context, int textViewResourceId, boolean listar_offline) {
            super(context, textViewResourceId);
            this.context = context;
            this.listar_offline = listar_offline;
        }

        public int getCount() {
            return this.lista_materias.size();
        }

        public ARCHIVO getItem(int index) {
            return this.lista_materias.get(index);//getCount() - index - 1);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ARCHIVO arch = getItem(position);
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = convertView;

            Log.v("DESCARGADO", "=" + arch.descargado);

            if (arch.descargado || !listar_offline) {


                final FileFromURL card = arch.datos;


                myView = inflater.inflate(R.layout.archivo, null);

                boolean mr = false;
                String dir = "";

                if (position == 0) {
                    mr = true;
                    dir = RUTA(arch);
                } else {
                    ARCHIVO arch2 = getItem(position - 1);
                    String r2 = RUTA(arch2);
                    String r1 = RUTA(arch);

                    Log.v("R2", r2 + "***" + r1);

                    if (!r2.equals(r1) && (!listar_offline || arch2.descargado)) {
                        mr = true;
                        dir = r1;
                    }


                }

                if (dir.length() == 0) {
                    mr = false;
                }

                TextView rut = (TextView) myView.findViewById(R.id.ruta);

                Log.v("DIR", dir);

                if (mr) {
                    rut.setVisibility(View.VISIBLE);
                    rut.setText(dir);
                } else {
                    rut.setVisibility(View.GONE);
                }

                ImageButton borra = (ImageButton) myView.findViewById(R.id.borrar);
                ImageButton abre = (ImageButton) myView.findViewById(R.id.abrir);
                RelativeLayout opciones = (RelativeLayout) myView.findViewById(R.id.opciones);

                ImageView guardado = (ImageView) myView.findViewById(R.id.guardado);

                if (arch.descargado) {
                    opciones.setVisibility(View.VISIBLE);
                    borra.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.v("BORRAR", "=" + card.getURL());
                            BORRAR_ARCHIVO(false, card);

                        }
                    });

                    abre.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.v("HE PINCHADO AQUII", "DE MENTIRA");

                            final FileFromURL card = arch.datos;

                            if (arch.descargado) {
                                ABRIR(card.getFileDestination());
                            }

                        }
                    });

                } else {
                    opciones.setVisibility(View.GONE);

                }


                RelativeLayout tarjeta = (RelativeLayout) myView.findViewById(R.id.tarjeta);

                TextView textoo = (TextView) myView.findViewById(R.id.titulo);

                RelativeLayout color_extension = (RelativeLayout) myView.findViewById(R.id.color_extension);
                TextView extension = (TextView) myView.findViewById(R.id.extension);


                color_extension.setBackgroundColor(color(tipo((NOMBRE_FICHERO(arch)))));
                extension.setText(tipo(NOMBRE_FICHERO(arch)));

                textoo.setText(nombre_archivo_sin_tipo(NOMBRE_FICHERO(arch)));
                textoo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.v("HE PINCHADO AQUII", "DE MENTIRA2");

                        final FileFromURL card = arch.datos;

                        if (arch.descargado) {
                            ABRIR(card.getFileDestination());
                        } else {
                            if (PERMISO()) {
                                DESCARGAR_ARCHIVO(card, faitic, materia);
                            } else {
                                SNACKBAR("No tengo permiso para escribir en el dispositivo");
                            }
                        }

                    }
                });
            } else {
                myView = inflater.inflate(R.layout.vacio, null);
            }

            return myView;
        }


    }

    String NOMBRE_FICHERO(ARCHIVO arch) {
        String f[] = arch.datos.getFileDestination().split("/");
        return f[f.length - 1].replace("_", " ").replace("-", " ");
    }

    String RUTA(ARCHIVO arch) {

        String r = "";


        String ruta = arch.datos.getFileDestination();

        if (ruta.contains("/")) {
            String r2[] = ruta.split("/");
            for (int i = 0; i < (r2.length - 1); i++) {
                if (r.length() > 0) {
                    r = r + ", " + PRIMERA_LETRA_MAYUSCULA(r2[i].toLowerCase());
                } else {
                    r = PRIMERA_LETRA_MAYUSCULA(r2[i].toLowerCase());
                }
            }
        }


        return r.replace("_", " ").replace("-", " ");
    }

    static class ARCHIVO {

        FileFromURL datos;
        boolean descargado;

        ARCHIVO(FileFromURL datos, boolean descargado) {
            this.datos = datos;
            this.descargado = descargado;
        }

    }

    private void DESCARGAR_ARCHIVO(final FileFromURL path, final Faitic faitic, final String asignatura) {

        new Thread(new Runnable() {
            public void run() {


                String fileRelPath = path.getFileDestination();
                String whereToDownloadTheFile = path.getURL();

                Log.v("Path", path.getURL() + ":::" + path.getFileDestination());

                String strFileDestination = CREAR_DIRECCION(asignatura + File.separator + path.getFileDestination()) + File.separator + asignatura;

                boolean descargado = false;
                try {

                    faitic.downloadFile(whereToDownloadTheFile, "", strFileDestination + File.separator + path.getFileDestination());
                    descargado = true;
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                final boolean des = descargado;

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (des) {
                            adaptador.actualizar(new ARCHIVO(path, true));
                            SNACKBAR("Descargado " + path.getURL());
                        } else {
                            SNACKBAR("Error descargando " + path.getURL());
                        }
                    }
                });

            }
        }).start();
    }

    private void BORRAR_ARCHIVO(boolean background, final FileFromURL path) {

        File file = new File(PATH() + File.separator + materia + File.separator + path.getFileDestination());
        boolean deleted = file.delete();

        if (!background) {
            if (deleted) {
                adaptador.actualizar(new ARCHIVO(path, false));
                SNACKBAR("Borrado " + path.getFileDestination());
            } else {
                SNACKBAR("Error borrando " + path.getURL());
            }
        }

    }

    static String PRIMERA_LETRA_MAYUSCULA(String frase) {

        if (frase.length() > 1) {

            int x = 0;
            String z = "";
            String simbolos = "!Â¡\"()=Â¿?[]{}";

            if (simbolos.contains("" + frase.charAt(0) + "")) {
                x = 1;
                z = "" + frase.charAt(0) + "";
            }

            String a = "" + frase.charAt(0 + x) + "";
            String b = "";

            for (int i = (1 + x); i < frase.length(); i++) {
                b = b + "" + frase.charAt(i) + "";
            }

            String letras = "abcdefghijklmnñopqrstuvwxyzáéíóú";
            String letras2 = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ";

            for (int i = 0; i < letras.length(); i++) {
                a = a.replace(letras.charAt(i), letras2.charAt(i));
            }

            frase = z + "" + a + "" + b;

        }

        return frase;
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

    static String PATH() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Faicheck");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }

        return Environment.getExternalStorageDirectory() +
                File.separator + "Faicheck";
    }

    boolean ESTA_DESCARGADO(FileFromURL path, String nmateria) {
        File folder = new File(PATH() + File.separator + nmateria + File.separator + path.getFileDestination());
        boolean success = true;
        if (!folder.exists()) {
            success = false;
        } else {
            success = true;
        }

        Log.v("EXISTE", "=" + success);

        return success;
    }

    static String CREAR_DIRECCION(String rel) {

        String path = PATH();

        String[] rutas = rel.split("/");

        if (rutas.length > 1) {
            String path2 = path;
            for (int i = 0; i < (rutas.length - 1); i++) {
                path2 = path2 + File.separator + rutas[i];
                File folder = new File(path2);
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {
                    // Do something on success
                } else {
                    // Do something else on failure
                }
            }
        }

        return path;

    }

    void SNACKBAR(String texto) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), texto, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    int color(String url) {

        url = "." + url.toLowerCase();

        int res = Color.parseColor("#ffffff");

        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            res = Color.parseColor("#3F51B5");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            res = Color.parseColor("#EF5350");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            res = Color.parseColor("#4DB6AC");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            res = Color.parseColor("#4CAF50");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            res = Color.parseColor("#7E57C2");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            res = Color.parseColor("#FFCA28");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            res = Color.parseColor("#795548");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            res = Color.parseColor("#78909C");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            res = Color.parseColor("#009688");
        } else if (url.toString().contains(".txt")) {
            // Text file
            res = Color.parseColor("#CDDC39");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg")) {
            res = Color.parseColor("#EC407A");
        } else {


            while (url.length() < 6) {
                url = url + url;
            }

            String dos = "";
            String acept = "abcdefghijklmnopqrstuvwxyz0123456789";

            for (int i = 0; i < url.length(); i++) {
                if (acept.contains("" + url.charAt(i) + "")) {
                    dos = dos + "" + url.charAt(i) + "";
                }
            }

            if (dos.length() == 0) {
                url = "cristian";
            } else {
                url = dos;
            }

            url = url.replace("a", "01");
            url = url.replace("b", "02");
            url = url.replace("c", "03");
            url = url.replace("d", "04");
            url = url.replace("e", "05");
            url = url.replace("f", "06");
            url = url.replace("g", "07");
            url = url.replace("h", "08");
            url = url.replace("i", "09");
            url = url.replace("j", "10");
            url = url.replace("k", "11");
            url = url.replace("l", "12");
            url = url.replace("m", "13");
            url = url.replace("n", "14");
            url = url.replace("o", "15");
            url = url.replace("p", "16");
            url = url.replace("q", "17");
            url = url.replace("r", "18");
            url = url.replace("s", "19");
            url = url.replace("t", "20");
            url = url.replace("u", "21");
            url = url.replace("v", "22");
            url = url.replace("w", "23");
            url = url.replace("x", "24");
            url = url.replace("y", "25");
            url = url.replace("z", "26");

            int r1 = 10;
            int g1 = 10;
            int b1 = 10;
            int r2 = 10;
            int g2 = 10;
            int b2 = 10;

            while (url.length() < 20) {
                url = url + url;
            }

            if ((INT("" + url.charAt(0) + "") == 0 || INT("" + url.charAt(0) + "") == 1) && INT("" + url.charAt(1) + "") <= 5) {
                r1 = INT("" + url.charAt(0) + "" + url.charAt(1) + "");
            } else {
                r1 = INT("" + url.charAt(0) + "");
            }

            if ((INT("" + url.charAt(2) + "") == 0 || INT("" + url.charAt(2) + "") == 1) && INT("" + url.charAt(3) + "") <= 5) {
                r2 = INT("" + url.charAt(2) + "" + url.charAt(3) + "");
            } else {
                r2 = INT("" + url.charAt(2) + "");
            }

            if ((INT("" + url.charAt(4) + "") == 0 || INT("" + url.charAt(4) + "") == 1) && INT("" + url.charAt(5) + "") <= 5) {
                g1 = INT("" + url.charAt(4) + "" + url.charAt(5) + "");
            } else {
                g1 = INT("" + url.charAt(4) + "");
            }

            if ((INT("" + url.charAt(6) + "") == 0 || INT("" + url.charAt(6) + "") == 1) && INT("" + url.charAt(7) + "") <= 5) {
                g2 = INT("" + url.charAt(6) + "" + url.charAt(7) + "");
            } else {
                g2 = INT("" + url.charAt(6) + "");
            }

            if ((INT("" + url.charAt(8) + "") == 0 || INT("" + url.charAt(8) + "") == 1) && INT("" + url.charAt(9) + "") <= 5) {
                b1 = INT("" + url.charAt(8) + "" + url.charAt(9) + "");
            } else {
                b1 = INT("" + url.charAt(8) + "");
            }

            if ((INT("" + url.charAt(10) + "") == 0 || INT("" + url.charAt(10) + "") == 1) && INT("" + url.charAt(11) + "") <= 5) {
                b2 = INT("" + url.charAt(10) + "" + url.charAt(11) + "");
            } else {
                b2 = INT("" + url.charAt(10) + "");
            }

            String c = he(r1) + "" + he(r2) + "" + he(g1) + "" + he(g2) + "" + he(b1) + "" + he(b2);


            res = Color.parseColor("#94" + c);


        }

        return res;
    }

    String he(int n) {
        String a = "0";

        if (n <= 9) {
            a = "" + n;
        } else {
            if (n == 10) {
                a = "a";
            } else {
                if (n == 11) {
                    a = "b";
                } else {
                    if (n == 12) {
                        a = "c";
                    } else {
                        if (n == 13) {
                            a = "d";
                        } else {
                            if (n == 14) {
                                a = "e";
                            } else {
                                if (n == 15) {
                                    a = "f";
                                } else {
                                    a = "0";
                                }
                            }
                        }
                    }
                }
            }
        }

        return a;
    }

    int INT(String a) {
        return Integer.parseInt(("" + a + "").replaceAll("[\\D]", ""));
    }

    String tipo(String a) {
        String r = "";

        String b[] = a.split("[.]");
        if (b.length > 1) {
            r = b[b.length - 1];
        } else {
            r = "color.dev.com.red";
            Log.v("TIPO?", a);
        }


        return r;
    }

    String nombre_archivo_sin_tipo(String a) {
        String r = "";

        String b[] = a.split("[.]");
        if (b.length > 1) {
            for (int i = 0; i < b.length - 1; i++) {
                r = i == 0 ? b[i] : r + "." + b[i];
            }
        } else {
            r = a;
        }


        return r;
    }

    private class COMUNICACION extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            final String frase = arg1.getStringExtra("DATO2").trim();
            final ArrayList<String> lista = arg1.getStringArrayListExtra("LISTA");


            if (frase.length() > 0) {


                Thread timer = new Thread() {
                    public void run() {


                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {


                                    ArrayList<Archivos.ARCHIVO> list = new ArrayList<Archivos.ARCHIVO>();
                                    for (int i = 0; i < lista.size(); i = i + 2) {
                                        list.add(new Archivos.ARCHIVO(new FileFromURL(lista.get(i), lista.get(i + 1)), ESTA_DESCARGADO(new FileFromURL(lista.get(i), lista.get(i + 1)), materia)));
                                    }

                                    android.util.Log.v("RECIBIDO", ">" + frase);


                                    if (frase.equals("ABRIR")) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).descargado) {
                                                ABRIR(list.get(i).datos.getFileDestination());
                                            } else {

                                            }
                                        }
                                    }

                                    if (frase.equals("DESCARGAR")) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).descargado) {

                                            } else {
                                                if (PERMISO()) {
                                                    DESCARGAR_ARCHIVO(list.get(i).datos, faitic, materia);
                                                } else {
                                                    //SNACKBAR("No tengo permiso para escribir en el dispositivo");
                                                }
                                            }
                                        }

                                        list = new ArrayList<Archivos.ARCHIVO>();
                                        for (int i = 0; i < lista.size(); i = i + 2) {
                                            list.add(new Archivos.ARCHIVO(new FileFromURL(lista.get(i), lista.get(i + 1)), ESTA_DESCARGADO(new FileFromURL(lista.get(i), lista.get(i + 1)), materia)));
                                        }
                                    }

                                    if (frase.equals("BORRAR")) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).descargado) {
                                                BORRAR_ARCHIVO(true, list.get(i).datos);
                                            } else {
                                    /*if (PERMISO()) {
                                        DESCARGAR_ARCHIVO(list.get(i).datos, faitic, materia);
                                    } else {
                                        //SNACKBAR("No tengo permiso para escribir en el dispositivo");
                                    }*/
                                            }
                                        }

                                        list = new ArrayList<Archivos.ARCHIVO>();
                                        for (int i = 0; i < lista.size(); i = i + 2) {
                                            list.add(new Archivos.ARCHIVO(new FileFromURL(lista.get(i), lista.get(i + 1)), ESTA_DESCARGADO(new FileFromURL(lista.get(i), lista.get(i + 1)), materia)));
                                        }
                                    }


                                    final ArrayList<Archivos.ARCHIVO> list2 = list;


                                    adaptador.carga_lista(list2);
                                    adaptador.notifyDataSetChanged();
                                    //adaptador.actualizar(list2);

                                }
                            });


                        } catch (Exception e) {

                        } finally {

                        }
                    }
                };
                timer.start();
            }
        }

    }

}
