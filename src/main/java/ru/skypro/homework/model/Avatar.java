package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    @JsonIgnore
    private byte[] data;
    @OneToOne
    @JsonIgnore
    private User user;
}
