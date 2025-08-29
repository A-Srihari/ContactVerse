package com.srihari.contactverse.controller;

import com.srihari.contactverse.entities.Contact;
import com.srihari.contactverse.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{id}")
    public Contact getContactById(@PathVariable int id) {
        return contactService.findById(id);
    }

}
