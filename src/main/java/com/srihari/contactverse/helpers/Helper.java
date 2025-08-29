package com.srihari.contactverse.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public String getEmailOfLoggedInUser(Authentication authentication) {
        if(authentication instanceof OAuth2AuthenticationToken) {
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauth2user = (OAuth2User) authentication.getPrincipal();
            String username = "";
            if(clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting email from google");
                username = oauth2user.getAttribute("email").toString();
            } else if(clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting email from github");
                username = oauth2user.getAttribute("email") !=null? oauth2user.getAttribute("email") : oauth2user.getAttribute("login").toString() + "@email.com";

            }
            return username;
        } else {
            System.out.println("Getting data from local db");
            return authentication.getName();
        }
    }
}
