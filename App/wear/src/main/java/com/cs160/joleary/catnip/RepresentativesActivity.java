package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RepresentativesActivity extends Activity {

    private TextView mTextView;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Sensor mSensor;
    private String zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                String info = "-1";

                Log.d("T", "SHAKE: SENDING INTENT TO WATCHTOSERVICE");
                sendIntent.putExtra("Shake", info);
                startService(sendIntent);
            }

        });


        setContentView(R.layout.representatives);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String repInfo = extras.getString("Representatives");
        String[] info = repInfo.split(",");
        zip = info[info.length-1];
        //ArrayList<String[]> reps = (ArrayList<String[]>)extras.getSerializable("Representatives");
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        DotsPageIndicator dottiethings = (DotsPageIndicator) findViewById(R.id.dots);
        dottiethings.setPager(pager);
        dottiethings.setOnPageChangeListener(new GridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, int i1, float v, float v1, int i2, int i3) {
            }

            @Override
            public void onPageSelected(int row, int col) {
                //Send message to phone
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                String info = "" + row + "," + col + "," + zip;
                Log.d("T", "SENDING INTENT TO WATCHTOSERVICE");
                sendIntent.putExtra("Card", info);
                startService(sendIntent);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        pager.setAdapter(new RepsAdapter(this, getFragmentManager(), repInfo));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
