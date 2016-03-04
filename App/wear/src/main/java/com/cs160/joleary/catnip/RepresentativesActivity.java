package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RepresentativesActivity extends Activity implements SensorEventListener {

    private TextView mTextView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);

        setContentView(R.layout.representatives);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String repInfo = extras.getString("Representatives");
        //ArrayList<String[]> reps = (ArrayList<String[]>)extras.getSerializable("Representatives");
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(new GridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, int i1, float v, float v1, int i2, int i3) {
            }

            @Override
            public void onPageSelected(int row, int col) {
                //Send message to phone
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                String info = "" + row + "," + col;
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
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        if (Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1] + event.values[2]*event.values[2]) > 30) {
            Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
            String info = "" + (int)(10000 + Math.random() * (90000)); // random zipcode

            Toast toast = Toast.makeText(getApplicationContext(), "Zipcode: "+info, Toast.LENGTH_SHORT);
            toast.show();
            Log.d("T", "SHAKE: SENDING INTENT TO WATCHTOSERVICE");
            sendIntent.putExtra("Shake", info);
            startService(sendIntent);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
