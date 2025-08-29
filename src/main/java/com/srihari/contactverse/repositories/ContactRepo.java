package com.srihari.contactverse.repositories;

import com.srihari.contactverse.entities.Contact;
import com.srihari.contactverse.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {


    Contact findByContactId(Integer contactId);

    @Query("SELECT c FROM Contact c WHERE c.name = :name")
    List<Contact> findByName(@Param("name") String name);


    @Query("SELECT c FROM Contact c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.about) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.linkedin) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.instagram) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(c.profilePic) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    List<Contact> searchByKeyword(@Param("keyword") String keyword);


    @Query("SELECT c FROM Contact c " +
            "WHERE c.user = :user AND (" +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.about) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.linkedin) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.instagram) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))"
    )
    List<Contact> searchByKeywordAndUser(@Param("keyword") String keyword,
                                         @Param("user") User user);


    Contact findByEmail(String email);

    List<Contact> getContactByUser(User user);

    Page<Contact> findByUser(User user, Pageable pageable);
}
