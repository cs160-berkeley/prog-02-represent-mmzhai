package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);

        setContentView(R.layout.activity_main);

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
