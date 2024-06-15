package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    // Метод загрузки пользователя по имени пользователя
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    // Сохранение пользователя и установка роли по умолчанию
    @Transactional
    public boolean saveUser(User user) {
        if (userRepository.findUserByName(user.getName()).isPresent()) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // Сохранение пользователя с указанными ролями
    @Transactional
    public boolean saveUser(User user, List<String> rolesFromView) {

        Set<Role> roles = roleService.findByRoleNameIn(rolesFromView);
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // Сохранение пользователя с переданными ролями
    @Transactional
    public boolean saveUser(User user, Set<Role> roles) {
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // Получение списка всех пользователей
    @Transactional
    public List<User> getListAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + id + " не найден"));
    }

    // Обновление пользователя
    @Transactional
    public void updateUser(User user, List<String> rolesFromView) {
        User userFromDB = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + user.getId() + " не найден"));

        if (!user.getPassword().equals(userFromDB.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        user.setRoles(rolesFromView.stream()
                .map(roleService::findRoleByRoleName)
                .collect(Collectors.toSet()));
        userRepository.save(user);
    }

    // Удаление пользователя по ID
    @Transactional
    public void deleteUser(int id) {
        userRepository.findById(id).ifPresentOrElse(
                user -> userRepository.deleteById(id),
                () -> {
                    throw new EntityNotFoundException("Пользователь с id " + id + " не найден");
                }
        );
    }
}
