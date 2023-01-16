package ru.job4j.todo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import javax.persistence.*;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "todo_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;
    private String name;
    private String login;
    private String password;
}
