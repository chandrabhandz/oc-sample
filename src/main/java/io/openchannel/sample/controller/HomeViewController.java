package io.openchannel.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 2/9/17 7:40 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Controller
public class HomeViewController {
    @GetMapping({"/", ""})
    public String renderHome(){
        return "redirect:/app/";
    }
}