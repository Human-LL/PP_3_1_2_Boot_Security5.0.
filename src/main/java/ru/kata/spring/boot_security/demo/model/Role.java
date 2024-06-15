package ru.kata.spring.boot_security.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "role_name")
    private String roleName;

    // Множество пользователей, у которых установлена данная роль
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
    }

    public Role(int id, String roleName) {
        Id = id;
        this.roleName = roleName;
    }

    // Возвращает имя роли без префикса "ROLE_"
    public String getRoleNameWithoutRole() {
        return roleName.substring(5);
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "Id=" + Id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
