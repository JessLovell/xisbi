package com.codefellows.xisbi;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class Auth {
    public static boolean isHost(XisbiUser user, Party party){

        // can just be
        // return user.id == party.partyHost.id;

        if (user.id == party.partyHost.id){
            return true;
        }
        return false;
    }
}
