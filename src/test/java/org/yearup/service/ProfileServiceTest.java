package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.repository.ProfileRepository;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getUserId_shouldReturnUserIdFromPrincipal() {
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");

        when(principal.getName()).thenReturn("testUser");
        when(userService.getByUserName("testUser")).thenReturn(user);

        int result = profileService.getUserId(principal);

        assertEquals(1, result);
    }

    @Test
    void getProfileById_shouldReturnMatchingProfile() {
        User user = new User();
        user.setId(2);
        user.setUsername("testUser");

        Profile profile1 = new Profile();
        profile1.setUserId(1);

        Profile profile2 = new Profile();
        profile2.setUserId(2);
        profile2.setFirstName("Blaine");

        when(principal.getName()).thenReturn("testUser");
        when(userService.getByUserName("testUser")).thenReturn(user);
        when(profileRepository.findAll()).thenReturn(List.of(profile1, profile2));

        Profile result = profileService.getProfileById(principal);

        assertNotNull(result);
        assertEquals(2, result.getUserId());
        assertEquals("Blaine", result.getFirstName());
    }

    @Test
    void create_shouldSaveProfile() {
        Profile profile = new Profile();
        profile.setUserId(1);
        profile.setFirstName("Blaine");

        when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.create(profile);

        assertEquals(profile, result);
        verify(profileRepository).save(profile);
    }

    @Test
    void updateProfile_shouldUpdateCurrentUsersProfile() {
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");

        Profile existingProfile = new Profile();
        existingProfile.setUserId(1);
        existingProfile.setFirstName("Old");
        existingProfile.setLastName("Name");

        Profile updatedInfo = new Profile();
        updatedInfo.setFirstName("Blaine");
        updatedInfo.setLastName("Bell");
        updatedInfo.setPhone("555-1234");
        updatedInfo.setEmail("blaine@example.com");
        updatedInfo.setAddress("123 Main St");
        updatedInfo.setCity("San Francisco");
        updatedInfo.setState("CA");
        updatedInfo.setZip("94105");

        when(principal.getName()).thenReturn("testUser");
        when(userService.getByUserName("testUser")).thenReturn(user);
        when(profileRepository.findAll()).thenReturn(List.of(existingProfile));
        when(profileRepository.save(existingProfile)).thenReturn(existingProfile);

        Profile result = profileService.updateProfile(principal, updatedInfo);

        assertEquals("Blaine", result.getFirstName());
        assertEquals("Bell", result.getLastName());
        assertEquals("555-1234", result.getPhone());
        assertEquals("blaine@example.com", result.getEmail());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("San Francisco", result.getCity());
        assertEquals("CA", result.getState());
        assertEquals("94105", result.getZip());

        verify(profileRepository).save(existingProfile);
    }

    @Test
    void deleteProfile_shouldDeleteByUserId() {
        int userId = 1;

        profileService.deleteProfile(userId);

        verify(profileRepository).deleteById(userId);
    }
}