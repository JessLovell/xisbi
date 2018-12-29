package com.codefellows.xisbi;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class XisbiUser implements UserDetails {

    // Generate XISBI table within xisbi_user database with a table name of the class "user"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // XISBI XisbiUser Properties
    public long id;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String partyInterests;

    // XISBI XisbiUser Constructors
    public XisbiUser() {}
    public XisbiUser(String username, String password, String firstName, String lastName, String dateOfBirth, String partyInterests) {
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
