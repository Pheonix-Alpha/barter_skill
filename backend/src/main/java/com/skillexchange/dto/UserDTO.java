package com.skillexchange.dto;

import com.skillexchange.model.SkillType;
import com.skillexchange.model.User;
import com.skillexchange.model.UserSkill;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
        private String bio;
    private String profilePicture;


    private boolean isFriend;
    private boolean requestSent;
    private boolean requestReceived;

    private List<String> offeringSkills;
    private List<String> wantingSkills;

     private List<Long> offeringSkillIds;
    private List<Long> wantingSkillIds;

    // ✅ Constructor with relationship flags + skill sets
    public UserDTO(User user, User currentUser) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
           this.bio = user.getBio(); // ✅ bio added
       this.profilePicture = user.getProfilePicture();


        this.isFriend = currentUser.getFriends().contains(user);
        this.requestSent = currentUser.getSentRequests().contains(user);
        this.requestReceived = currentUser.getReceivedRequests().contains(user);

        this.offeringSkills = user.getUserSkills().stream()
                .filter(us -> us.getType() == SkillType.OFFERED)
                .map(us -> us.getSkill().getName())
                .collect(Collectors.toList());

        this.wantingSkills = user.getUserSkills().stream()
                .filter(us -> us.getType() == SkillType.WANTED)
                .map(us -> us.getSkill().getName())
                .collect(Collectors.toList());

                   this.offeringSkillIds = user.getUserSkills().stream()
                .filter(us -> us.getType() == SkillType.OFFERED)
                .map(us -> us.getSkill().getId())
                .collect(Collectors.toList());

        this.wantingSkillIds = user.getUserSkills().stream()
                .filter(us -> us.getType() == SkillType.WANTED)
                .map(us -> us.getSkill().getId())
                .collect(Collectors.toList());
    }

     public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public String getProfilePicture() { return profilePicture; }

    public boolean isFriend() { return isFriend; }
    public boolean isRequestSent() { return requestSent; }
    public boolean isRequestReceived() { return requestReceived; }

    public List<String> getOfferingSkills() { return offeringSkills; }
    public List<String> getWantingSkills() { return wantingSkills; }

     public List<Long> getOfferingSkillIds() { return offeringSkillIds; }
    public List<Long> getWantingSkillIds() { return wantingSkillIds; }

}
