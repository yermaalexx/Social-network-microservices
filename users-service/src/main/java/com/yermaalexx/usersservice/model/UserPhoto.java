package com.yermaalexx.usersservice.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoto {
    @Id
    private UUID userId;

    @Basic
    @Column(name = "user_photo", columnDefinition = "BYTEA")
    private byte[] userPhoto;

}
