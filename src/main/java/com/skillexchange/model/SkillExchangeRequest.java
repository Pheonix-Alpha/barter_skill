package com.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User target;

    @ManyToOne
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
