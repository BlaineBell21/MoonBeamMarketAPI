package org.yearup.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.repository.ProfileRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;
    private final UserService userService;

    public ProfileService(ProfileRepository profileRepository, UserService userService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
    }

    public List<Profile> getAllProfiles(){
        // retrieves all user profiles
        return profileRepository.findAll();
    }

    public Profile getProfileById(Principal principal){
        // retrieves user's profile by their user ID
        int userId = getUserId(principal);
        List<Profile> allProfiles = getAllProfiles();

        for (Profile profile : allProfiles){
            if (profile.getUserId() == userId){
                return profile;
            }
        }
        return null;
    }

    public int getUserId(Principal principal){
        // retrieves user ID of currently logged in user
        String userName = principal.getName();

        User userProfile = userService.getByUserName(userName);

        return userProfile.getId();
    }


    public Profile create(Profile profile) {
        // creates profile for new users
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Principal principal, Profile profile){
        // allows currently signed in user to update profile information
        Profile userProfile = getProfileById(principal);

        userProfile.setFirstName(profile.getFirstName());
        userProfile.setLastName(profile.getLastName());
        userProfile.setPhone(profile.getPhone());
        userProfile.setEmail(profile.getEmail());
        userProfile.setAddress(profile.getAddress());
        userProfile.setCity(profile.getCity());
        userProfile.setState(profile.getState());
        userProfile.setZip(profile.getZip());

        // saves profile changes to database
        return profileRepository.save(userProfile);
    }

    public void deleteProfile(int userId){
        profileRepository.deleteById(userId);
    }
}
