/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentoLista extends Fragment {


    COMUNICACION comunicacion = null;
    final static String BROADCAST = "color.dev.com.red.bus";


    Context context;
    private RelativeLayout cargando;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MarcadorAdapter.marcador> messageList = new ArrayList();

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // = inflater.inflate(R.layout.first_fragment, container, false);

        context = getActivity();


        view = inflater.inflate(R.layout.fragmento_noticias, container, false);


        cargando = (RelativeLayout) view.findViewById(R.id.tarjeta_cargando);

        cargando(true);
        descarga();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recview);

        //mRecyclerView.setBackground(wallpaperDrawable);

        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);//CenterZoomLayoutManager(this);//LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MarcadorAdapter(context, messageList);
        mRecyclerView.setAdapter(mAdapter);


        if (comunicacion == null) {
            comunicacion = new COMUNICACION();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST);
            getContext().registerReceiver(comunicacion, intentFilter);
        }

        recargar();

        cargarBotones();

        return view;
    }

    void recargar() {
        cargando(true);
        if (XYZConecta.EXISTE(XYZConecta.PARTICIPANTES[3], context)) {
            XYZConecta.ENVIA("CERCA", "", "CERCA", XYZConecta.PARTICIPANTES[2], XYZConecta.PARTICIPANTES[3], context);
        }
    }

    void cargarBotones() {

        ImageView botonApp = (ImageView) view.findViewById(R.id.abrirVigoBus);
        botonApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    Intent i = context.getPackageManager().getLaunchIntentForPackage(XYZConecta.PARTICIPANTES[3]);
                    context.startActivity(i);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }

            }
        });

        ImageView botonRecarga = (ImageView) view.findViewById(R.id.botonrecargar);
        botonRecarga.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                recargar();

            }
        });
    }


    void cargando(boolean estado) {

        cargando.setVisibility(estado ? View.VISIBLE : View.GONE);
    }

    void descarga() {
        RelativeLayout descar = (RelativeLayout) view.findViewById(R.id.tarjeta_descarga);
        descar.setVisibility(XYZConecta.EXISTE(XYZConecta.PARTICIPANTES[3], context) ? View.GONE : View.VISIBLE);
        ImageView boton = (ImageView) view.findViewById(R.id.botondescargaplay);
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                XYZConecta.GOOGLE_PLAY(XYZConecta.PARTICIPANTES[3], context);

            }
        });
    }


    private class COMUNICACION extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            final String frase = arg1.getStringExtra("DATO").trim();

            android.util.Log.v("RECIBIDO", ">" + frase);
            if (frase.length() > 0) {


                Thread timer = new Thread() {
                    public void run() {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    cargando(false);
                                    Log.v("FRASECominucacion", frase);
                                    messageList.clear();
                                    //messageList.add(new MarcadorAdapter.marcador("",100));

                                    ArrayList<MarcadorAdapter.marcador> m = XYZConectaB.marcadoresFromXML(frase);

                                    for (int i = 0; i < m.size(); i++) {
                                        Log.v("N Marcador", "=" + m.get(i).linea);
                                        messageList.add(m.get(i));
                                    }

                                    Log.v("titulodescrip", messageList.get(0).linea);
                                    final String[] ab = messageList.get(0).linea.split("<num>");
                                    final String parada = ab[0].replace("N", "").replace("º", "").replace(" ", "");
                                    TextView titulo = view.findViewById(R.id.titulo);
                                    TextView descripcion = view.findViewById(R.id.descripcion);
                                    descripcion.setText(ab[0]);
                                    titulo.setText(ab[1]);


                                    mAdapter.notifyDataSetChanged();
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
