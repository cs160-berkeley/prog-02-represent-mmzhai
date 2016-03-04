package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

public class RepresentativesActivity extends Activity {
    private ArrayList<ListItem> reps;
    int demVotes = (int)(Math.random() * (101));
    private int zip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && extras.getString("Zip") != null) {
            zip = Integer.parseInt(extras.getString("Zip"));
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Zipcode: "+zip, duration);
            toast.show();

            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            String info = "Barbara Lee,Democratic Party,"+demVotes+",Dianne Feinstein,Democratic Party,Barbara Boxer,Democratic Party";
            Log.d("T", "SENDING INTENT TO PHONETOWATCHSERVICE");
            sendIntent.putExtra("Representatives", info);
            startService(sendIntent);
        }


        setContentView(R.layout.activity_representatives);
        setTitle("CA district 13");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView repList = (ListView) findViewById(R.id.repList);
        String name1 = "Rep. Barbara Lee [D]";
        String name2 = "Sen. Dianne Feinstein [D]";
        String name3 = "Sen. Barbara Boxer [D]";

        String term = "Term ends January 2017";

        String website1 = "http://lee.house.gov/";
        String website2 = "http://feinstein.senate.gov/";
        String website3 = "http://boxer.senate.gov/";

        String email1 = "lee@mail.house.gov";
        String email2 = "senator@feinstein.senate.gov";
        String email3 = "senator@boxer.senate.gov";

        String twitter1 = "Can’t think of a better way to kick off #WomensHistoryMonth than with " +
                "champions @FLOTUS & @DrBiden! #WomenSucceed ";
        String twitter2 = "The federal government needs authority to aggressively pursue " +
                "transnational criminal organizations to reduce flow of drugs into our country.";
        String twitter3 = "This outrageous law has already forced more than half the clinics in " +
                "Texas to close. We must #StopTheSham http://wapo.st/1WTegX0 ";

        String bill1 = "H.R. 3712: Improving Access to Mental Health Act\n\nH.Con.Res. 77: Recognizing " +
                "the 70th anniversary of the establishment of the United Nations\n\nH.R. 2972: Equal " +
                "Access to Abortion Coverage in Health Insurance (EACH Woman) Act of 2015";
        String bill2= "H.Con.Res. 77: Recognizing \n\n" +
                "\"the 70th anniversary of the establishment of the United Nations\n\nH.R. 2972: Equal \n\n" +
                "\"Access to Abortion Coverage in Health Insurance (EACH Woman) Act of 2015\n\n" +
                "H.R. 2721: Pathways Out of Poverty Act of 2015\n\n" +
                "Access to Abortion Coverage in Health Insurance (EACH Woman) Act of 2015";
        String bill3 = "H.Con.Res. 77: Recognizing \n\n" +
                "\"the 70th anniversary of the establishment of the United Nations\n\nH.R. 2972: Equal \n\n" +
                "\"Access to Abortion Coverage in Health Insurance (EACH Woman) Act of 2015\n\n" +
                "H.R. 2721: Pathways Out of Poverty Act of 2015";

        String com1 = "Committee on Appropriations\n" +
                "House Committee on The Budget";
        String com2 = "Committee on Appropriations\n" +
                "Committee on the Judiciary\n" +
                "Committee on Rules and Administration\n" +
                "Select Committee on Intelligence";
        String com3 = "Committee on Commerce, Science, and Transportation\n" +
                "Committee on Environment and Public Works\n" +
                "Committee on Foreign Relations\n" +
                "Select Committee on Ethics";


        ListItem rep1 = new ListItem(name1, R.drawable.barbaralee, term, website1, email1, twitter1, com1, bill1, "Democratic Party");
        ListItem rep2 = new ListItem(name2, R.drawable.dianne, term, website2, email2, twitter2, com2, bill2, "Democratic Party");
        ListItem rep3 = new ListItem(name3, R.drawable.boxer, term, website3, email3, twitter3, com3, bill3, "Democratic Party");

        reps = new ArrayList<>();
        reps.add(rep1);
        reps.add(rep2);
        reps.add(rep3);



        if (extras != null && extras.getString("Position") != null) {
            String position = extras.getString("Position");
            int pos = Integer.parseInt(position);
            Intent info = new Intent(this, DetailsActivity.class);
            info.putExtra("Representative", reps.get(pos));
            this.startActivity(info);
        }



        final ListAdapter adapter = new ListAdapter(this, R.layout.list_group, reps);
        repList.setAdapter(adapter);
    }
}
