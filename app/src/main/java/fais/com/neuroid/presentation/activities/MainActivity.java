package fais.com.neuroid.presentation.activities;


import android.app.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.Toolbar;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.TrainDataSet;
import fais.com.neuroid.Neural.NeuralNetwork;
import fais.com.neuroid.R;
import fais.com.neuroid.presentation.AndroidOutputProvider;
import fais.com.neuroid.presentation.widgets.NotSwipeablePager;
import fais.com.neuroid.utilities.Util;


/**
 * This activity holds activity_result application view
 */
public class MainActivity extends AppCompatActivity {


    @Bind(R.id.viewPager)
    public NotSwipeablePager viewPager;

    @Bind(R.id.sliding_tabs)
    public TabLayout slidingTabs;



    private NeuralNetwork neuralNetwork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view
        setContentView(R.layout.activity_main);
        // bind this view
        ButterKnife.bind(this);
        viewPager.setAdapter(new NeuroPagerAdapter(getSupportFragmentManager()));
        viewPager.setPagingEnabled(false);
        slidingTabs.setupWithViewPager(viewPager);
        slidingTabs.setTabGravity(TabLayout.GRAVITY_FILL);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    public void setNeuralNetwork(NeuralNetwork network) {
        this.neuralNetwork = network;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }


    public static class NeuroPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public NeuroPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LearnFragment.newInstance();
                case 1:
                    return RecognitionFragment.newInstance();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Learn";
                case 1:
                   return "Recognize";
                default:
                    return null;
            }
        }

    }

}
