package com.skillexchange.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.skillexchange.model.Skill;

public class ProfileUpdateRequest {

    private String username;
    private String bio;
     private String profilePicture;
    private List<String> offeringSkills;
    private List<String> wantingSkills;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getProfilePicture() {
        return profilePicture;
    }

     public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<String> getOfferingSkills() {
        return offeringSkills;
    }

    public void setOfferingSkills(List<String> offeringSkills) {
        this.offeringSkills = offeringSkills;
    }

    public List<String> getWantingSkills() {
        return wantingSkills;
    }

    public void setWantingSkills(List<String> wantingSkills) {
        this.wantingSkills = wantingSkills;
    }

    // Converts string skill names to Skill entities
    public List<Skill> getOfferingSkillsAsEntities() {
        return offeringSkills.stream().map(name -> {
            Skill skill = new Skill();
            skill.setName(name);
            return skill;
        }).collect(Collectors.toList());
    }

    public List<Skill> getWantingSkillsAsEntities() {
        return wantingSkills.stream().map(name -> {
            Skill skill = new Skill();
            skill.setName(name);
            return skill;
        }).collect(Collectors.toList());
    }
}
