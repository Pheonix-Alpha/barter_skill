package com.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.LEARNER; // or Role.USER, as per your app's default

    @Column(length = 500)
    private String bio;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer totalRatings = 0;

    @Builder.Default
    private Integer ratingSum = 0;

    @Builder.Default
    private boolean isActive = true;
}
