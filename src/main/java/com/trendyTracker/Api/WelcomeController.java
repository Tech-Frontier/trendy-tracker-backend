package com.trendytracker.Api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    
    @GetMapping("/")
    public String SwaggerPage(){
        return "redirect:/swagger-ui/index.html"; 
    }
}
