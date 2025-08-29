package com.srihari.contactverse.forms;

//import lombok.Data;
//import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

//@Data
//@ToString
public class UserForm {

    private String name;
    private String email;
    private MultipartFile profilePicture;
    private String password;
    private String phoneNumber;
    private String about;
    private String profilePicPublicId;

    public void setProfilePicPublicId(String profilePicPublicId) { this.profilePicPublicId = profilePicPublicId; }
    public String getProfilePicPublicId() { return profilePicPublicId; }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

}
