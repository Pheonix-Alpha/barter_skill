package com.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "reviewed_user_id")
    private User reviewedUser;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;
}
