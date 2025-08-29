package com.srihari.contactverse.controller;

import com.srihari.contactverse.entities.Contact;
import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.forms.ContactForm;
import com.srihari.contactverse.helpers.Helper;
import com.srihari.contactverse.services.CloudinaryService;
import com.srihari.contactverse.services.ContactService;
import com.srihari.contactverse.services.ImageService;
import com.srihari.contactverse.services.UserService;
import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @RequestMapping("/addContact" )
    public String addContact(Model model, HttpSession session) {
        System.out.println("Add Contact Page");
        model.addAttribute("contactForm", new ContactForm());
        return "user/addContact";
    }


    Logger logger = LoggerFactory.getLogger(ContactController.class);


    @RequestMapping(value = "/addContact", method = RequestMethod.POST)
    public String processContact(@Valid @ModelAttribute ContactForm contactForm,
                                 BindingResult result,
                                 Authentication authentication) throws IOException {
        if (result.hasErrors()) {
            return "user/addContact";
        }
        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);

        Contact contact = new Contact();
        contact.setUser(userService.findUserByEmail(username));
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAbout(contactForm.getAbout());
        contact.setAddress(contactForm.getAddress());
        contact.setLinkedin(contactForm.getLinkedin());
        contact.setInstagram(contactForm.getInstagram());
        contact.setFavorite(contactForm.isFavorite());

        MultipartFile file = contactForm.getProfilePic();
        if (file != null && !file.isEmpty()) {
            try {
                CloudinaryService.ImageUploadResult res = cloudinaryService.upload(file);
                contact.setProfilePic(res.getUrl());
                contact.setProfilePicPublicId(res.getPublicId());
            } catch (Exception ex) {
                contact.setProfilePic("/images/logo.png");
                logger.error("Cloudinary upload failed, falling back to default image", ex);
            }
        } else if (file == null || file.isEmpty()) {
            contact.setProfilePic("https://res.cloudinary.com/dunaaon7s/image/upload/v1756354676/user_oupfc4.jpg");

        }

        contactService.save(contact);
        return "redirect:/user/contact/addContact";
    }


    @GetMapping("/saved-contacts")
    public String contacts(
            @RequestParam(value = "size", defaultValue = "100000") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name")  String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc")   String sortDirection,
            Authentication authentication, Model model) {
        System.out.println("Contacts Page");

        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.findUserByEmail(username);
        Page<Contact> pageContact = contactService.findByUser(user, page, size, sortBy, sortDirection);
        model.addAttribute("contacts", pageContact);

        return "user/contacts";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword,
                         Authentication authentication,
                         Model model) {
        System.out.println("Searching for: " + keyword);

        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.findUserByEmail(username);

        List<Contact> results = contactService.searchByKeywordAndUser(keyword, user);

        model.addAttribute("contacts", new PageImpl<>(results));

        return "user/contacts";  // same page as saved-contacts
    }


    //    TODO displaying the contact details card/ modal
    @GetMapping("/{id}")
    @ResponseBody
    public Contact getContactById(@PathVariable("id") Integer id, Authentication authentication) {
        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.findUserByEmail(username);

        Contact contact = contactService.findById(id);

        if (contact != null && contact.getUser().getUserId().equals(user.getUserId())) {
            return contact;
        } else {
            throw new RuntimeException("Contact not found or unauthorized access!");
        }
    }

    //TODO: delete contact
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteContact(@PathVariable("id") Integer id, Authentication authentication) {
        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.findUserByEmail(username);
        Contact contact = contactService.findById(id);
        if (contact != null && contact.getUser().getUserId().equals(user.getUserId())) {
            // delete cloud asset if exists
            if (contact.getProfilePicPublicId() != null && !contact.getProfilePicPublicId().isBlank()) {
                cloudinaryService.delete(contact.getProfilePicPublicId());
            }
            contactService.deleteById(id);
            return "Contact deleted successfully!";
        } else {
            throw new RuntimeException("Contact not found or unauthorized access!");
        }
    }



    //TODO    Updating the contact
    // Show Edit Contact Page
    @GetMapping("/edit/{id}")
    public String editContact(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);

        Contact contact = contactService.findById(id);

        // ensure the contact belongs to the logged-in user
        if (contact == null || !contact.getUser().getEmail().equals(username)) {
            return "redirect:/user/contacts"; // prevent editing others' contacts
        }

        // Pre-fill form
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAbout(contact.getAbout());
        contactForm.setAddress(contact.getAddress());
        contactForm.setLinkedin(contact.getLinkedin());
        contactForm.setInstagram(contact.getInstagram());
        contactForm.setFavorite(contact.isFavorite());

        // Profile pic not set here because it comes as MultipartFile only on upload

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", id);

        return "user/editContact"; // thymeleaf page
    }


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public String updateContact(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute("contactForm") ContactForm contactForm,
                                BindingResult result,
                                Authentication authentication) throws IOException {

        if (result.hasErrors()) {
            return "user/editContact";
        }

        Helper helper = new Helper();
        String username = helper.getEmailOfLoggedInUser(authentication);

        Contact contact = contactService.findById(id);

        // ensure the contact belongs to the logged-in user
        if (contact == null || !contact.getUser().getEmail().equals(username)) {
            return "redirect:/user/contacts";
        }

        // update fields
        contact.setContactId(id);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAbout(contactForm.getAbout());
        contact.setAddress(contactForm.getAddress());
        contact.setLinkedin(contactForm.getLinkedin());
        contact.setInstagram(contactForm.getInstagram());
        contact.setFavorite(contactForm.isFavorite());

        // update profile pic if new one uploaded
        MultipartFile file = contactForm.getProfilePic();
        if (file != null && !file.isEmpty()) {
            // delete existing cloud asset if present
            if (contact.getProfilePicPublicId() != null && !contact.getProfilePicPublicId().isBlank()) {
                cloudinaryService.delete(contact.getProfilePicPublicId());
            }
            try {
                CloudinaryService.ImageUploadResult res = cloudinaryService.upload(file);
                contact.setProfilePic(res.getUrl());
                contact.setProfilePicPublicId(res.getPublicId());
            } catch (Exception ex) {
                logger.error("Cloudinary upload failed during update", ex);
                // optionally keep old URL or set to default
            }
        }


        contactService.save(contact);

        return "redirect:/user/contact/saved-contacts";
    }


}
