package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mmzhai on 3/2/16.
 */
public class ListAdapter extends ArrayAdapter{
    Context context;
    int layoutResourceId;
    ArrayList<ListItem> reps;
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

        final ListItem rep = reps.get(position);
        ImageView photo = (ImageView) convertView.findViewById(R.id.repImg);
        photo.setImageResource(rep.repPhoto);
        TextView name = (TextView) convertView.findViewById(R.id.repName);
        name.setText(rep.repName);
        TextView twitter = (TextView) convertView.findViewById(R.id.tweet);
        twitter.setText(rep.twitter);

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
        final TextView tweet = (TextView) convertView.findViewById(R.id.tweet);
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


}
