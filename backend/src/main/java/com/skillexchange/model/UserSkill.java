package com.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private SkillType type;

    // ✅ equals and hashCode only use `id`
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSkill)) return false;
        UserSkill that = (UserSkill) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31; // simple constant hashCode to prevent accidental use of uninitialized ID
    }
}
