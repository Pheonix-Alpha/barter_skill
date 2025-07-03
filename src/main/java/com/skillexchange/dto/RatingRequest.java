package com.skillexchange.dto;

import lombok.Data;

@Data
public class RatingRequest {
    private Long ratedUserId;
    private int stars;
    private String feedback;
}
