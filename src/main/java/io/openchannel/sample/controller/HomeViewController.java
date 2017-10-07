package io.openchannel.sample.controller;

import io.openchannel.sample.exception.ApplicationOperationException;
import io.openchannel.sample.service.OpenChannelService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;

/**
 * HomeViewController.java : responsible for rendering all the HTML view
 */


@Controller
public class HomeViewController {
    /**
     * Logger Reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeViewController.class);

    /**
     * Constants
     */
    private static final String MODEL_ID = "modelId";

    /**
     * Undefiend parameter
     */
    private static final String UNDEFINED = "undefined";

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
        model.addAttribute("apps", openChannelService.searchApprovedApps());
        model.addAttribute("featuredApps", openChannelService.getFeaturedApps());
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
    @GetMapping("/details/{appId}/{appVersion}")
    public String getAppDetailPage(@PathVariable("appId") final String appId, @PathVariable(value = "appVersion", required = false) final String version, final Model model) {
        JSONObject appDetail = openChannelService.getAppFromId(appId, version);

        JSONObject customObject = (JSONObject)appDetail.get("customData");
        JSONArray categoryArray = (JSONArray)customObject.get("category");

        JSONObject relatedApps = openChannelService.getSortedCategoryApp(categoryArray.toString(), appId);

        JSONArray modelArray = (JSONArray)appDetail.get("model");
        JSONObject modelObject = (JSONObject) modelArray.get(0);
        String modelId = (String)modelObject.get(MODEL_ID);

        model.addAttribute("relatedApps", relatedApps);
        model.addAttribute("appDetail", appDetail);
        model.addAttribute(MODEL_ID, modelId);
        return "details";
    }

    /**
     * Uninstall app
     *
     * @param ownershipId  unique ownership id
     * @return JsonObject
     */
    @GetMapping("/uninstall/{ownershipId}")
    public @ResponseBody JSONObject uninstallApp(@PathVariable("ownershipId") final String ownershipId) {
        try {
            return openChannelService.unInstallApp(ownershipId);
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
     * @param   category search parameter
     * @return JsonObject
     */
    @GetMapping({"/searchapp/{query}", "/searchapp/{query}/{category}"})
    public @ResponseBody JSONObject searchApp(@PathVariable("query") String query, @PathVariable(value = "category", required = false)String category) {
        try {
            String appCategory = null;
            if(category == null){
                appCategory = UNDEFINED;
            } else {
                appCategory = category;
            }
            return openChannelService.searchAppForQuery(query, appCategory);
        } catch (Exception e) {
            LOGGER.debug("Error while searching app");
            throw new ApplicationOperationException("Failed to search app", e);
        }
    }

    /**
     * Search all the apps owned by the user
     *
     * @return JsonObject
     */
    @GetMapping({"/ownedapp/{collections}", "/ownedapp/{collections}/{queryParam}"})
    public @ResponseBody JSONObject ownedApp(@PathVariable("collections") final String collections, @PathVariable(value = "queryParam", required = false) String queryParam) {
        try {
            String query = null;
            if(queryParam == null) {
                query = UNDEFINED;
            } else {
                query = queryParam;
            }
            return openChannelService.searchOwnedApps(collections, query);
        } catch (Exception e) {
            LOGGER.debug("Error while searching owned app");
            throw new ApplicationOperationException("Failed to search owned app", e);
        }
    }

    /**
     * Renders App detail page
     *
     * @param safeName              safeName of the app
     * @param model model to be injected into view
     * @return view name
     */
    @GetMapping({"/details/{safeName}"})
    public String getAppDetailPage(@PathVariable("safeName") final String safeName, final Model model) {
        JSONObject appDetail = openChannelService.getAppFromSafeName(safeName);

        JSONObject customObject = (JSONObject)appDetail.get("customData");
        JSONArray categoryArray = (JSONArray)customObject.get("category");

        String appId = (String)appDetail.get("appId");

        JSONObject relatedApps = openChannelService.getSortedCategoryApp(categoryArray.toString(), appId);

        JSONArray modelArray = (JSONArray)appDetail.get("model");
        JSONObject modelObject = (JSONObject) modelArray.get(0);
        String modelId = (String)modelObject.get(MODEL_ID);

        model.addAttribute("relatedApps", relatedApps);
        model.addAttribute("appDetail", appDetail);
        model.addAttribute(MODEL_ID, modelId);
        return "details";
    }
}
