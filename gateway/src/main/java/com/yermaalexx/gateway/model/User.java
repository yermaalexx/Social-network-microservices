package com.yermaalexx.gateway.model;

import com.yermaalexx.gateway.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String login;
    private String password;
    private int birthYear;
    private String location;
    private LocalDate registrationDate;
    private String photoBase64;
    private List<String> matchingInterests;
    private List<String> otherInterests;

    public static User from(UserDTO dto, List<String> common, List<String> other) {
        if (dto == null)
            return null;
        String photoBase64 = (dto.getUserPhoto() == null) ? null : Base64.getEncoder().encodeToString(dto.getUserPhoto());
        if (common == null)
            common = dto.getInterests();
        return new User(
                dto.getId(),
                dto.getName(),
                null,
                null,
                dto.getBirthYear(),
                dto.getLocation(),
                dto.getRegistrationDate(),
                photoBase64,
                common,
                other);
    }
}
