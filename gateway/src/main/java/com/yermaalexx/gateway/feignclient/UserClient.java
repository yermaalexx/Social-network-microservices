package com.yermaalexx.gateway.feignclient;

import com.yermaalexx.gateway.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "users-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable UUID id);

    @GetMapping("/users/{id}/interests")
    List<String> getInterests(@PathVariable UUID id);

    @PostMapping("/users")
    UserDTO createUser(@RequestBody UserDTO userDto);

    @PutMapping("/users/{id}")
    void updateUser(@PathVariable UUID id, @RequestBody UserDTO userDto);

    @PostMapping("/users/list")
    List<UserDTO> getUsersByIds(@RequestBody List<UUID> ids);

    @GetMapping("/users/{id}/matches")
    List<UUID> getUsersSortedByInterestMatch(@PathVariable UUID id);

}
