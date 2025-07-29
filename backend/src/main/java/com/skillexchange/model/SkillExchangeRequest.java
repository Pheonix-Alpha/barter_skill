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
    private User target; // ✅ <--- You need this field!

    @ManyToOne
    @JoinColumn(name = "wanted_skill_id", nullable = true)
       private Skill wantedSkill; // This is the "wanted" skill

    @ManyToOne
    @JoinColumn(name = "offered_skill_id", nullable = true)
    private Skill offeredSkill;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

     @Enumerated(EnumType.STRING)
    private SkillType type; 
}
