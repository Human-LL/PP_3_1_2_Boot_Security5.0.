package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    // Метод для добавления контроллеров представлений
    public void addViewControllers(ViewControllerRegistry registry) {
        // Добавление контроллера для URL "/user", который будет использовать представление "user"
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/login").setViewName("login");
    }
}