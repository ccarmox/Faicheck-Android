/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */

package color.dev.com.red;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;


public class FragmentoPrincipal extends AppCompatActivity {

    // For this example, only two pages
    static final int NUM_ITEMS = 5;

    ViewPager mPager;
    SlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmento_principal);



		/* Instantiate a ViewPager and a PagerAdapter. */
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        //mPager.setPageTransformer(true, new ViewPagerAnimacion());


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RelativeLayout r1 = (RelativeLayout) findViewById(R.id.fondo_icon10);
                RelativeLayout r2 = (RelativeLayout) findViewById(R.id.fondo_icon11);
                RelativeLayout r3 = (RelativeLayout) findViewById(R.id.fondo_icon12);
                RelativeLayout r4 = (RelativeLayout) findViewById(R.id.fondo_icon13);
                RelativeLayout r5 = (RelativeLayout) findViewById(R.id.fondo_icon14);

                r1.setBackground(getResources().getDrawable(R.drawable.transparente));
                r2.setBackground(getResources().getDrawable(R.drawable.transparente));
                r3.setBackground(getResources().getDrawable(R.drawable.transparente));
                r4.setBackground(getResources().getDrawable(R.drawable.transparente));
                r5.setBackground(getResources().getDrawable(R.drawable.transparente));

                if (position == 0) {
                    r1.setBackground(getResources().getDrawable(R.drawable.circulo_gris));
                }
                if (position == 1) {
                    r2.setBackground(getResources().getDrawable(R.drawable.circulo_gris));
                }
                if (position == 2) {
                    r3.setBackground(getResources().getDrawable(R.drawable.circulo_gris));
                }
                if (position == 3) {
                    r4.setBackground(getResources().getDrawable(R.drawable.circulo_gris));
                }
                if (position == 4) {
                    r5.setBackground(getResources().getDrawable(R.drawable.circulo_gris));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RelativeLayout r1 = (RelativeLayout) findViewById(R.id.fondo_icon11);
        RelativeLayout r2 = (RelativeLayout) findViewById(R.id.fondo_icon12);
        RelativeLayout r3 = (RelativeLayout) findViewById(R.id.fondo_icon13);
        RelativeLayout r0 = (RelativeLayout) findViewById(R.id.fondo_icon10);
        RelativeLayout r4 = (RelativeLayout) findViewById(R.id.fondo_icon14);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(3);
            }
        });

        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(4);
            }
        });

        r0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });


        mPager.setCurrentItem(1);


    }

    public void setPagerFragment(int a) {
        mPager.setCurrentItem(a);
    }

    /* PagerAdapter class */
    public class SlidePagerAdapter extends FragmentPagerAdapter {
        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /*
			 * IMPORTANT: This is the point. We create a RootFragment acting as
			 * a container for other fragments
			 */
            if (position == 4) {
                return new FragmentoInformacion();
            } else {
                if (position == 3) {
                    return new FragmentoDatosUsuario();
                } else {
                    if (position == 2) {
                        return new FragmentoHorario();
                    } else {
                        if (position == 0) {
                            return new FragmentoListaNoDisponible();
                        } else {
                            return new FragmentoLogin();
                        }
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

}