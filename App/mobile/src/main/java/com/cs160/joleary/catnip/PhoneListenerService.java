package com.cs160.joleary.catnip;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

//   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String msg = "/Card";
    private static final String msg2 = "/Shake";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(msg) ) {

            // Value contains the String we sent over in WatchToPhoneService, which is card info
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            // parse value for row and column
            String[] position = value.split(",");
            if (Integer.parseInt(position[0]) == 0) {
                Log.d("T", "about to call RepresentativesActivity.GoToDetails");
                Intent sendIntent = new Intent(getBaseContext(), RepresentativesActivity.class);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String info = "" + position[1];
                String zipInfo = position[2];
                zipInfo = zipInfo.replace("!!", ",");
                sendIntent.putExtra("Position", info);
                sendIntent.putExtra("Zip", zipInfo);
                startActivity(sendIntent);
            }
            // Make a toast with the String
            /*Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, value, duration);
            toast.show();*/

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions
        } else if ( messageEvent.getPath().equalsIgnoreCase(msg2) ){
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent sendIntent = new Intent(getBaseContext(), RepresentativesActivity.class);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.putExtra("Zip", value);
            startActivity(sendIntent);

        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}
