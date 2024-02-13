package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
public class CurveController {

    private final String title = "Curve Point";

    @Autowired
    private CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public ModelAndView home(HttpServletRequest request)
    {
        ModelAndView response = new ModelAndView("curvePoint/list");
        response.addObject("title", title);
        response.addObject("user", request.getRemoteUser());
        response.addObject("curvePoints", curvePointService.findAll());
        return response;
    }

    @GetMapping("/curvePoint/add")
    public ModelAndView addBidForm(CurvePoint curvePoint) {
        ModelAndView response = new ModelAndView("curvePoint/add");
        response.addObject("title", title + " - ADD");
        return response;
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {
        if(result.hasErrors()){
            return "curvePoint/add";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView response = new ModelAndView();
        Optional<CurvePoint> curvePoint = curvePointService.findById(id);
        if(curvePoint.isPresent()){
            response.setViewName("curvePoint/update");
            response.addObject("title", title + " - UPDATE");
            response.addObject("curvePoint", curvePoint.get());
            return response;
        }
        response.setViewName("redirect:/curvePoint/list?error=true");
        return response;
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result) {
        if(result.hasErrors()){
            return "curvePoint/update";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
