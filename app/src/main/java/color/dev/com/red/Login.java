/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Login extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button boton = (Button) findViewById(R.id.blogin);
        final Button boton_offline = (Button) findViewById(R.id.boffline);
        EditText user = (EditText) findViewById(R.id.usuario);
        EditText contra = (EditText) findViewById(R.id.contra);
        CheckBox recuerda = (CheckBox) findViewById(R.id.recordar);
        RelativeLayout online = (RelativeLayout) findViewById(R.id.online);
        RelativeLayout offline = (RelativeLayout) findViewById(R.id.offline);
        ImageView more_info = (ImageView) findViewById(R.id.more_info);

        RelativeLayout cuadro = (RelativeLayout) findViewById(R.id.cuadro);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_up);

        // Start animation
        cuadro.startAnimation(slide_up);

        boolean modo_online = ONLINE();

        online.setVisibility(modo_online ? View.VISIBLE : View.GONE);
        offline.setVisibility((!modo_online) ? View.VISIBLE : View.GONE);

        String uid = lee("USUARIO", "", Login.this);
        String ucl = lee("CLAVE", "", Login.this);
        if (ucl.length() > 0) {
            try {
                ucl = DESENCRIPTA(ucl, uid);
                recuerda.setChecked(true);
                contra.setText(ucl);
            } catch (Exception ex) {

            }
        }

        user.setText(uid);

        contra.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    LOGIN(false);
                    return true;
                }
                return false;
            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LOGIN(false);

            }
        });

        more_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                INFORMACION();

            }
        });

        boton_offline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LOGIN(true);

            }
        });

        //DRAWER();
    }

    void DRAWER() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        switch (menuItem.getItemId()) {
                            case R.id.politica:
                                POLITICA();
                                break;
                            case R.id.informacion:
                                INFORMACION();
                                break;
                            case R.id.licencia:
                                LICENCIA();
                                break;
                            case R.id.web:
                                WEB();
                                break;

                        }


                        return true;
                    }
                });

    }

    void WEB() {
        String url = "https://davovoid.github.io/color.dev.com.red/";
        if (false) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Intent i2 = new Intent();
            i2.setClass(Login.this, Web.class);
            i2.putExtra("URL", url);
            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Login.this.startActivity(i2);
        }
    }

    void POLITICA() {

        Intent i2 = new Intent();
        i2.setClass(Login.this, Politica.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Login.this.startActivity(i2);

    }

    void INFORMACION() {

        Intent i2 = new Intent();
        i2.setClass(Login.this, Informacion.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Login.this.startActivity(i2);

    }

    void LICENCIA() {

        Intent i2 = new Intent();
        i2.setClass(Login.this, Licencia.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Login.this.startActivity(i2);

    }

    private boolean ONLINE() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    static String lee(String variable, String predefinido, Context context) {

        String valor = "";

        SharedPreferences prefs16 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        valor = prefs16.getString(variable, predefinido);


        return valor;
    }

    static void guarda(String variable, String valor, Context context) {

        SharedPreferences prefs73 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor73 = prefs73.edit();

        editor73.putString(variable, valor);
        editor73.commit();


    }

    static int lee(String variable, int predefinido, Context context) {

        int valor = 0;

        SharedPreferences prefs16 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        valor = prefs16.getInt(variable, predefinido);


        return valor;
    }

    static void guarda(String variable, int valor, Context context) {

        SharedPreferences prefs73 = context.getSharedPreferences("DATA",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor73 = prefs73.edit();

        editor73.putInt(variable, valor);
        editor73.commit();


    }

    void LOGIN(final boolean modo_offline) {
        final Button boton = (Button) findViewById(R.id.blogin);
        final Button boton_offline = (Button) findViewById(R.id.boffline);
        EditText user = (EditText) findViewById(R.id.usuario);
        EditText contra = (EditText) findViewById(R.id.contra);
        final CheckBox recuerda = (CheckBox) findViewById(R.id.recordar);

        if (user.getText().length() > 0 && contra.getText().length() > 0) {

            DATOS data = new DATOS(user.getText().toString(), contra.getText().toString());
            final String usuario = user.getText().toString();
            final String contrase = contra.getText().toString();


            new Thread(new Runnable() {
                public void run() {


                    ArrayList<Subject> lista = null;
                    boolean offline = false;
                    Faitic faitic = new Faitic(true);

                    if (modo_offline) {
                        lista = Subject.LEE("MATERIAS", Login.this);
                        offline = true;
                    } else {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                boton.setText("Iniciando sesión");
                                boton.setEnabled(false);
                                boton_offline.setEnabled(false);
                            }
                        });

                        try {


                            String login = faitic.faiticLogin(usuario, contrase);


                            if (login != null) {


                                if (recuerda.isChecked()) {
                                    guarda("USUARIO", usuario, Login.this);
                                    guarda("CLAVE", ENCRIPTA(contrase, usuario), Login.this);
                                } else {
                                    guarda("USUARIO", "", Login.this);
                                    guarda("CLAVE", "", Login.this);
                                }

                                //Log.v("LOGIN", login);

                                lista = faitic.faiticSubjects(login);

                            } else {


                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        boton.setText("Error de Autentificación");

                                    }
                                });
                            }


                        } catch (Exception e) {
                            Log.v("ERROR", e.toString());
                        }

                    }

                    if (lista != null) {
                        INICIAR(lista, faitic, offline);
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                boton.setText("Error");
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            boton.setEnabled(true);
                            boton_offline.setEnabled(true);
                        }
                    });


                }
            }).start();

        }

    }

    class DATOS {

        String usuario;
        String contra;

        DATOS(String user, String contra) {
            this.usuario = user;
            this.contra = contra;
        }
    }

    void INICIAR(ArrayList<Subject> lista, Faitic faitic, boolean offline) {

        Subject.GUARDA("MATERIAS", lista, Login.this);

        Intent i2 = new Intent();
        i2.setClass(Login.this, Materias.class);
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i2.putExtra("FAITIC", faitic);
        i2.putExtra("OFFLINE", offline);
        i2.putExtra("TAM", lista.size());
        for (int i = 0; i < lista.size(); i++) {
            i2.putExtra("MAT" + i, lista.get(i).getDatos());
        }
        Login.this.startActivity(i2);

    }

    static String clave(String usuario) {
        String key = "";

        usuario = usuario.replace("a", "145");
        usuario = usuario.replace("b", "14554");
        usuario = usuario.replace("c", "657");
        usuario = usuario.replace("d", "1452");
        usuario = usuario.replace("e", "12324");
        usuario = usuario.replace("f", "1456");
        usuario = usuario.replace("g", "987");
        usuario = usuario.replace("h", "23");
        usuario = usuario.replace("i", "24245");
        usuario = usuario.replace("j", "2345");
        usuario = usuario.replace("k", "3256");
        usuario = usuario.replace("m", "3463");
        usuario = usuario.replace("n", "5643");
        usuario = usuario.replace("?±", "3467");
        usuario = usuario.replace("o", "2345");
        usuario = usuario.replace("p", "2346");
        usuario = usuario.replace("q", "234");
        usuario = usuario.replace("r", "32546");
        usuario = usuario.replace("s", "56345");
        usuario = usuario.replace("t", "3456");
        usuario = usuario.replace("u", "4356");
        usuario = usuario.replace("v", "3456");
        usuario = usuario.replace("w", "236432");
        usuario = usuario.replace("x", "3467");
        usuario = usuario.replace("y", "2356");
        usuario = usuario.replace("z", "7657");


        usuario = usuario.replace("0", "7677");
        usuario = usuario.replace("1", "76347");
        usuario = usuario.replace("2", "443");
        usuario = usuario.replace("3", "7137");
        usuario = usuario.replace("4", "7234");
        usuario = usuario.replace("5", "12353");
        usuario = usuario.replace("6", "1235");
        usuario = usuario.replace("7", "7235");
        usuario = usuario.replace("8", "1232");
        usuario = usuario.replace("9", "34635");


        key = usuario;

        return key;
    }

    static int SUMA(int n) {
        int r = 0;

        String a = "" + n;

        for (int i = 0; i < a.length(); i++) {
            r = r + Integer.parseInt(("" + a.charAt(i) + "").replaceAll("[\\D]", ""));
        }

        return r;
    }


    public AlgorithmParameterSpec getIV() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }

    public String ENCRIPTA(String plainText, String password) throws Exception {
        Cipher cipher;
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        AlgorithmParameterSpec spec = getIV();

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");

        return encryptedText;
    }

    public String DESENCRIPTA(String cryptedText, String password) throws Exception {
        Cipher cipher;
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        AlgorithmParameterSpec spec = getIV();

        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(bytes);
        String decryptedText = new String(decrypted, "UTF-8");

        return decryptedText;
    }

    static void GUARDA(String nombre, ArrayList<String[]> l, Context context) {

        guarda(nombre + "//TAM", l.size(), context);
        for (int i = 0; i < l.size(); i++) {
            guarda(nombre + "//ARRAY//" + i, l.get(i).length, context);

            for (int j = 0; j < l.get(i).length; j++) {
                guarda(nombre + "//ARRAY//" + i + "//" + j, l.get(i)[j], context);
            }
        }

    }

    static ArrayList<String[]> LEE(String nombre, Context context) {
        ArrayList<String[]> lista = new ArrayList<String[]>();

        int tam = lee(nombre + "//TAM", 0, context);

        for (int i = 0; i < tam; i++) {

            int tam2 = lee(nombre + "//ARRAY//" + i, 0, context);
            String[] a = new String[tam2];

            for (int j = 0; j < tam2; j++) {

                String b = lee(nombre + "//ARRAY//" + i + "//" + j, "", context);
                a[j] = b;

            }

            lista.add(a);

        }

        return lista;
    }


}
