package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.Profile;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer>
{
    List<Profile> getProfilesByUserId(int userId);

    List<Profile> getProfileByUserId(int userId);
}
