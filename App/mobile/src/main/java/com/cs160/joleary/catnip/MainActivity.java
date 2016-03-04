package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    public final static String EXTRA_MESSAGE = "com.cs160.joleary.catnip.MESSAGE";
    private ImageButton mFredButton;
    private ImageButton mLexyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*

        mFredButton = (ImageButton) findViewById(R.id.zip_code_btn);
        mLexyButton = (ImageButton) findViewById(R.id.zip_code_btn);

        mFredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("CAT_NAME", "Fred");
                startService(sendIntent);
            }
        });

        mLexyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("CAT_NAME", "Lexy");
                startService(sendIntent);
            }
        });*/

    }

    public void selectLocation(View view) {
        Intent reps = new Intent(this, RepresentativesActivity.class);
        /*Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        String info = "Barbara Lee,Democratic Party,60,Dianne Feinstein,Democratic Party,Barbara Boxer,Democratic Party";
        Log.d("T", "SENDING INTENT TO PHONETOWATCHSERVICE");
        sendIntent.putExtra("Representatives", info);
        startService(sendIntent);*/
        EditText zip = (EditText) findViewById(R.id.zipcode);
        String s = zip.getText().toString();

        reps.putExtra("Zip", s);
        //ImageButton hereButton = (ImageButton) findViewById(R.id.here_btn);
        startActivity(reps);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
