package ru.job4j.todo.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime created;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
