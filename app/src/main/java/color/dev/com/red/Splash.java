package color.dev.com.red;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.transition.Slide;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Splash extends Activity {

    private static final int SPLASH_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.splash);


        final Intent intent = new Intent(Splash.this, FragmentoPrincipal.class);



        new Thread() {
            public void run() {


                try {
                    Thread.sleep(SPLASH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                });


            }
        }.start();


    }

}