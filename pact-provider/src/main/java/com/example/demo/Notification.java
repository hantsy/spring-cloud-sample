package com.example.demo;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String body;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Type type = Type.MESSAGE;

    static enum Type {
        WARNING, ERROR, MESSAGE;
    }
}
