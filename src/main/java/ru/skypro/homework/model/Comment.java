package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @ManyToOne
    private User author;
    private Integer createdAt;
    private String text;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(pk, comment.pk) && Objects.equals(author, comment.author) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(text, comment.text);
    }
    @Override
    public int hashCode() {
        return Objects.hash(author, createdAt, text);
    }
    @Override
    public String toString() {
        return "Comment{" +
                "pk=" + pk +
                ", author=" + author +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                '}';
    }
}