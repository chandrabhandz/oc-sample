package io.openchannel.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 2/9/17 7:40 PM by Raja Dushyant Vashishtha
 */

@Controller
public class HomeViewController {
    @GetMapping({"/", ""})
    public String renderHome() {
        return "redirect:/app/";
    }
}
