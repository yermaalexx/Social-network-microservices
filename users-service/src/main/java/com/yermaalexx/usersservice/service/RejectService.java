package com.yermaalexx.usersservice.service;

import com.yermaalexx.usersservice.model.Reject;
import com.yermaalexx.usersservice.repository.RejectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RejectService {

    private final RejectRepository rejectRepository;

    @Transactional
    public void reject(UUID userId, UUID rejectedUserId) {
        log.info("Rejecting user: userId={} rejects rejectedUserId={}", userId, rejectedUserId);
        rejectRepository.save(new Reject(null, userId, rejectedUserId));
        rejectRepository.save(new Reject(null, rejectedUserId, userId));
        log.debug("Reject records saved for both directions: {} ⇄ {}", userId, rejectedUserId);
    }

    public List<UUID> findAllRejectedUsers(UUID userId) {
        log.debug("Finding all rejected users for userId={}", userId);
        List<UUID> rejected = rejectRepository.findAllByUserId(userId)
                .stream()
                .map(Reject::getRejectedUserId)
                .toList();
        log.info("Found {} rejected user(s) for userId={}", rejected.size(), userId);
        return rejected;
    }
}
