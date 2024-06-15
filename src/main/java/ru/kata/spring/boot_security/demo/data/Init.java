package ru.kata.spring.boot_security.demo.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Метод, выполняемый после создания объекта Init
    @PostConstruct
    public void init() {
        // Создание ролей
        Role roleUser = new Role(1, "ROLE_USER");
        Role roleAdmin = new Role(2, "ROLE_ADMIN");

        // Добавление ролей в базу данных
        roleService.addRole(roleUser);
        roleService.addRole(roleAdmin);

        // Создание и сохранение пользователей
        userService.saveUser(
                new User("user", "user", "terminator", 27, "user@mail.ru"),
                new HashSet<Role>(Set.of(roleAdmin, roleUser))
        );
        userService.saveUser(
                new User("user2", "user2", "tractor", 28, "user2@mail.ru")
        );
    }
}