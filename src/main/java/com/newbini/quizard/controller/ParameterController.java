package com.newbini.quizard.controller;

import com.newbini.quizard.dto.request.ParameterForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/parameter")
public class ParameterController {

    @GetMapping
    public String parameterPage() {
        return "parameter-choice";
    }

    @PostMapping
    public String parameterSetting(@ModelAttribute ParameterForm form, RedirectAttributes redirectAttributes) {

        List<String> problem_types = form.getProblem_types();
        String difficulty = form.getDifficulty();

        // /upload with path parameter
        redirectAttributes.addAttribute("type", problem_types);
        redirectAttributes.addAttribute("difficulty", difficulty);

        return "redirect:/upload";
    }

}
