
package com.skillexchange.dto;

import com.skillexchange.model.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    // ✅ Add Getters (important for JSON serialization)
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
