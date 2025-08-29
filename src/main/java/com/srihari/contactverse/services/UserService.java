package com.srihari.contactverse.services;

import com.srihari.contactverse.entities.User;
import com.srihari.contactverse.helpers.ResourceNotFoundException;
import com.srihari.contactverse.repositories.UserRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.id.uuid.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public User saveUser(User user) {
        Integer userId = UUID.randomUUID().toString().hashCode();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getProfilePic() );
        System.out.println(user.getProfilePicPublicId());
        return userRepo.save(user);
    }


    public Optional<User> getUserById(Integer userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> updateUser(User user) {
    // Fetch user by ID or throw NotFoundException
    User user2 = userRepo.findById(user.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    // Update user details (primary key remains unchanged)
    user2.setName(user.getName());
    user2.setEmail(user.getEmail());
    user2.setPassword(user.getPassword());
    user2.setAbout(user.getAbout());
    user2.setPhoneNumber(user.getPhoneNumber());
    user2.setProfilePic(user.getProfilePic());
    user2.setEnabled(user.isEnabled());
    user2.setEmailVerified(user.isEmailVerified());
    user2.setPhoneVerified(user.isPhoneVerified());
    user2.setProvider(user.getProvider());
    user2.setProviderId(user.getProviderId());
    logger.info("User : " + user2.getProfilePicPublicId());

        // user2.setContacts(user.getContacts());

    logger.info("User Updated Successfully");

    return Optional.of(userRepo.save(user2));
}

    public void deleteUser(Integer userId) {
        User user2 = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.save(user2);

    }

    public boolean isUserExists(Integer userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2 != null;

    }

    public boolean isUserExistsByEmail(String email) {
        Optional<User> user2 = userRepo.findByEmail(email);
        return user2.isPresent();
    }

   public List<User> getAllUsers(){
        return userRepo.findAll();
    }

   public User findUserByEmail(String email) {
        return  userRepo.findByEmail(email).orElse(null);
   }

}