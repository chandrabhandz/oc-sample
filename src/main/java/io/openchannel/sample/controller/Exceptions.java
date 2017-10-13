package io.openchannel.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;

public class Exceptions {

	private static final Logger LOGGER = LoggerFactory.getLogger(Exceptions.class);
    /**
     * Renders error page for invalid parameter exception
     *
     * @param model model to be injected into view
     * @return view name
     */
    @ExceptionHandler(InvalidParameterException.class)
    public String handleResourceNotFoundException(InvalidParameterException ie, Model model) {
        model.addAttribute("message", ie.getMessage());
        return "error";
    }

    /**
     * Renders error page
     *
     * @param model model to be injected into view
     * @return view name
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        model.addAttribute("message", "If you are the application owner check the logs for more information.");
        
        LOGGER.error("Error",e);
        return "error";
    }
}
