package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mmzhai on 3/2/16.
 */
public class ListAdapter extends ArrayAdapter{

    Context context;
    int layoutResourceId;
    public ArrayList<ListItem> reps;
    private Intent info;

    public ListAdapter(Context context, int layoutResourceId, ArrayList<ListItem> reps){
        super(context, layoutResourceId, reps);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.reps = reps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(layoutResourceId, parent, false);
        final int pos = position;
        final ListItem rep = reps.get(pos);

        TextView name = (TextView) convertView.findViewById(R.id.repName);
        name.setText(rep.repName + " [" + rep.party.charAt(0) + "]");

        ImageView photo = (ImageView) convertView.findViewById(R.id.repImg);
        String photoUrl = "https://theunitedstates.io/images/congress/225x275/"+ rep.id +".jpg";
        new ImageFromUrl(photoUrl, photo).execute();

        ImageView webButton = (ImageView) convertView.findViewById(R.id.repWeb);

        webButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(rep.website));
                context.startActivity(intent);
            }
        });

        ImageView emailButton = (ImageView) convertView.findViewById(R.id.repEmail);
        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                String[] addresses = new String[1];
                addresses[0] = rep.email;
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        ImageView twitterButton = (ImageView) convertView.findViewById(R.id.repTwitter);

        Log.d("T", "BEFORE CALLING TWITTER FOR : " + rep.repName);

        final TextView tweet = (TextView) convertView.findViewById(R.id.tweet);
        //final ImageView photo = (ImageView) convertView.findViewById(R.id.repImg);
        final View finalConvertView = convertView;
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, rep.twitter, 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        for (Tweet tweet : listResult.data) {
                            TextView twitter = (TextView) finalConvertView.findViewById(R.id.tweet);
                            twitter.setText(tweet.text);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        twitterButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        tweet.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        tweet.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ImageView detail = (ImageView) convertView.findViewById(R.id.btnDetails);
        detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                info = new Intent(context, DetailsActivity.class);
                info.putExtra("Representative", rep);
                context.startActivity(info);
            }
        });
        return convertView;
    }
    public ListItem getRep(int position) {
        return reps.get(position);
    }


}
