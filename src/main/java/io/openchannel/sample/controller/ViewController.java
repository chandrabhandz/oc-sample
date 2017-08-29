package io.openchannel.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 29/8/17 4:53 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Controller
public class ViewController extends AbstractController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
