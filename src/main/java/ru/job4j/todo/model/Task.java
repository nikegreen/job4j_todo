package ru.job4j.todo.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @ManyToMany
    @JoinTable(
            name = "tasks_categories",
            joinColumns = { @JoinColumn(name = "task_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Category> categories;

    /**
     * Функция возращает время с учётом часового пояса пользователя
     * создавшего задачу.
     * @return {@link java.lang.String} в формате: HH:mm yyyy-MM-dd
     */
    public String createdToString() {
        String timeZone = (getUser() == null
                || getUser().getZone() == null
                || "".equals(getUser().getZone()))
                ? TimeZone.getDefault().getID() : getUser().getZone();
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime zdtAtZoneId = getCreated().atZone(zoneId);
        return zdtAtZoneId.withZoneSameInstant(
                ZoneId.of(timeZone)
        ).format(DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd"));
    }
}
