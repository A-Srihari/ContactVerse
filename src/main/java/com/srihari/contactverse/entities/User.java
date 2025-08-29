package com.srihari.contactverse.entities;

import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public class User implements UserDetails {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId; // Ensure this is not final or immutable

    @Column(name = "username", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;
    private String password;
    private String about;
    @Column(name = "profile_pic")
    private String profilePic;
    private String phoneNumber;
    private boolean isEnabled ;
    private boolean isEmailVerified = false;
    private boolean isPhoneVerified = false;
    @Column(name = "profile_pic_public_id")
    private String profilePicPublicId;


    @Enumerated(value = EnumType.STRING)
    private Provider provider = Provider.SELF;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();



//    ! DON'T GO DOWN



// getters and setters


//    ? SETTERS

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setProfilePicPublicId(String profilePicPublicId) { this.profilePicPublicId = profilePicPublicId; }

//    TODO GETTERS

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAbout() {
        return about;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public String getProfilePicPublicId() { return profilePicPublicId; }

    //implementing the UserDetailsMethods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @Override
//    public boolean isEnabled() {
//        return isEnabled;
//    }


}