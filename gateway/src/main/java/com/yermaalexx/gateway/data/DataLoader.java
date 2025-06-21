package com.yermaalexx.gateway.data;

import com.yermaalexx.gateway.model.User;
import com.yermaalexx.gateway.repository.LoginRepository;
import com.yermaalexx.gateway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Configuration
@Slf4j
public class DataLoader {

    @Bean
    public CommandLineRunner dataLoaderUsers(UserService userService,
                                             LoginRepository loginRepository) {
        return (args) -> {
            if(loginRepository.count() == 0) {
                byte[] photo = null;
                try {
                    ClassPathResource imgFile = new ClassPathResource("static/images/data/woman3.jpg");
                    photo = FileCopyUtils.copyToByteArray(imgFile.getInputStream());
                } catch (IOException e) {
                    log.warn("Cannot load photo in DataLoader");
                }
                String photoBase64 = (photo != null) ? Base64.getEncoder().encodeToString(photo) : null;
                userService.saveNewUser(new User(null,"Alexx",
                        "alexx","pass",1997,"Dnipro",LocalDate.now(),photoBase64,
                        List.of("Movies SciFi","Movies Drama","Movies Comedy"),null));
                photo = null;
                try {
                    ClassPathResource imgFile = new ClassPathResource("static/images/data/man3.jpg");
                    photo = FileCopyUtils.copyToByteArray(imgFile.getInputStream());
                } catch (IOException e) {
                    log.warn("Cannot load photo in DataLoader");
                }
                photoBase64 = (photo != null) ? Base64.getEncoder().encodeToString(photo) : null;
                userService.saveNewUser(new User(null,"Bill",
                        "bill","pass",2000,"NY",LocalDate.now(),photoBase64,
                        List.of("Movies SciFi","Movies Drama","Movies Comedy","Books SciFi","Books Thriller","Books Classic"), null));
                photo = null;
                try {
                    ClassPathResource imgFile = new ClassPathResource("static/images/data/man2.jpg");
                    photo = FileCopyUtils.copyToByteArray(imgFile.getInputStream());
                } catch (IOException e) {
                    log.warn("Cannot load photo in DataLoader");
                }
                photoBase64 = (photo != null) ? Base64.getEncoder().encodeToString(photo) : null;
                userService.saveNewUser(new User(null,"Dave",
                        "dave","pass",2005,"LA",LocalDate.now(),photoBase64,
                        List.of("Movies SciFi","Movies Action","Movies Documentaries","Books Horror","Rock"), null));
                userService.saveNewUser(new User(null,"Petya",
                        "petya","pass",1998,"Dnipro",LocalDate.now(),null,
                        List.of("Movies SciFi","Movies Drama","Books SciFi","Books Thriller","Hip-Hop","Cycling & Running"), null));
            }
        };
    }
}
