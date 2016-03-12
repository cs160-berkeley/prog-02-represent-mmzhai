package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RepresentativesActivity extends Activity {
    private ArrayList<ListItem> reps;
    float demVotes;
    float repVotes;
    private static int zip;
    private static float lat;
    private static float lon;
    private static String sunlightUrl;
    private static String sunlightApiKey = "3162e3f5e7b4463187d84fb5638e9c42";
    private static String googleApiKey = "AIzaSyBoS6f0zcT74n9nn5cZrzMKebZ31NSIQlQ";
    private JSONObject repData;
    private String id;
    private static String zipCode;
    private static String county;
    private static String state;
    private static JSONArray voteArr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        setContentView(R.layout.activity_representatives);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (extras != null && extras.getString("Zip") != null) {
            Log.d("T", "I HAS DEM EXTRAZ: " + zip + ", " + lat + ", " + lon);
            zipCode = extras.getString("Zip");
            String[] ext = zipCode.split(",");
            String queryUrl = null;
            if (ext.length == 1) {
                zip = Integer.parseInt(ext[0]);
                if (zip == -1) { // was a shake
                    // randomly pick a county

                    Log.d("T", "SHAKEYSHAKEYSHAKEY ");
                    int ran = (int)(Math.random() * (voteArr.length() + 1));
                    try {
                        if (voteArr == null) {
                            try {
                                InputStream stream = getAssets().open("election-county-2012.json");
                                int size = 0;
                                size = stream.available();

                                byte[] buffer = new byte[size];
                                stream.read(buffer);
                                stream.close();
                                String jsonString = new String(buffer, "UTF-8");
                                voteArr = new JSONArray(jsonString);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        county = voteArr.getJSONObject(ran).getString("county-name");
                        state = voteArr.getJSONObject(ran).getString("state-postal");
                        String countyQuery = county + "+County";
                        countyQuery = countyQuery.replace(" ", "+");
                        String geourl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + countyQuery + "&key=" + googleApiKey;
                        Log.d("T", "QUERYING FOR RANDOM COUNTY " + geourl);
                        JSONObject googleLocation = new NetworkHelper().execute(geourl).get();
                        Log.d("T", "GOT SOMETHING FROM GOOGLE " + googleLocation);
                        JSONObject geometry = googleLocation.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                        JSONObject loc = geometry.getJSONObject("location");
                        lat = Float.parseFloat(loc.getString("lat"));
                        lon = Float.parseFloat(loc.getString("lng"));

                        Log.d("T", "SHAKEYSHAKE LAT LONG: " + lat + ", " + lon);
                        sunlightUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + lat + "&longitude=" + lon + "&apikey=" + sunlightApiKey;
                        queryUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat+","+lon+ "&key=" + googleApiKey;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                } else {
                    sunlightUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zip + "&apikey=" + sunlightApiKey;
                    //  getting county from zip
                    queryUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + zip + "&key=" + googleApiKey;
                }
            } else {
                lat = Float.parseFloat(ext[0]);
                lon = Float.parseFloat(ext[1]);
                sunlightUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + lat + "&longitude=" + lon + "&apikey=" + sunlightApiKey;
                // getting county from long/lat
                queryUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat+","+lon+ "&key=" + googleApiKey;
            }
            try {
                JSONObject geo = new NetworkHelper().execute(queryUrl).get();
                JSONArray json = geo.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < json.length(); i++) {
                    JSONObject component = json.getJSONObject(i);
                    JSONArray type = component.getJSONArray("types");
                    for (int j = 0; j < type.length(); j++) {
                        if (type.getString(j).equals("administrative_area_level_2")) {
                            county = component.getString("short_name").replace(" County", "");
                        }
                    }
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            Log.d("T", "ON REPRESENTATIVE PAGE WITH NOTHING TO SHOW: " + zip + ", " + lat + ", " + lon);
        }
        String[] urlArr = new String[1];
        urlArr[0] = sunlightUrl;
        String district = null;
        try {
            repData = new NetworkHelper().execute(urlArr).get();
            JSONArray repArr = repData.getJSONArray("results");

            Log.d("T", "DATA READ FROM STARLIGHT: " + repData);
            String watchInfo = "";
            reps = new ArrayList<>();
            for (int i = 0; i < repArr.length(); i++) {
                final JSONObject rep = repArr.getJSONObject(i);
                if (rep.getString("district") != "null"){
                    district = rep.getString("district");
                }
                String firstname = rep.getString("first_name");
                final String lastname = rep.getString("last_name");
                String party = rep.getString("party");
                if (party.equals("D")) {
                    party = "Democratic";
                } else if (party.equals("R")) {
                    party = "Republican";
                } else {
                    party = "Independent";
                }
                String term = rep.getString("term_end");
                String email = rep.getString("oc_email");
                String website = rep.getString("website");
                final String twitter = rep.getString("twitter_id");
                String title = rep.getString("title");
                id = rep.getString("bioguide_id");

                String comUrl = "https://congress.api.sunlightfoundation.com/committees?member_ids=" +
                        id +"&apikey=" + sunlightApiKey;

                JSONObject repCommittees = new NetworkHelper().execute(comUrl).get();
                JSONArray comArr = repCommittees.getJSONArray("results");
                String committees = "";
                for (int j = 0; j < comArr.length(); j++) {
                    JSONObject com = comArr.getJSONObject(j);
                    committees += com.getString("name")+ "\n\n";
                }

                String billUrl = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=" +
                        id + "&apikey=" + sunlightApiKey;

                JSONObject repBills = new NetworkHelper().execute(billUrl).get();

                JSONArray billArr = repBills.getJSONArray("results");
                String bills = "";
                int count = 0;
                for (int j = 0; j < billArr.length() && count < 5; j++) {
                    JSONObject bill = billArr.getJSONObject(j);
                    String shortTitle = bill.getString("short_title");
                    if (shortTitle != "null") {
                        String date = bill.getString("introduced_on");
                        bills += date + ": " + shortTitle + "\n\n";
                        count ++;
                    }
                }

                // append info for sending to watch
                watchInfo += firstname + " " + lastname +"," + party + ",";
                if (i == 0) {
                    //get votes data
                    if (voteArr == null) {
                        try {
                            InputStream stream = getAssets().open("election-county-2012.json");
                            int size = 0;
                            size = stream.available();

                            byte[] buffer = new byte[size];
                            stream.read(buffer);
                            stream.close();
                            String jsonString = new String(buffer, "UTF-8");
                            voteArr = new JSONArray(jsonString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int k = 0; k < voteArr.length(); k++) {
                        JSONObject location = voteArr.getJSONObject(k);

                        if (location.getString("county-name").equals(county)) {
                            demVotes = Float.parseFloat(location.getString("obama-percentage"));
                            repVotes = Float.parseFloat(location.getString("romney-percentage"));
                            state = location.getString("state-postal");
                            break;
                        }
                    }
                    watchInfo += demVotes+"!!"+repVotes+"!!"+county+" county!!"+state+ ",";
                }
                // append info for phone
                ListItem repList = new ListItem(title + " " + firstname + " " + lastname, term, website, email, twitter, committees, bills, party, id, zipCode);
                reps.add(repList);

                setTitle("District " + district);

            }

            if (extras != null && extras.getString("Position") != null) {
                String position = extras.getString("Position");
                int pos = Integer.parseInt(position);
                Intent info = new Intent(this, DetailsActivity.class);
                //ListItem rep = reps.get(pos);
                //rep.repUrl = adapter.getUrl(pos);
                info.putExtra("Representative", reps.get(pos));
                this.startActivity(info);
            }

            else {
                // send intent to watch with representatives info
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                Log.d("T", "SENDING INTENT TO PHONETOWATCHSERVICE");
                String newZip = zipCode.replace(",", "!!");
                watchInfo += "," + newZip;
                sendIntent.putExtra("Representatives", watchInfo);
                startService(sendIntent);

                ListView repList = (ListView) findViewById(R.id.repList);
                ListAdapter adapter = new ListAdapter(this, R.layout.list_group, reps);

                repList.setAdapter(adapter);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }







    }
}
