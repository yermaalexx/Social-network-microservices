package com.yermaalexx.gateway.feignclient;

import com.yermaalexx.gateway.dto.MessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "chat-service")
public interface ChatClient {

    @GetMapping("/chat/{userA}/{userB}")
    List<MessageDTO> getAllMessages(@PathVariable UUID userA, @PathVariable UUID userB);

    @GetMapping("/chat/newMessage/{userId}")
    List<UUID> findNewMessageRecordsByUserId(@PathVariable UUID userId);

    @DeleteMapping("/chat/{userA}/{userB}")
    void deleteAllMessages(@PathVariable UUID userA, @PathVariable UUID userB);

    @DeleteMapping("/chat/{messageId}")
    void deleteMessageById(@PathVariable UUID messageId);

    @DeleteMapping("/chat/newMessage/{userId}/{whoSent}")
    void deleteNewMessageRecord(@PathVariable UUID userId, @PathVariable UUID whoSent);

    @PostMapping("/chat/message")
    void sendMessage(@RequestBody MessageDTO message);

}
