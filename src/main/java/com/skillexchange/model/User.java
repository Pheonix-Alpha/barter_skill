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
    private Role role;

    @Column(length = 500)
private String bio;

private Double averageRating = 0.0;
private Integer totalRatings = 0;
private Integer ratingSum = 0;

}
