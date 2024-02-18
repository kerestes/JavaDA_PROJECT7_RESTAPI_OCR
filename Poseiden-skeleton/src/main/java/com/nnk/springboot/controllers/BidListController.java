package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
public class BidListController {

    private final String title = "Bid List";

    @Autowired
    private BidListService bidListService;

    @RequestMapping("/bidList/list")
    public ModelAndView home(HttpServletRequest request){
        ModelAndView response = new ModelAndView("bidList/list");
        response.addObject("user", request.getRemoteUser());
        response.addObject("title", title);
        response.addObject("bidLists", bidListService.findAll());
        return response;
    }

    @GetMapping("/bidList/add")
    public ModelAndView addBidForm(BidList bid) {
        ModelAndView response = new ModelAndView("bidList/add");
        response.addObject("title", title + " - ADD");
        return response;
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {
        if(result.hasErrors()){
            return "bidList/add";
        }
        bidListService.save(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView response = new ModelAndView();
        Optional<BidList> bidList = bidListService.findById(id);
        if(bidList.isPresent()){
            response.setViewName("bidList/update");
            response.addObject("title", title + " - UPDATE");
            response.addObject("bidList", bidList.get());
            return response;
        }
        response.setViewName("redirect:/bidList/list?error=true");
        return response;
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bid,
                             BindingResult result) {
        if(result.hasErrors()){
            return "bidList/update";
        }
        bidListService.save(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
