package com.yermaalexx.usersservice.repository;

import com.yermaalexx.usersservice.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<UserPhoto, UUID> {

}
