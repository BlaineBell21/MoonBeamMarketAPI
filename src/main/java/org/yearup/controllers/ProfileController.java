package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.service.ProfileService;
import org.yearup.utils.ValidationCheck;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;


    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> getProfile(Principal principal){
        // retrieves a user's profile
        Profile userProfile = profileService.getProfileById(principal);
        // checks if user exists before returning response
        userValidation(userProfile);

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile){
        Profile userProfile = profileService.getProfileById(principal);
        // checks if user profile exists before attempting to update

        Profile updatedProfile = profileService.updateProfile(principal, profile);

        return ResponseEntity.ok(updatedProfile);

    }

    @DeleteMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteProfile(Principal principal){
        Profile userProfile = profileService.getProfileById(principal);
        // checks if user profile exists before attempting to delete
        userValidation(userProfile);

        int userId = profileService.getUserId(principal);
        profileService.deleteProfile(userId);
        return ResponseEntity.ok().build();
    }

    public static void userValidation(Profile profile){
        // verifies that the user exists in database
        if (profile == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Profile not found.\n" +
                            "This user either doesn't exist or the incorrect ID was inputted.");
        }
    }
}
