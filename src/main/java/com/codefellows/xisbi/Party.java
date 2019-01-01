package com.codefellows.xisbi;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Party {

    // Generate PARTY table within xisbi_app database with a table name of the class "party"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // XISBI Party Properties
    public long id;
    public String partyTitle;
    public String partyTime;
    public String partyDate;
    public String partyLocation;
    public String partyDescription;

    @ManyToMany
    @JoinTable(name = "guest_list", joinColumns = {@JoinColumn(name = "party_id")}, inverseJoinColumns = {@JoinColumn(name = "guest_id")})
    public Set<XisbiUser> guestList;

    @ManyToOne
    public XisbiUser partyHost;

    // XISBI Party Constructors
    public Party() {}
    public Party(String partyTitle, String partyTime, String partyDate, String partyLocation, String partyDescription) {
        this.partyTitle = partyTitle;
        this.partyTime = partyTime;
        this.partyDate = partyDate;
        this.partyLocation = partyLocation;
        this.partyDescription = partyDescription;
    }

    public boolean updateParty(String partyTitle, String partyTime, String partyDate, String partyLocation, String partyDescription){
        this.partyTitle = partyTitle;
        this.partyTime = partyTime;
        this.partyDate = partyDate;
        this.partyLocation = partyLocation;
        this.partyDescription = partyDescription;

        return true;
    }

    // Data returns XISBI party properties as a string
    public String toString () {
        return "Title: " + partyTitle + "\nHost: " + partyHost + "\nTime: " + partyTime + "\nDate: " + partyDate + "\nLocation: " + partyLocation + "\nDescription/Theme: " + partyDescription;
    }
}
