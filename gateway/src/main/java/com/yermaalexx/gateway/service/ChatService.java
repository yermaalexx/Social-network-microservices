package com.yermaalexx.gateway.service;

import com.yermaalexx.gateway.dto.MessageDTO;
import com.yermaalexx.gateway.feignclient.ChatClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatClient chatClient;

    public List<MessageDTO> getChat(UUID userA, UUID userB) {
        log.debug("getAllMessages called with userA={}, userB={}", userA, userB);
        return chatClient.getAllMessages(userA, userB);
    }

    @Transactional
    public void deleteChat(UUID userA, UUID userB) {
        log.info("Deleting chat between userA={} and userB={}", userA, userB);
        chatClient.deleteAllMessages(userA, userB);
        log.info("Chat successfully deleted between {} and {}", userA, userB);

    }

    @Transactional
    public void sendMessage(UUID senderId, UUID receiverId, String content) {
        log.debug("sendMessage called: senderId={}, receiverId={}", senderId, receiverId);
        MessageDTO message = new MessageDTO(null, senderId, receiverId, content, LocalDateTime.now(), null);
        chatClient.sendMessage(message);
        log.info("Message from {} to {} saved", senderId, receiverId);
    }

    public void deleteNewMessageRecord(UUID userId, UUID whoSent) {
        log.info("Delete NewMessage record from {} to {}", whoSent, userId);
        chatClient.deleteNewMessageRecord(userId, whoSent);
    }

    public List<UUID> findNewMessageRecordsByUserId(UUID userId) {
        log.info("Find all NewMessage records for userId={}", userId);
        return chatClient.findNewMessageRecordsByUserId(userId);
    }

}
