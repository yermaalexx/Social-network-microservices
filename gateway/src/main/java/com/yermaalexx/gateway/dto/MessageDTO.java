package com.yermaalexx.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageDTO {
    private UUID messageId;
    private UUID senderId;
    private UUID receiverId;
    private String content;
    private LocalDateTime sentAt;
    private String formattedSentAt;

    public void setFormattedSentAt() {
        if (sentAt == null)
            formattedSentAt = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        formattedSentAt = sentAt.format(formatter);
    }
}
