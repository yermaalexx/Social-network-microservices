package com.yermaalexx.usersservice.dto;

import com.yermaalexx.usersservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
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

    public static UserDTO from(User user, byte[] photo) {
        if (user == null)
            return null;
        return new UserDTO(user.getId(),
                user.getName(),
                user.getBirthYear(),
                user.getLocation(),
                user.getRegistrationDate(),
                photo,
                user.getInterests());
    }
}
