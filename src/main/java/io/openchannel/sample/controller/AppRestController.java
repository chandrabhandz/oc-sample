package io.openchannel.sample.controller;

import io.openchannel.sample.form.AppFormModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AppRestController.java : Rest API controller which will communicate with openchannel API
 *
 * Created on 29/8/17 4:54 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@RestController
@RequestMapping("/api/app")
public class AppRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRestController.class);

    @PostMapping("/create")
    public String create(@ModelAttribute AppFormModel appFormModel) {
        LOGGER.debug(appFormModel.toString());
        return "Hello";
    }
}
