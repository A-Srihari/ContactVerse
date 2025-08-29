package com.srihari.contactverse.controller;


import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.forms.UserForm;
import com.srihari.contactverse.services.CloudinaryService;
import com.srihari.contactverse.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping({"/home", "/"})
    public String hello(Model model) {
        model.addAttribute("name", "Srihari");
        model.addAttribute("Lang","Java");
        model.addAttribute("Dev","Spring");
        model.addAttribute("link","https://www.youtube.com");
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        System.out.println("About Page");
        return "about";
    }

    @GetMapping("/services")
    public String service() {
        System.out.println("Service Page");
        return "contactUs";
    }

    @GetMapping("/contactus")
    public String contact() {
        System.out.println("Contact Us Page");
        return "contactUs";
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("Login Page");
        return "login";
    }
    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        model.addAttribute("userForm", new UserForm());
        System.out.println("Sign-Up Page");
        return "signup";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") UserForm userForm, Model model, HttpSession session) {
        try {
            User user = new User();
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            user.setPassword(userForm.getPassword());
            user.setPhoneNumber(userForm.getPhoneNumber());
            user.setAbout(userForm.getAbout());
            user.setEnabled(true);

            // Handle profile picture upload
            if (userForm.getProfilePicture() != null && !userForm.getProfilePicture().isEmpty()) {
                CloudinaryService.ImageUploadResult res = cloudinaryService.upload(userForm.getProfilePicture());
                user.setProfilePic(res.getUrl());
                user.setProfilePicPublicId(res.getPublicId());
            } else {
                user.setProfilePic("https://res.cloudinary.com/dunaaon7s/image/upload/v1756354676/user_oupfc4.jpg");
            }

            userService.saveUser(user);
            session.setAttribute("message", "Registration Successful! Please log in.");
            return "redirect:/login";

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Registration failed: " + e.getMessage());
            model.addAttribute("userForm", userForm);
            return "signup";
        }
    }


    @PostMapping("/invalidateMessage")
    @ResponseBody
    public String invalidateMessage(HttpSession session) {
//        session.removeAttribute("message");
            session.invalidate();
        return "Session message removed";

    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "user/profile";
    }
}