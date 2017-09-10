/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentoInformacion extends Fragment {


    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;// = inflater.inflate(R.layout.first_fragment, container, false);

        context = getActivity();
        view = inflater.inflate(R.layout.fragmento_info, container, false);


        Button inf = (Button) view.findViewById(R.id.info);
        Button web = (Button) view.findViewById(R.id.web);
        Button pol = (Button) view.findViewById(R.id.politica);
        Button lic = (Button) view.findViewById(R.id.licencia);


        inf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                INFORMACION();

            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                WEB();

            }
        });

        pol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                POLITICA();

            }
        });

        lic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LICENCIA();

            }
        });


        return view;
    }

    void WEB() {
        String url = "https://davovoid.github.io/color.dev.com.red/";
        if (false) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Intent i2 = new Intent();
            i2.setClass(context, Web.class);
            i2.putExtra("URL", url);
            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i2);
        }
    }

    void POLITICA() {

        Intent i2 = new Intent();
        i2.setClass(context, Politica.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i2);

    }

    void INFORMACION() {

        Intent i2 = new Intent();
        i2.setClass(context, Informacion.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i2);

    }

    void LICENCIA() {

        Intent i2 = new Intent();
        i2.setClass(context, Licencia.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i2);

    }


}
