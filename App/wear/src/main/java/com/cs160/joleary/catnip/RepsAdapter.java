package com.cs160.joleary.catnip;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;


public class RepsAdapter extends FragmentGridPagerAdapter {
    final private Context context;
    private int numReps;
    private String[][][] PAGES;

    public RepsAdapter(Context c, FragmentManager frag, String info) {
        super(frag);
        context = c;
        String[] s = info.split(",");
        Log.d("T", "STRING PASSED IN ADAPTER: " + info);
        numReps = (s.length - 1)/2;
        PAGES = new String[2][numReps][2];
        for (int i = 0; i < numReps; i++) {
            if (i == 0) {
                PAGES[0][0][0] = s[0];
                PAGES[0][0][1] = s[1];
                PAGES[1][0][0] = s[2];
            } else {
                PAGES[0][i][0] = s[2*i + 1];
                PAGES[0][i][1] = s[2*i + 2];
            }
        }
        //for (int i = 0; i < 2); i++) {
            /*PAGES[0][i].name = r.get(i)[0];
            PAGES[0][i].party = r.get(i)[1];
            PAGES[0][i].demVote = r.get(0)[2];*/
        //}
    }
    /*private static class Page{
        String name;
        String party;
        String demVote;

    }*/

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount(int i) {
        if (i == 0) {
            return numReps;
        } else {
            return 1;
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        /*Page page = PAGES[row][col];
        String name = page.name;
        String party = page.party;
        int demVote = Integer.parseInt(page.demVote);
        int repVote = 100 - demVote;*/

        Log.d("T", "MAKING FRAGMENT: row = " + row + " col = " + col);
        CardFragment fragment;
        if (row == 0) {
            fragment = CardFragment.create(PAGES[row][col][0], PAGES[row][col][1]);
        } else {
            fragment = CardFragment.create("2012 Elections", "Dem: " + PAGES[1][0][0] + "  Rep: " + (100 - Integer.parseInt(PAGES[1][0][0])));
        }
        return fragment;
    }
    /*@Override
    public Drawable getBackgroundForRow(int row) {
        return context.getResources().getDrawable(
                (BG_IMAGES[row % BG_IMAGES.length]), null);
    }*/
}
