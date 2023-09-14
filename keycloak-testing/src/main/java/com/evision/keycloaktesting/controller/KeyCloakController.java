package com.evision.keycloaktesting.controller;

import com.evision.keycloaktesting.DTO.UserDTO;
import com.evision.keycloaktesting.service.KeyCloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user")
public class KeyCloakController {

    @Autowired
    KeyCloakService service;

    @PostMapping
    public String addUser(@RequestBody UserDTO userDTO){
        service.addUser(userDTO);
        return "User Added Successfully.";
    }

    @PostMapping("/assign-role/{username}/{rolename}")
    public String assignRoleToUser(@PathVariable String username, @PathVariable String rolename) {
        service.assignRoleToUserByUsername(username, rolename);
        return "Role assigned successfully.";
    }

}
