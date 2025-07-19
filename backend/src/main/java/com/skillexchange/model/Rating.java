package com.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User rater;

    @ManyToOne
    private User rated;

    private int stars; // 1 to 5
    private String feedback;
}
