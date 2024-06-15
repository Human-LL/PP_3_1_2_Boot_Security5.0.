package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRESTController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AdminRESTController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    // Отображение всех пользователей
    @GetMapping()
    public ModelAndView showAllUsers(Model model, @AuthenticationPrincipal User user) {
        // Получение списка ролей и всех пользователей для отображения
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.getListAllUsers());
        model.addAttribute("currentUser", user);
        model.addAttribute("userEmpty", new User());
        return new ModelAndView("all_users");
    }

    // Создание нового пользователя
    @PostMapping("/create")
    public ModelAndView saveNewUser(@ModelAttribute("user") User user,
                                    @RequestParam(value = "rolesForController", required = false) List<String> rolesFromView) {
        userService.saveUser(user, rolesFromView);
        return new ModelAndView("redirect:/admin");
    }

    // Обновление существующего пользователя
    @PatchMapping(value = "/update/{id}")
    public ModelAndView saveUpdateUser(@ModelAttribute("user") User user,
                                       @RequestParam(value = "rolesForController", required = false) List<String> rolesFromView) {

        userService.updateUser(user, rolesFromView);
        return new ModelAndView("redirect:/admin");
    }

    // Удаление пользователя по ID
    @PostMapping("/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int id,
                                   @ModelAttribute("user") User user) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
    }
}