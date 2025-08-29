package com.srihari.contactverse.services;


import com.srihari.contactverse.entities.Contact;
import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.repositories.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Contact save(Contact contact) {
        //code to save the contact
        if(contact.getContactId() == null) {
            Integer contactId = UUID.randomUUID().hashCode();
            contact.setContactId(contactId);
        }


        return  contactRepo.save(contact);
    }

    public Contact update(Contact contact) {
//       ? TODO
        return  contactRepo.save(contact);
    }

    public Contact findById(Integer id) {
        return contactRepo.findById(id).orElse(null);
    }

    public Contact findByEmail(String email) {
        return contactRepo.findByEmail(email);
    }

    public List<Contact> findAll() {
        return contactRepo.findAll();
    }

    public void deleteById(Integer id) {
        Contact contact = contactRepo.findById(id).orElseThrow(/*...*/);
        // delete asset
        if (contact.getProfilePicPublicId() != null) {
            cloudinaryService.delete(contact.getProfilePicPublicId());
        }
        contactRepo.delete(contact);
    }


    public List<Contact> searchByName(String name) {
        return contactRepo.findByName(name);
    }

    public Contact searchByKeyword(String keyword) {
        return (Contact) contactRepo.searchByKeyword(keyword);
    }

        public List<Contact> getContactsByUser(User user) {
        return contactRepo.getContactByUser(user);
    }

    public Page<Contact> findByUser(User user, int page, int size, String sortBy, String sortDirection) {

        Sort sort = sortDirection.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size);
        return contactRepo.findByUser(user, pageable);
    }

    public List<Contact> searchByKeywordAndUser(String keyword, User user) {
        return contactRepo.searchByKeywordAndUser(keyword, user);
    }



}
