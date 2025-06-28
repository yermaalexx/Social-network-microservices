package com.yermaalexx.chatservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    /* Максимальна кількість повідомлень в чаті. Якщо більше - то найстаріше повідомлення видаляється */
    private int maxMessagesInChat;
}
