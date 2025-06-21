package com.yermaalexx.usersservice.service;

import com.yermaalexx.usersservice.model.User;
import com.yermaalexx.usersservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#id")
    public User getUser(UUID id) {
        log.info("Fetching user from DB for id = {}", id);
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public List<UUID> findUsersSortedByInterestMatch(UUID userId) {
        return userRepository.findUsersSortedByInterestMatch(userId)
                .stream()
                .map(UUID::fromString)
                .toList();
    }

    public List<String> getInterests(UUID userId) {
        return userRepository.findInterestsByUserId(userId);
    }

}