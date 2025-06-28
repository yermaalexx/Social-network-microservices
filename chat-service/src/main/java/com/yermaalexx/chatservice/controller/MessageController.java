package com.yermaalexx.chatservice.controller;

import com.yermaalexx.chatservice.dto.MessageDTO;
import com.yermaalexx.chatservice.service.MessageService;
import com.yermaalexx.chatservice.service.NewMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;
    private final NewMessageService newMessageService;

    @GetMapping("/{userA}/{userB}")
    public ResponseEntity<List<MessageDTO>> getAllMessages(@PathVariable UUID userA,
                                                           @PathVariable UUID userB) {
        log.info("Get all messages between users with ids {} and {}", userA, userB);
        return ResponseEntity.ok(messageService.getChat(userA, userB));
    }

    @GetMapping("/newMessage/{userId}")
    public ResponseEntity<List<UUID>> findNewMessageRecordsByUserId(@PathVariable UUID userId) {
        log.info("Find NewMessage notifications for userId={}", userId);
        return ResponseEntity.ok(newMessageService.findAllUsersWithNewMessagesByUserId(userId));
    }

    @DeleteMapping("/{userA}/{userB}")
    public ResponseEntity<Void> deleteAllMessages(@PathVariable UUID userA,
                                                  @PathVariable UUID userB) {
        log.info("Delete all messages between users with ids {} and {}", userA, userB);
        messageService.deleteChat(userA, userB);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable UUID messageId) {
        log.info("Delete message with id {}", messageId);
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/newMessage/{userId}/{whoSent}")
    public ResponseEntity<Void> deleteNewMessageRecord(@PathVariable UUID userId,
                                                       @PathVariable UUID whoSent) {
        log.info("Delete NewMessage notification for userId={} from sender={}", userId, whoSent);
        newMessageService.deleteNewMessage(userId, whoSent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageDTO message) {
        log.info("Send message from {} to {}", message.getSenderId(), message.getReceiverId());
        messageService.sendMessage(message);
        return ResponseEntity.ok().build();
    }



}
