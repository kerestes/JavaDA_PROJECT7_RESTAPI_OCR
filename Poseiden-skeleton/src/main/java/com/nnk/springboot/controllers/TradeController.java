package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {

    private final String title = "Trade";
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public ModelAndView home(HttpServletRequest request)
    {
        ModelAndView response = new ModelAndView("trade/list");
        response.addObject("title", title);
        response.addObject("user", request.getRemoteUser());
        response.addObject("trades", tradeService.findAll());
        return response;
    }

    @GetMapping("/trade/add")
    public ModelAndView addUser(Trade trade) {
        ModelAndView response = new ModelAndView("trade/add");
        response.addObject("title", title + " - ADD");
        return response;
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {
        if(result.hasErrors())
            return "trade/add";
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView response = new ModelAndView();
        Optional<Trade> trade = tradeService.findById(id);
        if(trade.isPresent()){
            response.setViewName("trade/update");
            response.addObject("title", title + " - UPDATE");
            response.addObject("trade", trade.get());
            return response;
        }
        response.setViewName("redirect:/trade/list?error=true");
        return response;
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result) {
        if(result.hasErrors())
            return "trade/update";
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
