package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")

public class UserRESTController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getListAllUsers();
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.findUserById(id.intValue());
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @PatchMapping("users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        userService.updateUser(user, new ArrayList<>());
        return userService.findUserById(id.intValue());
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id.intValue());
    }
}
