package com.codefellows.xisbi;

import javax.persistence.*;

@Entity
public class User {

    // Generate XISBI table within xisbi_user database with a table name of the class "user"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // XISBI User Properties
    public long id;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String partyInterests;

    // XISBI User Constructors
    public User() {}
    public User(String username, String password, String firstName, String lastName, String dateOfBirth, String partyInterests) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.partyInterests = partyInterests;
    }

        // Data returns XISBI user properties as a string (minus the password)
        public String toString () {
            return "Username: " + username + "\nName: " + firstName + " " + lastName + "\nDOB: " + dateOfBirth + "\nParty Interests: " + partyInterests;
        }


}
