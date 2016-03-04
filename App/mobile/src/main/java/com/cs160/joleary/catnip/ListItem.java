package com.cs160.joleary.catnip;

import java.io.Serializable;

/**
 * Created by mmzhai on 3/2/16.
 */
public class ListItem implements Serializable{
    public String repName;
    public int repPhoto;
    public String repTerm;
    public String website;
    public String email;
    public String twitter;
    public String repCommittees;
    public String repBills;
    public String party;

    public ListItem(String repName, int repPhoto, String repTerm, String website, String email, String twitter, String repCommittees, String repBills, String party) {
        this.repName = repName;
        this.repPhoto = repPhoto;
        this.repTerm = repTerm;
        this.website = website;
        this.email = email;
        this.twitter = twitter;
        this.repCommittees = repCommittees;
        this.repBills = repBills;
        this.party = party;
    }
}
