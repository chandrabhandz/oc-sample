package io.openchannel.sample.controller;

import io.openchannel.sample.form.AppFormModel;
import io.openchannel.sample.service.OpenChannelService;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AppViewController.java : responsible for rendering all the HTML view
 * <p>
 * Created on 29/8/17 4:53 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Controller
@RequestMapping("/app")
public class AppViewController {
    /**
     * Logger Reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppViewController.class);

    /**
     * OpenChannelService, an intermediate layer between APIs and Controller
     */
    private OpenChannelService openChannelService;

    /**
     * Constructor injection for dependency
     *
     * @param openChannelService open channel service reference
     */
    @Autowired
    public AppViewController(OpenChannelService openChannelService) {
        this.openChannelService = openChannelService;
    }

    /**
     * Renders index/default page
     *
     * @param model model to be injected into view
     * @return view name
     */
    @GetMapping({"", "/"})
    public String index(final Model model) {
        LOGGER.debug("initializing index page rendering");
        model.addAttribute("apps", openChannelService.searchApps());
        JSONArray statistics = openChannelService.getStatistics();
        int views = 0;
        for (int i = 0; i < statistics.size(); i++) {
            JSONArray statsJsonArray = (JSONArray) statistics.get(i);
            views += Integer.valueOf(String.valueOf(statsJsonArray.get(1)));
        }

        model.addAttribute("views", views);
        model.addAttribute("statistics", statistics.toJSONString());

        return "index";
    }

    /**
     * Renders App Create page where user can create a new application
     *
     * @param model model to be injected into view
     * @return view name
     */
    @GetMapping("/create")
    public String createApp(final Model model) {
        model.addAttribute("app", new AppFormModel());
        return "app/create";
    }
}
