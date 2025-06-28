package com.yermaalexx.chatservice.service;

import com.yermaalexx.chatservice.config.AppConfig;
import com.yermaalexx.chatservice.dto.MessageDTO;
import com.yermaalexx.chatservice.model.Message;
import com.yermaalexx.chatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final NewMessageService newMessageService;
    private final AppConfig appConfig;

    @Transactional
    public List<MessageDTO> getChat(UUID userA, UUID userB) {
        log.debug("getChat called with userA={}, userB={}", userA, userB);
        List<Message> allMessages = messageRepository.findChatHistory(userA, userB);
        while (allMessages.size() > appConfig.getMaxMessagesInChat()) {
            deleteMessage(allMessages.remove(0).getId());
        }
        log.info("Found {} messages between users {} and {}", allMessages.size(), userA, userB);
        return allMessages.stream().map(MessageDTO::from).toList();
    }

    @Transactional
    public void deleteChat(UUID userA, UUID userB) {
        log.info("Deleting messages between {} and {}", userA, userB);
        messageRepository.deleteChatHistory(userA, userB);
        log.info("Messages successfully deleted between {} and {}", userA, userB);
    }

    @Transactional
    public void deleteMessage(UUID messageId) {
        log.info("Deleting message with messageId={}", messageId);
        messageRepository.deleteById(messageId);
    }

    @Transactional
    public void sendMessage(MessageDTO messageDTO) {
        log.debug("sendMessage called: senderId={}, receiverId={}", messageDTO.getSenderId(), messageDTO.getReceiverId());
        Message message = messageRepository.save(Message.from(messageDTO));
        log.info("Message from {} to {} saved with id={}", message.getSenderId(), message.getReceiverId(), message.getId());
        newMessageService.saveNewMessage(message.getReceiverId(), message.getSenderId());
        log.info("New message notification saved for user {} from {}", message.getReceiverId(), message.getSenderId());
    }
}
