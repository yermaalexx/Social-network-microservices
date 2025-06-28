package com.yermaalexx.usersservice.controller;

import com.yermaalexx.usersservice.dto.UserDTO;
import com.yermaalexx.usersservice.model.User;
import com.yermaalexx.usersservice.model.UserPhoto;
import com.yermaalexx.usersservice.service.PhotoService;
import com.yermaalexx.usersservice.service.RejectService;
import com.yermaalexx.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PhotoService photoService;
    private final RejectService rejectService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        log.info("Get info and photo for user with id = {}", id);
        User user = userService.getUser(id);
        UserPhoto userPhoto = photoService.getPhoto(id);
        byte[] photo = (userPhoto != null) ? userPhoto.getUserPhoto() : null;

        return ResponseEntity.ok(UserDTO.from(user, photo));
    }

    @GetMapping("/{id}/interests")
    public ResponseEntity<List<String>> getInterests(@PathVariable UUID id) {
        log.info("Get interests for user with id = {}", id);
        List<String> interests = userService.getInterests(id);

        return ResponseEntity.ok(interests);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        log.info("Creating user {}", dto.getName());
        User savedUser = userService.saveUser(new User(null,
                dto.getName(),
                dto.getBirthYear(),
                dto.getLocation(),
                LocalDate.now(),
                dto.getInterests()));
        if (dto.getUserPhoto() != null)
            photoService.savePhoto(new UserPhoto(savedUser.getId(), dto.getUserPhoto()));
        dto.setId(savedUser.getId());
        dto.setRegistrationDate(savedUser.getRegistrationDate());
        log.info("User {} successfully created with id = {}", dto.getName(), dto.getId());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserDTO dto) {
        log.info("Updating user {} with id = {}", dto.getName(), id);
        userService.updateUser(User.from(dto));
        photoService.deletePhoto(id);
        if (dto.getUserPhoto() != null)
            photoService.savePhoto(new UserPhoto(id, dto.getUserPhoto()));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/list")
    public ResponseEntity<List<UserDTO>> getUsersByIds(@RequestBody List<UUID> ids) {
        log.info("Get list of {} dtos", ids.size());
        List<UserDTO> dtos = ids.stream()
                .map(id -> {
                    User user = userService.getUser(id);
                    UserPhoto userPhoto = photoService.getPhoto(id);
                    byte[] photo = (userPhoto != null) ? userPhoto.getUserPhoto() : null;
                    return UserDTO.from(user, photo);
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<UUID>> getUsersSortedByInterestMatch(@PathVariable UUID id) {
        List<UUID> list = userService.findUsersSortedByInterestMatch(id);
        log.info("Found {} matches for user with id = {}", list.size(), id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/reject/{userId}")
    public ResponseEntity<List<UUID>> getAllRejectedUsers(@PathVariable UUID userId) {
        List<UUID> list = rejectService.findAllRejectedUsers(userId);
        log.info("Found {} rejects for user with id = {}", list.size(), userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/reject/{userId}/{rejectedUserId}")
    public ResponseEntity<Void> reject(@PathVariable UUID userId,
                                       @PathVariable UUID rejectedUserId) {
        log.info("User {} rejects user {}", userId, rejectedUserId);
        rejectService.reject(userId, rejectedUserId);
        return ResponseEntity.ok().build();
    }

}
