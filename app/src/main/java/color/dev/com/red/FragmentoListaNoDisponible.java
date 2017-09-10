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


public class FragmentoListaNoDisponible extends Fragment {


    final static String BROADCAST = "color.dev.com.red.bus";


    Context context;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // = inflater.inflate(R.layout.first_fragment, container, false);

        context = getActivity();


        view = inflater.inflate(R.layout.fragmento_noticias_no_disponible, container, false);

        return view;
    }

}
