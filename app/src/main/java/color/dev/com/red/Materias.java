/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Materias extends AppCompatActivity {


    ListaAdaptador adaptador;
    ListView listView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materias);


        Intent intent = getIntent();
        final Faitic faitic = (Faitic) intent.getSerializableExtra("FAITIC");
        final boolean offline = intent.getBooleanExtra("OFFLINE", false);
        int tam = intent.getIntExtra("TAM", 0);
        ArrayList<Subject> lista = new ArrayList<Subject>();

        for (int i = 0; i < tam; i++) {
            lista.add(new Subject(intent.getStringArrayExtra("MAT" + i)));

        }


        listView = (ListView) findViewById(R.id.lista);

        adaptador = new ListaAdaptador(getApplicationContext(), R.layout.materia);
        listView.setAdapter(adaptador);

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.v("HE PINCHADO AQUII", "DE VERDAD");
                final Subject card = adaptador.getItem(position);

                //Log.v("CARD",card[0]+"::"+card[1]);

                MOSTRAR_ARCHIVOS(card, faitic, offline);

            }
        });

        for (int i = 0; i < lista.size(); i++) {
            adaptador.add(lista.get(i));
        }
    }

    class ListaAdaptador extends ArrayAdapter<Subject> {

        private TextView chatText;
        private List<Subject> lista_materias = new ArrayList<Subject>();
        private Context context;

        @Override
        public void add(Subject object) {

            lista_materias.add(0, object);

            super.add(object);
        }


        public ListaAdaptador(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.context = context;
        }

        public int getCount() {
            return this.lista_materias.size();
        }

        public Subject getItem(int index) {
            return this.lista_materias.get(index);//getCount() - index - 1);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final Subject card = getItem(position);
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View myView = convertView;
            myView = inflater.inflate(R.layout.materia, null);

            /*RelativeLayout tarjeta = (RelativeLayout) myView.findViewById(R.id.tarjeta);
            tarjeta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    MOSTRAR_ARCHIVOS(card, faitic);

                }});*/

            TextView textoo = (TextView) myView.findViewById(R.id.titulo);
            RelativeLayout color = (RelativeLayout) myView.findViewById(R.id.color);
            color.setBackgroundColor(color(position));
            textoo.setText(card.getName());

            return myView;
        }


    }

    private static DocumentFromURL subject;
    private static int subjectType;
    private static String subjectURL;

    void MOSTRAR_ARCHIVOS(final Subject materia, final Faitic faitic, final boolean offline) {

        /**/


        new Thread(new Runnable() {
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        CARGANDO();
                    }
                });


                if (subjectURL != null && subject != null) {

                    try {
                        //faitic.logoutSubject(subjectURL, subject.getName(), subjectType);
                    } catch (Exception e) {
                        Log.v("Saliendo Asignatura", e.toString());
                    }

                }


                ArrayList<FileFromURL> fileList = null;

                if (!offline) {
                    try {


                        //Faitic faitic = new Faitic(true);
                        Log.v("GOTOSUBJECT", "0");
                        subject = faitic.goToSubject(materia.getURL());
                        Log.v("GOTOSUBJECT", "1");

                        subjectType = faitic.subjectPlatformType(subject.getURL());
                        subjectURL = subject.getURL();
                        //String subjectName = materia.getName();

                        Log.v("BUSCAR ARCHIVOS", "BUSCANDO...");

                        Log.v("subjectType", "=" + subjectType);

                        if (subjectType == faitic.CLAROLINE) {

                            fileList = faitic.listDocumentsClaroline(subjectURL);

                        } else if (subjectType == faitic.MOODLE) {

                            fileList = faitic.listDocumentsMoodle(faitic.lastRequestedURL);

                        } else if (subjectType == faitic.MOODLE2) {

                            fileList = faitic.listDocumentsMoodle2(faitic.lastRequestedURL);

                        } else if (subjectType == faitic.MOODLE3) {

                            fileList = faitic.listDocumentsMoodle2(faitic.lastRequestedURL);

                        } else {

                            //Unknown
                            if (fileList != null) fileList.clear();
                            else fileList = new ArrayList<FileFromURL>();

                        }

                        for (int o = 0; o < fileList.size(); o++) {
                            Log.v("ARCHIVO=" + o, fileList.get(o).getFileDestination());
                        }

                    } catch (Exception ex) {

                        Log.v("ERROR0", ex.toString());


                        if (fileList != null) fileList.clear();
                        else fileList = new ArrayList<FileFromURL>();

                    }

                }

                if ((fileList == null || fileList.size() == 0) && offline) {

                    fileList = FileFromURL.LEE(materia.getName(), Materias.this);


                }


                if (fileList != null) {
                    if (fileList.size() > 0) {

                        FileFromURL.GUARDA(materia.getName(), fileList, Materias.this);

                        Intent i2 = new Intent();
                        i2.setClass(Materias.this, Archivos.class);
                        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i2.putExtra("FAITIC", faitic);
                        i2.putExtra("OFFLINE", offline);
                        i2.putExtra("MATERIA", materia.getName());
                        i2.putExtra("ATAM", fileList.size());
                        for (int i = 0; i < fileList.size(); i++) {
                            i2.putExtra("AMAT" + i, fileList.get(i).getDatos());
                        }
                        Materias.this.startActivity(i2);

                    } else {
                        SNACKBAR("No hay ningún archivo");
                    }
                } else {
                    SNACKBAR("ERROR");
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        CARGA_FINALIZADA();
                    }
                });

            }
        }).start();
    }

    void SNACKBAR(String texto) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), texto, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    void CARGANDO() {

        ImageView imagen = (ImageView) findViewById(R.id.carga);

        final Animation rotar = AnimationUtils.loadAnimation(Materias.this, R.anim.girar);

        imagen.startAnimation(rotar);

        RelativeLayout layout_contenido = (RelativeLayout) findViewById(R.id.layout_contenido);
        RelativeLayout layout_carga = (RelativeLayout) findViewById(R.id.layout_carga);


        layout_contenido.setVisibility(View.GONE);
        layout_carga.setVisibility(View.VISIBLE);

    }

    void CARGA_FINALIZADA() {

        RelativeLayout layout_contenido = (RelativeLayout) findViewById(R.id.layout_contenido);
        RelativeLayout layout_carga = (RelativeLayout) findViewById(R.id.layout_carga);


        layout_contenido.setVisibility(View.VISIBLE);
        layout_carga.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    static int color(int n) {

        n = n % colores.length;

        return Color.parseColor(colores[n]);

    }

    static final String[] colores =
            {
                    "#E53935",
                    "#673AB7",
                    "#4CAF50",
                    "#FF5722",
                    "#795548",
                    "#7986CB",
                    "#00E676",
                    "#F9A825",
                    "#607D8B",
                    "#EF5350",
                    "#4FC3F7",
                    "#5E35B1",
                    "#689F38",
                    "#E040FB",
                    "#B39DDB",
                    "#E91E63"
            };


}
