package com.srihari.contactverse.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
//import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Data
public class Contact {

    @Id
    private Integer contactId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    @Column(name = "profile_pic")
    private String profilePic;
    @Column(name = "profile_pic_public_id")
    private String profilePicPublicId;
    private String about;
    private boolean isFavorite;
    private String instagram;
    private String linkedin;
//    private List<SocialLinks> socialLinks = new ArrayList<>();

    @ManyToOne
    @JsonIgnore(true)
    private User user;

    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SocialLinks> socialLinks = new ArrayList<>();


    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SocialLinks> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(List<SocialLinks> socialLinks) {
        this.socialLinks = socialLinks;
    }

    public String getProfilePicPublicId() { return profilePicPublicId; }
    public void setProfilePicPublicId(String profilePicPublicId) { this.profilePicPublicId = profilePicPublicId; }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", about='" + about + '\'' +
                ", isFavorite=" + isFavorite +
                ", instagram='" + instagram + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", user=" + user +
                ", socialLinks=" + socialLinks +
                '}';
    }
}
