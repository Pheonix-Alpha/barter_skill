package com.skillexchange.dto;

import com.skillexchange.model.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;

    private boolean isFriend;
    private boolean requestSent;
    private boolean requestReceived;

    // ✅ Constructor with relationship flags
    public UserDTO(User user, User currentUser) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();

        this.isFriend = currentUser.getFriends().contains(user);
        this.requestSent = currentUser.getSentRequests().contains(user);
        this.requestReceived = currentUser.getReceivedRequests().contains(user);
    }

    // ✅ Getters for serialization
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public boolean isRequestReceived() {
        return requestReceived;
    }
}
