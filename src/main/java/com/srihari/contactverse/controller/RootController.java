package com.srihari.contactverse.controller;


import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.helpers.Helper;
import com.srihari.contactverse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice //methods in this will be executed for all the requests
public class RootController {



    @Autowired
    UserService userService;

    @ModelAttribute
    public void getCurrentUser(Authentication authentication, Model model) {
        if(authentication == null) {
            return;
        }

        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);
        System.out.println("username is " + username);
        User user = userService.findUserByEmail(username);

        if (user == null) {
            return;
        }

        System.out.println("name: " + user.getName());
        System.out.println("email: " + user.getEmail());

        model.addAttribute("loggedInUser", user);
        model.addAttribute("loggedInUserFullName", user.getName());
        model.addAttribute("loggedInUserName", user.getUsername());
        model.addAttribute("loggedInUserId", user.getUserId());
        model.addAttribute("loggedInUserEmail", user.getEmail());
        model.addAttribute("loggedInUserPhone", user.getPhoneNumber());
        model.addAttribute("loggedInUserProvider", user.getProvider());
        model.addAttribute("loggedInUserProviderId", user.getProviderId());
        model.addAttribute("loggedInUserEnabled", user.isEnabled());
        model.addAttribute("loggedInUserEmailVerified", user.isEmailVerified());
        model.addAttribute("loggedInUserPhoneVerified", user.isPhoneVerified());
        model.addAttribute("loggedInUserAbout", user.getAbout());
        model.addAttribute("loggedInUserProfilePic", user.getProfilePic());
        System.out.println("loggedInUserName is " + model.getAttribute("loggedInUserName"));
    }

}
