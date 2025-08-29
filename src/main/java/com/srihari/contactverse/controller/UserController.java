package com.srihari.contactverse.controller;

import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.forms.UserForm;
import com.srihari.contactverse.services.UserService;
import com.srihari.contactverse.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    //dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "user/dashboard";
    }

    //profile page
    @GetMapping("/profile")
    public String profile(Model model) {
        return "user/profile";
    }

    // ✅ Registration handler
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("userForm") UserForm userForm,
            Model model) {

        try {
            User user = new User();
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            user.setPassword(userForm.getPassword());
            user.setPhoneNumber(userForm.getPhoneNumber());
            user.setAbout(userForm.getAbout());

            // Handle profile picture upload
            if (userForm.getProfilePicture() != null && !userForm.getProfilePicture().isEmpty()) {
                CloudinaryService.ImageUploadResult res = cloudinaryService.upload(userForm.getProfilePicture());
                user.setProfilePic(res.getUrl());
                user.setProfilePicPublicId(res.getPublicId());
                System.out.println("Profile pic : " + user.getProfilePic());
                System.out.println("Profile pic public Id : " + user.getProfilePicPublicId());
            } else {
                user.setProfilePic("https://res.cloudinary.com/dunaaon7s/image/upload/v1756354676/user_oupfc4.jpg");
            }

            userService.saveUser(user);

            return "redirect:/login"; // after signup → login page
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Registration failed. Please try again.");


            System.out.println("Uploaded file name: " + userForm.getProfilePicture().getOriginalFilename());
            System.out.println("Is empty? " + userForm.getProfilePicture().isEmpty());


            return "signup";
        }
    }

}
