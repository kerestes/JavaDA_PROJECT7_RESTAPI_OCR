package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
public class RuleNameController {

    private final String title = "Rule Name";
    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public ModelAndView home(HttpServletRequest request)
    {
        ModelAndView response = new ModelAndView("ruleName/list");
        response.addObject("title", title);
        response.addObject("user", request.getRemoteUser());
        response.addObject("ruleNames", ruleNameService.findAll());
        return response;
    }

    @GetMapping("/ruleName/add")
    public ModelAndView addRuleForm(RuleName ruleName) {
        ModelAndView response = new ModelAndView("ruleName/add");
        response.addObject("title", title + " - ADD");
        return response;
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result) {
        if(result.hasErrors())
            return "ruleName/add";
        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView response = new ModelAndView();
        Optional<RuleName> ruleName = ruleNameService.findById(id);
        if(ruleName.isPresent()){
            response.setViewName("ruleName/update");
            response.addObject("title", title + " - UPDATE");
            response.addObject("ruleName", ruleName.get());
            return response;
        }
        response.setViewName("redirect:/ruleName/list?error=true");
        return response;
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result) {
        if(result.hasErrors())
            return "ruleName/update";
        ruleNameService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
