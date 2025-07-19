package com.skillexchange.repository;

import com.skillexchange.model.Rating;
import com.skillexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRated(User rated);
}
