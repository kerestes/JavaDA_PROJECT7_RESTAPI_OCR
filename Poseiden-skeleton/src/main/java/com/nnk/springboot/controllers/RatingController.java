package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class RatingController {

    private final String title = "Rating";
    @Autowired
    private RatingService ratingService;

    @RequestMapping("/rating/list")
    public ModelAndView home(HttpServletRequest request)
    {
        ModelAndView response = new ModelAndView("rating/list");
        response.addObject("title", title);
        response.addObject("user", request.getRemoteUser());
        response.addObject("ratings", ratingService.findAll());
        return response;
    }

    @GetMapping("/rating/add")
    public ModelAndView addRatingForm(Rating rating) {
        ModelAndView response = new ModelAndView("rating/add");
        response.addObject("title", title + " - ADD");
        return response;
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result) {
        if(result.hasErrors())
            return "rating/add";
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView response = new ModelAndView();
        Optional<Rating> rating = ratingService.findById(id);
        if(rating.isPresent()){
            response.setViewName("rating/update");
            response.addObject("title", title + " - UPDATE");
            response.addObject("rating", rating.get());
            return response;
        }
        response.setViewName("redirect:/rating/list?error=true");
        return response;
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result) {
        if(result.hasErrors())
            return "rating/update";
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteById(id);
        return "redirect:/rating/list";
    }
}
