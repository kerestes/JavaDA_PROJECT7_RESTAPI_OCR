package com.nnk.springboot.unit;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
public class RatingTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Test
    public void ratingListTest() throws Exception {

        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setFitchRating("Fitch test");
        rating1.setMoodysRating("Moody test");
        rating1.setSandPRating("Sand test");
        rating1.setOrderNumber(100);

        Rating rating2 = new Rating();
        rating2.setId(1);
        rating2.setFitchRating("Fitch test 2");
        rating2.setMoodysRating("Moody test 2");
        rating2.setSandPRating("Sand test 2");
        rating2.setOrderNumber(100);

        List<Rating> ratings =  new ArrayList<>(Arrays.asList(rating1, rating2));

        Mockito.when(ratingService.findAll()).thenReturn(ratings);

        MvcResult resultActions = mockMvc.perform(get("/rating/list")).andExpectAll(
                status().isOk(),
                model().size(3),
                model().attribute("title", "Rating")
        ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Fitch test"));
        Assertions.assertTrue(content.contains("Fitch test 2"));
        Assertions.assertTrue(content.contains("Moody test"));
        Assertions.assertTrue(content.contains("Moody test 2"));

        Mockito.verify(ratingService, Mockito.times(1)).findAll();
    }

    @Test
    public void ratingAddTest() throws Exception {

        mockMvc.perform(get("/rating/add")).andExpectAll(
                status().isOk(),
                model().size(2),
                model().attribute("title", "Rating - ADD")
        );
    }

    @Test
    public void ratingValidateErrorTest() throws Exception {

        Rating rating = new Rating();

        Mockito.when(ratingService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/rating/validate").flashAttr("rating", rating))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Moodys Rating is mandatory"));
        Assertions.assertTrue(content.contains("Sand Rating is mandatory"));
        Assertions.assertTrue(content.contains("Fitch Rating is mandatory"));
        Assertions.assertTrue(content.contains("Order Number Rating is mandatory"));

        Mockito.verify(ratingService, Mockito.times(0)).save(any());
    }

    @Test
    public void ratingValidateTest() throws Exception {

        Rating rating = new Rating();
        rating.setFitchRating("Fitch test");
        rating.setMoodysRating("Moody test");
        rating.setSandPRating("Sand test");
        rating.setOrderNumber(100);

        Mockito.when(ratingService.save(any())).thenReturn(null);

        mockMvc.perform(post("/rating/validate").flashAttr("rating", rating))
                .andExpect(
                        redirectedUrl("/rating/list")
                );

        Mockito.verify(ratingService, Mockito.times(1)).save(any());
    }

    @Test
    public void ratingUpdateErrorTest() throws Exception {

        Mockito.when(ratingService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/rating/update/{id}", 1))
                .andExpect(
                        redirectedUrl("/rating/list?error=true")
                );

        Mockito.verify(ratingService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void ratingUpdateTest() throws Exception {

        Rating rating = new Rating();
        rating.setId(1);
        rating.setFitchRating("Fitch test");
        rating.setMoodysRating("Moody test");
        rating.setSandPRating("Sand test");
        rating.setOrderNumber(100);

        Mockito.when(ratingService.findById(anyInt())).thenReturn(Optional.of(rating));

        mockMvc.perform(get("/rating/update/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        model().size(2),
                        model().attribute("title", "Rating - UPDATE")
                );

        Mockito.verify(ratingService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void ratingUpdateValidatorErrorTest() throws Exception {

        Rating rating = new Rating();
        rating.setId(1);

        Mockito.when(ratingService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/rating/update/{id}", 1).flashAttr("rating", rating))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Moodys Rating is mandatory"));
        Assertions.assertTrue(content.contains("Sand Rating is mandatory"));
        Assertions.assertTrue(content.contains("Fitch Rating is mandatory"));
        Assertions.assertTrue(content.contains("Order Number Rating is mandatory"));

        Mockito.verify(ratingService, Mockito.times(0)).save(any());
    }

    @Test
    public void ratingUpdateValidatorTest() throws Exception {

        Rating rating = new Rating();
        rating.setId(1);
        rating.setFitchRating("Fitch test");
        rating.setMoodysRating("Moody test");
        rating.setSandPRating("Sand test");
        rating.setOrderNumber(100);

        Mockito.when(ratingService.save(any())).thenReturn(null);

        mockMvc.perform(post("/rating/update/{id}", 1).flashAttr("rating", rating))
                .andExpect(
                        redirectedUrl("/rating/list")
                );

        Mockito.verify(ratingService, Mockito.times(1)).save(any());
    }

    @Test
    public void ratingDeleteTest() throws Exception {

        Mockito.doNothing().when(ratingService).deleteById(anyInt());

        mockMvc.perform(get("/rating/delete/{id}", 1)).andExpect(
                redirectedUrl("/rating/list")
        );

        Mockito.verify(ratingService, Mockito.times(1)).deleteById(anyInt());
    }
}
