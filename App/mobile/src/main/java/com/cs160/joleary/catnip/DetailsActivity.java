package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        ListItem rep = (ListItem)intent.getSerializableExtra("Representative");

        Intent home=new Intent(this,RepresentativesActivity.class);
        home.putExtra("Zip", rep.zip);
        startService(home);

        setTitle(rep.repName);

        ImageView photo = (ImageView) findViewById(R.id.repPhoto);
        String photoUrl = "https://theunitedstates.io/images/congress/225x275/"+ rep.id +".jpg";
        new ImageFromUrl(photoUrl, photo).execute();
        TextView party = (TextView) findViewById(R.id.repParty);
        party.setText(rep.party + " party");
        TextView term = (TextView) findViewById(R.id.repTerm);
        term.setText("Term ends: " + rep.repTerm);
        TextView com = (TextView) findViewById(R.id.repCommittees);
        com.setText(rep.repCommittees);
        TextView bill = (TextView) findViewById(R.id.repBills);
        bill.setText(rep.repBills);

    }
}
