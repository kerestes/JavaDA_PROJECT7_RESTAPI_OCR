package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
@Sql("/banco_scripts/rating.sql")
public class RatingServiceTest {
    @Autowired
    private RatingService ratingService;

    @Test
    public void ratingIT() {

        // Find All Test

        List<Rating> ratings = ratingService.findAll();

        Rating rating1 = ratings.get(0);
        Rating rating2 = ratings.get(1);

        if (rating1.getId() != 1) {
            rating1 = ratings.get(1);
            rating2 = ratings.get(0);
        }

        Assertions.assertEquals("Moodys Test", rating1.getMoodysRating());
        Assertions.assertEquals("Sand P Test", rating1.getSandPRating());
        Assertions.assertEquals("Fitch Test", rating1.getFitchRating());
        Assertions.assertEquals(3, rating1.getOrderNumber());
        Assertions.assertEquals("Moodys Test 2", rating2.getMoodysRating());
        Assertions.assertEquals("Sand P Test 2", rating2.getSandPRating());
        Assertions.assertEquals("Fitch Test 2", rating2.getFitchRating());
        Assertions.assertEquals(6, rating2.getOrderNumber());

        Assertions.assertTrue(ratings.size() == 2);

        //Save Test

        Rating rating = new Rating();
        rating.setMoodysRating("new Moodys");
        rating.setSandPRating("new Sand P");
        rating.setFitchRating("new Fitch");
        rating.setOrderNumber(9);

        Rating ratingResponse = ratingService.save(rating);
        Assertions.assertNotNull(ratingResponse.getId());
        Assertions.assertEquals(rating.getSandPRating(), ratingResponse.getSandPRating());

        //FindById Test

        Optional<Rating> optionalRating = ratingService.findById(3);

        Assertions.assertTrue(optionalRating.isPresent());
        Assertions.assertEquals("new Fitch", optionalRating.get().getFitchRating());
        Assertions.assertEquals(9, optionalRating.get().getOrderNumber());

        // Delete Test

        ratingService.deleteById(3);

        optionalRating = ratingService.findById(3);

        Assertions.assertTrue(optionalRating.isEmpty());
    }
}
