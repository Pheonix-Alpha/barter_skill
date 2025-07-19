package com.skillexchange.service;

import com.skillexchange.dto.RatingRequest;
import com.skillexchange.model.Rating;
import com.skillexchange.model.User;
import com.skillexchange.repository.RatingRepository;
import com.skillexchange.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepo;
    private final UserRepository userRepo;

    public RatingService(RatingRepository ratingRepo, UserRepository userRepo) {
        this.ratingRepo = ratingRepo;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).orElseThrow();
    }

    public Rating rateUser(RatingRequest req) {
        User rater = getCurrentUser();
        User rated = userRepo.findById(req.getRatedUserId()).orElseThrow();

        Rating rating = Rating.builder()
                .rater(rater)
                .rated(rated)
                .stars(req.getStars())
                .feedback(req.getFeedback())
                .build();

        rated.setRatingSum(rated.getRatingSum() + req.getStars());
        rated.setTotalRatings(rated.getTotalRatings() + 1);
        rated.setAverageRating((double) rated.getRatingSum() / rated.getTotalRatings());

        userRepo.save(rated);
        return ratingRepo.save(rating);
    }

    public List<Rating> getRatingsOfUser(Long userId) {
        User rated = userRepo.findById(userId).orElseThrow();
        return ratingRepo.findByRated(rated);
    }
}
