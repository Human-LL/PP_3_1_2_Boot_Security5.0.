package ru.kata.spring.boot_security.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User implements UserDetails {

    // Первичный ключ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Имя пользователя
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 10, message = "Длина имени должна быть от 2 до 10 символов")
    @Column(name = "name")
    private String name;

    // Пароль пользователя
    @NotEmpty(message = "Поле пароля не должно быть пустым")
    @Column(name = "password")
    private String password;

    // Фамилия пользователя
    @NotEmpty(message = "Пожалуйста, укажите фамилию")
    @Column(name = "surname")
    private String surname;

    // Возраст пользователя
    @Min(value = 0, message = "Возраст должен быть больше 0")
    @Column(name = "age")
    private int age;

    // Email пользователя
    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Email не должен быть пустым")
    @Email
    private String email;

    // Роли пользователя
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // Конструктор без параметров
    public User() {
    }

    // Конструктор с основными полями
    public User(String name, String password, String surname, int age) {
        this.name = name;
        this.password = password;
        this.surname = surname;
        this.age = age;
    }

    // Конструктор со всеми полями, включая email
    public User(String name, String password, String surname, int age, String email) {
        this.name = name;
        this.password = password;
        this.surname = surname;
        this.age = age;
        this.email = email;
    }

    // Методы интерфейса UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Метод toString для представления объекта User в виде строки
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}