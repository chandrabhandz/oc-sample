package io.openchannel.sample.controller;

import io.openchannel.sample.exception.ApplicationOperationException;
import io.openchannel.sample.service.OpenChannelService;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;

@Controller
public class HomeViewController {
    /**
     * Logger Reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeViewController.class);

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
    public HomeViewController(OpenChannelService openChannelService) {
        this.openChannelService = openChannelService;
    }

    /**
     * Renders home page
     *
     * @param model model to be injected into view
     * @return view name
     */
    @GetMapping({"/", ""})
    public String renderHome(final Model model) {
        JSONObject appList = openChannelService.searchApprovedApps();
        model.addAttribute("apps", appList);
        return "index";
    }

    /**
     * Searches for apps category wise
     *
     * @param category  app category
     * @return JsonObject
     */
    @GetMapping("/category/{category}")
    public @ResponseBody JSONObject renderCategoryApp(@PathVariable("category") final String category) {
        return openChannelService.searchAppsForCategory(category);
    }

    /**
     * Renders App detail page
     *
     * @param appId              unique app id
     * @param version            app version for the appId
     * @param model model to be injected into view
     * @return view name
     */
    @GetMapping({"/details/{appId}","/details/{appId}/{appVersion}"})
    public String getAppDetailPage(@PathVariable("appId") final String appId, @PathVariable(value = "appVersion", required = false) final String version, final Model model) {
        JSONObject appDetail = openChannelService.getAppFromId(appId, version);

        model.addAttribute("appDetail", appDetail);
        return "details";
    }

    /**
     * Uninstall app
     *
     * @param appId  unique app id
     * @return JsonObject
     */
    @GetMapping("/uninstall/{appId}")
    public @ResponseBody JSONObject uninstallApp(@PathVariable("appId") final String appId) {
        try {
            return openChannelService.unInstallApp(appId);
        } catch (Exception e) {
            LOGGER.debug("Error while uninstalling app");
            throw new ApplicationOperationException("Failed to uninstall app", e);
        }
    }

    /**
     * Install app
     *
     * @param appId  unique app id
     * @return JsonObject
     */
    @GetMapping("/install/{appId}/{modelId}")
    public @ResponseBody JSONObject installApp(@PathVariable("appId") final String appId, @PathVariable("modelId") final String modelId) {
        try {
            return openChannelService.installApp(appId, modelId);
        } catch (Exception e) {
            LOGGER.debug("Error while installing app");
            throw new ApplicationOperationException("Failed to install app", e);
        }
    }

    /**
     * Search apps
     *
     * @param   query search parameter
     * @return JsonObject
     */
    @GetMapping("/searchapp/{query}")
    public @ResponseBody JSONObject searchApp(@PathVariable("query") final String query) {
        try {
            return openChannelService.searchApp(query);
        } catch (Exception e) {
            LOGGER.debug("Error while searching app");
            throw new ApplicationOperationException("Failed to search app", e);
        }
    }
}
