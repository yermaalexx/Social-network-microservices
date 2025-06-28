package com.yermaalexx.gateway.service;

import com.yermaalexx.gateway.dto.UserDTO;
import com.yermaalexx.gateway.feignclient.UserClient;
import com.yermaalexx.gateway.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserLoginService userLoginService;
    private final ChatService chatService;
    private final UserClient userClient;

    @Transactional
    public User saveNewUser(User user) {
        log.info("Registering new user: name={}, birthYear={}, location={}", user.getName(), user.getBirthYear(), user.getLocation());

        UserDTO savedDto = userClient.createUser(UserDTO.from(user));
        user.setId(savedDto.getId());

        log.debug("User saved with id={}", savedDto.getId());

        userLoginService.saveLogin(user.getLogin(), user.getPassword(), savedDto.getId());
        log.info("Login credentials saved for userId={}", savedDto.getId());

        return user;
    }

    @Transactional
    public void updateUser(User userAfter, HttpSession session) {
        UUID id = userAfter.getId();
        log.info("Updating profile for {}, id = {}", userAfter.getName(), id);

        UserDTO userDto = userClient.getUserById(id);
        boolean interestsChanged = !userAfter.getMatchingInterests().equals(userDto.getInterests());

        userDto.setName(userAfter.getName());
        userDto.setBirthYear(userAfter.getBirthYear());
        userDto.setLocation(userAfter.getLocation());

        if (interestsChanged) {
            log.info("User interests changed for id = {}", id);
            userDto.setInterests(userAfter.getMatchingInterests());
            session.setAttribute("matchIds", null);
        }

        if (userAfter.getPhotoBase64() != null) {
            byte[] decodedPhoto = Base64.getDecoder().decode(userAfter.getPhotoBase64());
            log.info("Updating profile photo for id = {}, size = {} Kb", id, decodedPhoto.length/1024);
            userDto.setUserPhoto(decodedPhoto);
        } else {
            log.info("Deleting profile photo for id = {}", id);
            userDto.setUserPhoto(null);
        }

        userClient.updateUser(id, userDto);
        log.debug("User profile updated in DB for {}, id = {}", userDto.getName(), id);

    }

    public List<UUID>[] getUsersSortedByInterestMatch(UUID userId) {
        log.info("Getting sorted user matches for userId={}", userId);

        List<UUID> rejectedUsers = userClient.getAllRejectedUsers(userId);
        log.debug("{} rejected users for userId={}", rejectedUsers.size(), userId);

        List<UUID> usersWithNewMessages = chatService.findNewMessageRecordsByUserId(userId);
        log.debug("{} users with new messages for userId={}", usersWithNewMessages.size(), userId);

        List<UUID> usersSortedByInterestMatch = userClient.getUsersSortedByInterestMatch(userId)
                .stream()
                .filter(id -> !rejectedUsers.contains(id))
                .filter(id -> !usersWithNewMessages.contains(id))
                .toList();
        List<UUID> allSortedUsers = new ArrayList<>(usersWithNewMessages);
        allSortedUsers.addAll(usersSortedByInterestMatch);

        log.info("Matched users (sorted, with no rejects) found: {}", allSortedUsers.size());

        return new List[]{usersWithNewMessages, allSortedUsers};
    }

    public List<User> getMatchedUsers(List<UUID> ids, UUID userId) {
        log.info("Generating matched users for userId={} and {} matched IDs", userId, ids.size());

        List<String> interests = userClient.getInterests(userId);
        List<UserDTO> dtos = userClient.getUsersByIds(ids);

        return dtos.stream()
                .map(dto -> {
                    List<String> common = dto.getInterests().stream()
                            .filter(interests::contains)
                            .toList();
                    List<String> other = dto.getInterests().stream()
                            .filter(i -> !interests.contains(i))
                            .toList();
                    log.debug("For userId={} and matchedUserId={}, commonInterests={}, otherInterests={}",
                            userId, dto.getId(), common.size(), other.size());
                    return User.from(dto, common, other);
                })
                .toList();
    }

    public User getUserProfile(UUID userId) {
        log.info("Fetching profile for userId={}", userId);
        UserDTO dto = userClient.getUserById(userId);

        return User.from(dto, null, null);
    }

    public void reject(UUID userId, UUID rejectedUserId) {
        log.info("UserId={} rejects rejectedUserId={}", userId, rejectedUserId);
        userClient.reject(userId, rejectedUserId);

        chatService.deleteChat(userId, rejectedUserId);
        log.info("Deleted chat between {} and {}", userId, rejectedUserId);

        chatService.deleteNewMessageRecord(userId, rejectedUserId);
        chatService.deleteNewMessageRecord(rejectedUserId, userId);
        log.info("Deleted new message notifications between {} and {}", userId, rejectedUserId);
    }

}
