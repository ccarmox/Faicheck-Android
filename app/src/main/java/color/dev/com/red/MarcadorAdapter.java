/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MarcadorAdapter extends RecyclerView.Adapter<MarcadorAdapter.ViewHolder> {

    private ArrayList<marcador> messageList;

    Context context;

    public MarcadorAdapter(Context context, ArrayList<marcador> messages) {
        this.context = context;
        messageList = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public RelativeLayout tarjeta;
        public TextView descripcion;
        public TextView ultimaActualizacion;

        public ViewHolder(LinearLayout v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
            tarjeta = (RelativeLayout) v.findViewById(R.id.tarjeta);
            ultimaActualizacion = (TextView) v.findViewById(R.id.actualizacion);
        }
    }

    @Override
    public MarcadorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.portada, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticia, parent, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;

    }

    public void remove(int pos) {
        int position = pos;
        messageList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, messageList.size());

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if (position != 0) {


            new Thread() {
                @Override
                public void run() {

                    while (true) {

                        try {
                            double esp = ((messageList.get(position).milis - getMillis()) / 60000) + 1;

                            final String te = (((int) esp) == 0) ? "0" : (esp > 0) ? "" + ((int) esp) : "";

                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (holder.descripcion != null) {
                                        if (te.length() > 0) {
                                            holder.descripcion.setText(te + " min.");
                                        } else {
                                            holder.descripcion.setText("-");
                                        }
                                    }

                                }
                            });

                            try {
                                Thread.sleep(60000);
                            } catch (InterruptedException e) {
                            }

                        } catch (Exception e) {

                        }

                    }

                }
            }.start();


            holder.titulo.setText(messageList.get(position).linea);


            Log.v("lista", messageList.get(position).linea);

            /*if (holder.descripcion != null) {
                holder.descripcion.setText(messageList.get(position).espera + " min.");
            }*/

            if (holder.tarjeta != null) {
                //holder.tarjeta.setBackgroundColor(Color.parseColor(messageList.get(position).color));
            }
        } else {

            new Thread() {
                @Override
                public void run() {

                    while (true) {

                        final int esp = (int) Math.abs((messageList.get(position).milis - getMillis()) / 60000);


                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                holder.ultimaActualizacion.setText("Actualizado hace " + esp + " minutos");

                            }
                        });

                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                        }


                    }

                }
            }.start();


           /* final String[] ab = messageList.get(position).linea.split("<num>");
            final String parada = ab[0].replace("N","").replace("º","").replace(" ","");
            holder.descripcion.setText(ab[0]);
            holder.titulo.setText(ab[1]);
*/


        }
        //holder.tarjeta.setBackground(drawable);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    static public class marcador {
        String linea;
        int espera;
        long milis;

        marcador(String linea, int espera) {
            this.linea = linea;
            this.espera = espera;
            this.milis = getMillis() + espera * 60 * 1000;
        }
    }

    static long getMillis() {
        return System.currentTimeMillis();
    }

}