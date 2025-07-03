package com.skillexchange.controller;

import com.skillexchange.dto.RatingRequest;
import com.skillexchange.model.Rating;
import com.skillexchange.service.RatingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public Rating rateUser(@RequestBody RatingRequest req) {
        return ratingService.rateUser(req);
    }

    @GetMapping("/{userId}")
    public List<Rating> getUserRatings(@PathVariable Long userId) {
        return ratingService.getRatingsOfUser(userId);
    }
}
