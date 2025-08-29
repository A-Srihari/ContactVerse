package com.srihari.contactverse.entities;

import jakarta.persistence.*;
//import lombok.Data;
//import lombok.Generated;

//@Data
@Entity
public class SocialLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String link;
    private String title;

    @ManyToOne
    private Contact contact;

}
