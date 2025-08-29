package com.srihari.contactverse.config;


import com.srihari.contactverse.entities.Provider;
import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.repositories.UserRepo;
import com.srihari.contactverse.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//        identify the provider (google/github)


        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        System.out.println(authorizedClientRegistrationId);




        var oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        oauth2User.getAttributes().forEach((k,v) -> System.out.println(k + " : " + v));

        User user = new User();
        user.setName(oauth2User.getAttribute("name"));
        user.setEmail(oauth2User.getAttribute("name"));
        user.setEmailVerified(true);
        user.setProvider(Provider.GITHUB);
        user.setProviderId(oauth2User.getName());
        user.setEnabled(true);
        user.setUserId(UUID.randomUUID().toString().hashCode());



        if(authorizedClientRegistrationId.equals("google")){

            //google login

            user.setProvider(Provider.GOOGLE);
            user.setEmail(oauth2User.getAttribute("email"));
            user.setEmailVerified(true);
            user.setUserId(UUID.randomUUID().toString().hashCode());
            user.setEnabled(true);
            user.setProfilePic(oauth2User.getAttribute("picture"));
            user.setName(oauth2User.getAttribute("name"));
            user.setProviderId(oauth2User.getName());
            user.setAbout("This account was created using Google OAuth2");


        } else if(authorizedClientRegistrationId.equals("github")){

            //github login
            String email = oauth2User.getAttribute("email") !=null? oauth2User.getAttribute("email") : oauth2User.getAttribute("login").toString() + "@email.com";
            String name = oauth2User.getAttribute("name");
            String profilePic = oauth2User.getAttribute("avatar_url");
            String providerId = oauth2User.getName();
            user.setEmail(email);
            user.setProvider(Provider.GITHUB);
            user.setProfilePic(profilePic);
            user.setName(name);
            user.setProviderId(oauth2User.getName());
            user.setAbout("This account was created using Github OAuth2");


        }
        
        
        //saving user
        User savedUser = userRepo.findByEmail(user.getEmail()).orElse(null);
        System.out.println(user);
        if (savedUser == null) {
            userRepo.save(user);
            System.out.println("User Saved Successfully" + user);
        }



        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
