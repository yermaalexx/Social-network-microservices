package com.yermaalexx.gateway.dto;

import com.yermaalexx.gateway.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String name;
    private int birthYear;
    private String location;
    private LocalDate registrationDate;
    private byte[] userPhoto;
    private List<String> interests;

    public static UserDTO from(User user) {
        if (user == null)
            return null;
        byte[] userPhoto = (user.getPhotoBase64() == null) ? null : Base64.getDecoder().decode(user.getPhotoBase64());
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getBirthYear(),
                user.getLocation(),
                user.getRegistrationDate(),
                userPhoto,
                user.getMatchingInterests());
    }
}
