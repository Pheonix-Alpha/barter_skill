package com.skillexchange.dto;

import java.util.List;

public class UserProfileDTO {
    private String username;
    private String email;
    private String profilePicture;
    private List<String> skills;
    private List<String> reviews;
    private List<String> comments;

    public UserProfileDTO(String username, String email, String profilePicture,
                          List<String> skills, List<String> reviews, List<String> comments) {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.skills = skills;
        this.reviews = reviews;
        this.comments = comments;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getProfilePicture() { return profilePicture; }
    public List<String> getSkills() { return skills; }
    public List<String> getReviews() { return reviews; }
    public List<String> getComments() { return comments; }
}
