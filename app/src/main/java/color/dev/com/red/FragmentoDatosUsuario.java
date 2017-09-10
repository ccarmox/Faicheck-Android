/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentoDatosUsuario extends Fragment {


    Context context;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // = inflater.inflate(R.layout.first_fragment, container, false);

        context = getActivity();
        view = inflater.inflate(R.layout.fragmento_datos_usuario, container, false);

        String niu = Memoria.lee("NIU", "", context);
        String correo = Memoria.lee("CORREO", "", context);

        final EditText tniu = view.findViewById(R.id.niu);
        final EditText tcorreo = view.findViewById(R.id.correo);

        tniu.setText(niu);
        tcorreo.setText(correo);

        ImageView ccorreo = (ImageView) view.findViewById(R.id.copiarcorreo);
        ImageView cniu = (ImageView) view.findViewById(R.id.copiarniu);

        ccorreo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                copiarAlPortapapeles(Memoria.lee("CORREO", "", context), context);

            }
        });

        cniu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                copiarAlPortapapeles(Memoria.lee("NIU", "", context), context);

            }
        });

        Button guardarniucorreo = (Button) view.findViewById(R.id.guardarniuycorreo);

        guardarniucorreo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Memoria.guarda("NIU", tniu.getText().toString(), context);
                Memoria.guarda("CORREO", tcorreo.getText().toString(), context);

            }
        });

        Button nuevoGrupo = (Button) view.findViewById(R.id.nuevogrupo);
        nuevoGrupo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                grupo.nuevoGrupo(new grupo("", "", "", ""), context);
                cargarGrupos();

            }
        });


        Button b1 = (Button) view.findViewById(R.id.guardar6);
        Button b2 = (Button) view.findViewById(R.id.guardar5);
        Button b3 = (Button) view.findViewById(R.id.guardar4);
        Button b4 = (Button) view.findViewById(R.id.guardar3);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                abrirURL("http://faitic.uvigo.es/", context);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                abrirURL("http://seix.uvigo.es/uvigo.sv/index.php?modulo=index", context);

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                abrirURL("https://correoweb.uvigo.es/horde", context);

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                abrirURL("http://si.uvigo.es/telefonia/directorio/busca.htm", context);

            }
        });

        Button exportar = (Button) view.findViewById(R.id.exportar);
        exportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(PERMISO()) {
                    String res = Importar.exportar(context);

                    if (res.length() > 0) {
                        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button eliminar_horarios = (Button) view.findViewById(R.id.eliminar_horario);
        eliminar_horarios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                FragmentoHorario.tarea.eliminarHorario(context);
                                toast("Listo. El horario ya se ha borrado");


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Estás seguro de que quieres eliminar toda la información del horario?").setPositiveButton("Sí", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        cargarGrupos();

        return view;
    }

    void toast(String codigo) {
        Toast.makeText(context, codigo,
                Toast.LENGTH_LONG).show();
    }

    void cargarGrupos() {

        ArrayList<grupo> l = grupo.lee(context);

        Log.v("ELEMENTOS", "=" + l.size());

        LinearLayout grupos = (LinearLayout) view.findViewById(R.id.grupos);
        grupos.removeAllViews();

        for (int i = 0; i < l.size(); i++) {

            final int i2 = i;

            View child = getActivity().getLayoutInflater().inflate(R.layout.fragmento_datos_usuario_grupo, null);
            final RelativeLayout tar = (RelativeLayout) child.findViewById(R.id.informa);
            final RelativeLayout bor = (RelativeLayout) child.findViewById(R.id.borrar);
            final RelativeLayout edi = (RelativeLayout) child.findViewById(R.id.edicion);
            Button guardar = (Button) child.findViewById(R.id.guardar);

            final TextView t1 = (TextView) child.findViewById(R.id.grado);
            final TextView t2 = (TextView) child.findViewById(R.id.grupo);
            final TextView t3 = (TextView) child.findViewById(R.id.subgrupo);
            final TextView t4 = (TextView) child.findViewById(R.id.aula);

            final EditText e1 = (EditText) child.findViewById(R.id.abgrado);
            final EditText e2 = (EditText) child.findViewById(R.id.abgrupo);
            final EditText e3 = (EditText) child.findViewById(R.id.absubgrupo);
            final EditText e4 = (EditText) child.findViewById(R.id.abaula);

            edi.setVisibility(View.GONE);

            t1.setText(l.get(i).grado);
            t2.setText(l.get(i).grupo);
            t3.setText(l.get(i).subgrupo);
            t4.setText(l.get(i).aula);
            e1.setText(l.get(i).grado);
            e2.setText(l.get(i).grupo);
            e3.setText(l.get(i).subgrupo);
            e4.setText(l.get(i).aula);

            guardar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    grupo.actualizarGrupo(i2, new grupo(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), e4.getText().toString()), context);
                    t1.setText(e1.getText().toString());
                    t2.setText(e2.getText().toString());
                    t3.setText(e3.getText().toString());
                    t4.setText(e4.getText().toString());

                    boolean vi = edi.getVisibility() == View.VISIBLE;

                    Log.v("VISIB", "=" + vi);

                    if (vi) {
                        edi.setVisibility(View.GONE);
                    } else {
                        edi.setVisibility(View.VISIBLE);
                    }

                }
            });

            TextView te = (TextView) child.findViewById(R.id.hora);


            tar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    boolean vi = edi.getVisibility() == View.VISIBLE;

                    Log.v("VISIB", "=" + vi);

                    if (vi) {
                        edi.setVisibility(View.GONE);
                    } else {
                        edi.setVisibility(View.VISIBLE);
                    }
                }
            });


            bor.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogo, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked


                                    Log.v("borrar", "=" + i2);
                                    grupo.borra(i2, context);
                                    cargarGrupos();

                                    dialogo.dismiss();

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Estás seguro de que quieres eliminar este grupo?").setPositiveButton("Sí", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });


            grupos.addView(child);
        }


    }

    static void abrirURL(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    static void copiarAlPortapapeles(String t, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("texto", t);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, "Texto copiado al portapapeles",
                Toast.LENGTH_LONG).show();
    }

    public static class grupo {
        String grado;
        String grupo;
        String subgrupo;
        String aula;

        grupo(String grado, String grupo, String subgrupo, String aula) {
            this.subgrupo = subgrupo;
            this.grado = grado;
            this.grupo = grupo;
            this.aula = aula;
        }


        static String getXML(grupo a) {
            return "<g>" + a.grado + "</g><gr>" + a.grupo + "</gr><sg>" + a.subgrupo + "</sg><au>" + a.aula + "</au>";
        }

        String getXML() {
            return "<g>" + grado + "</g><gr>" + grupo + "</gr><sg>" + subgrupo + "</sg><au>" + aula + "</au>";
        }

        static grupo getGrupoFromXML(String texto) {
            Log.v("getGrupoFromXML", "=" + texto);
            String n = getEtiqueta(texto, "g");
            String in = getEtiqueta(texto, "gr");
            String ii = getEtiqueta(texto, "sg");
            String fi = getEtiqueta(texto, "au");

            return new grupo(n, in, ii, fi);
        }

        static String getEtiqueta(String texto, String e) {
            return (" " + texto.split("<" + e + ">")[1]).split("</" + e + ">")[0].trim();
        }

        static ArrayList<grupo> getArrayListgrupos(String texto) {
            ArrayList<grupo> l = new ArrayList<grupo>();
            String[] sp = texto.split("<g>");
            if (sp.length > 1) {
                for (int i = 1; i < sp.length; i++) {
                    l.add(getGrupoFromXML("<g>" + sp[i]));
                }
            }

            return l;
        }

        static String getXML(ArrayList<grupo> l) {
            String r = "";

            for (int i = 0; i < l.size(); i++) {
                r = r + "" + l.get(i).getXML();
            }

            return r;
        }

        static void guarda(ArrayList<grupo> l, Context context) {
            Memoria.guarda("GRUPO", getXML(l), context);
        }

        static ArrayList<grupo> lee(Context context) {
            String t = Memoria.lee("GRUPO", "", context);

            Log.v("MEMORIA GRUPO", "=" + t);

            return getArrayListgrupos(t);
        }

        static void borra(int posicion, Context context) {
            ArrayList<grupo> l = lee(context);
            l.remove(posicion);
            guarda(l, context);
        }

        static void actualizarGrupo(int posicion, grupo gr, Context context) {
            ArrayList<grupo> l = lee(context);
            ArrayList<grupo> r = new ArrayList<grupo>();

            for (int i = 0; i < l.size(); i++) {
                if (i == posicion) {
                    r.add(gr);
                } else {
                    r.add(l.get(i));
                }
            }

            l.remove(posicion);
            guarda(l, context);
        }

        static boolean nuevoGrupo(grupo tar, Context context) {
            ArrayList<grupo> l = lee(context);
            l.add(tar);
            guarda(l, context);

            return true;
        }
    }

    boolean PERMISO() {

        boolean permiso = false;

        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Faicheck", "Permission is granted");
                permiso = true;
            } else {

                Log.v("Faicheck", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Faicheck", "Permission is granted");
            permiso = true;
        }

        return permiso;
    }

}
