package com.skillexchange.dto;

import java.util.List;

public class UserProfileDTO {

    private String username;
    private String email;
    private String bio;
    private String profilePicture;

    private List<String> offeringSkills;
    private List<String> wantingSkills;

    private List<String> reviews;  // optional for now
    private List<String> comments; // optional for now

    public UserProfileDTO(
            String username,
            String email,
            String bio,
            String profilePicture,
            List<String> offeringSkills,
            List<String> wantingSkills,
            List<String> reviews,
            List<String> comments
    ) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.offeringSkills = offeringSkills;
        this.wantingSkills = wantingSkills;
        this.reviews = reviews;
        this.comments = comments;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public String getProfilePicture() { return profilePicture; }
    public List<String> getOfferingSkills() { return offeringSkills; }
    public List<String> getWantingSkills() { return wantingSkills; }
    public List<String> getReviews() { return reviews; }
    public List<String> getComments() { return comments; }
}
