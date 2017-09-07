package io.openchannel.sample.controller;

import io.openchannel.sample.form.AppFormModel;
import io.openchannel.sample.service.OpenChannelService;
import io.openchannel.sample.util.CommonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AppViewController.java : responsible for rendering all the HTML view
 */

@Controller
@RequestMapping("/app")
public class AppViewController {
    /**
     * Logger Reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppViewController.class);

    /**
     * Constants
     */
    private static final String TOAST_TYPE = "toast_type";
    private static final String TOAST_MESSAGE = "toast_message";
    private static final String PUBLISH = "publish";
    private static final String ERRORS = "errors";
    private static final String MODEL_MAP = "modelMap";
    private static final String REDIRECT_APP = "redirect:/app";
    private static final String IN_DEVELOPMENT = "inDevelopment";
    private static final String ERROR = "error";

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
        JSONObject appList = openChannelService.searchApps();
        model.addAttribute("apps", appList);
        JSONArray statistics = openChannelService.getStatistics();
        double views = 0;
        for (int i = 0; i < statistics.size(); i++) {
            JSONArray statsJsonArray = (JSONArray) statistics.get(i);
            try {
                views += Double.valueOf(String.valueOf(statsJsonArray.get(1)));
            } catch (NumberFormatException e) {
                LOGGER.info("Error while parsing stats to double : {}", statsJsonArray.get(1));
                LOGGER.debug("can not parse to double", e);
            }
        }

        model.addAttribute(IN_DEVELOPMENT, Boolean.FALSE);
        JSONArray list = (JSONArray) appList.get("list");
        if (!CommonUtil.isNull(list))
            for (int i = list.size() - 1; i >= 0; i--) {
                JSONObject app = (JSONObject) list.get(i);
                JSONObject status = (JSONObject) app.get("status");
                if (IN_DEVELOPMENT.equals(status.get("value"))) {
                    model.addAttribute(IN_DEVELOPMENT, Boolean.TRUE);
                    break;
                }
            }

        model.addAttribute("views", (int) views);
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
    public String getCreateAppPage(final Model model) {
        if (!model.containsAttribute("app"))
            model.addAttribute("app", new AppFormModel());
        return "create";
    }

    /**
     * Handles app submission where user can update already existing app
     *
     * @param appFormModel       form model submitted by user
     * @param model              Spring UI Model
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view
     */
    @PostMapping("/create")
    public String createApp(@ModelAttribute final AppFormModel appFormModel, final RedirectAttributes redirectAttributes) {
        final Model model = new BindingAwareModelMap();
        JSONObject status = null;
        try {
            status = openChannelService.createApp(appFormModel);
            if (!CommonUtil.isNull(status.get(ERRORS))) {
                JSONArray error = (JSONArray) status.get(ERRORS);
                String message = String.valueOf(((JSONObject) error.get(0)).get("message"));
                model.addAttribute(TOAST_TYPE, ERROR);
                model.addAttribute(TOAST_MESSAGE, message);
                redirectAttributes.addFlashAttribute("app", appFormModel);
                redirectAttributes.addFlashAttribute(MODEL_MAP, model);
                return "redirect:/app/create/";
            }

            if (appFormModel.getPublish()) {
                model.addAttribute(TOAST_TYPE, PUBLISH);
            } else {
                model.addAttribute(TOAST_TYPE, "create");
            }
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, ERROR);
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while creating app", e);
        }
        redirectAttributes.addFlashAttribute(MODEL_MAP, model);
        LOGGER.debug("App Created ? : {}", status);
        return REDIRECT_APP;
    }

    /**
     * Renders App Edit page where user can edit an app
     *
     * @param appId              unique app id
     * @param version            app version to be edited
     * @param model              model to be injected into view
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view name
     */
    @GetMapping("/edit/{appId}/{version}")
    public String getEditAppPage(@PathVariable("appId") final String appId, @PathVariable("version") final String version, final Model model, final RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("app", openChannelService.getApp(appId, version));

            JSONArray statistics = openChannelService.getStatistics(appId);
            double views = 0;
            for (int i = 0; i < statistics.size(); i++) {
                JSONArray statsJsonArray = (JSONArray) statistics.get(i);
                views += Double.parseDouble(String.valueOf(statsJsonArray.get(1)));
            }
            model.addAttribute("views", (int) views);
            model.addAttribute("statistics", statistics.toJSONString());
            return "edit";
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, ERROR);
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute(MODEL_MAP, model);
            LOGGER.debug("Error while getting app information");
            return REDIRECT_APP;
        }
    }

    /**
     * Handles app submission where user can update already existing app
     *
     * @param appFormModel       form model submitted by user
     * @param model              Spring UI Model
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view
     */
    @PostMapping("/edit")
    public String updateApp(@ModelAttribute final AppFormModel appFormModel, final Model model, final RedirectAttributes redirectAttributes) {

        JSONObject status = null;
        try {
            status = openChannelService.updateApp(appFormModel);
            if (!CommonUtil.isNull(status.get(ERROR))) {
                JSONArray error = (JSONArray) status.get(ERRORS);
                String message = String.valueOf(((JSONObject) error.get(0)).get("message"));
                model.addAttribute(TOAST_TYPE, ERROR);
                model.addAttribute(TOAST_MESSAGE, message);
                redirectAttributes.addFlashAttribute("app", appFormModel);
                redirectAttributes.addFlashAttribute(MODEL_MAP, model);
                return "redirect:/app/edit/" + appFormModel.getAppId() + "/" + appFormModel.getVersion();
            }

            if (appFormModel.getPublish()) {
                model.addAttribute(TOAST_TYPE, PUBLISH);
            } else {
                model.addAttribute(TOAST_TYPE, "update");
            }
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, ERROR);
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while updating app", e);
            redirectAttributes.addFlashAttribute(MODEL_MAP, model);
            return "redirect:/app/edit/" + appFormModel.getAppId() + "/" + appFormModel.getVersion();
        }
        redirectAttributes.addFlashAttribute(MODEL_MAP, model);
        LOGGER.debug("App Updated ? : {}", status);
        return REDIRECT_APP;
    }

    /**
     * deletes an existing app
     *
     * @param appId              unique app id
     * @param version            app version to be edited
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view
     */
    @GetMapping({"/delete/{appId}/{version}", "/delete/{appId}"})
    public String deleteApp(@PathVariable("appId") final String appId, @PathVariable(value = "version", required = false) final String version, final Model model, final RedirectAttributes redirectAttributes) {
        try {
            openChannelService.deleteApp(appId, version);
            model.addAttribute(TOAST_TYPE, "status");
            model.addAttribute(TOAST_MESSAGE, "App has been deleted");
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, ERROR);
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while deleting app");
        }
        redirectAttributes.addFlashAttribute(MODEL_MAP, model);
        return REDIRECT_APP;
    }

    /**
     * Publish an app to marketplace
     *
     * @param appId              unique app id
     * @param version            app version to be edited
     * @param model              Spring model
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view view
     */
    @GetMapping("/publish/{appId}/{version}")
    public String publishApp(@PathVariable("appId") final String appId, @PathVariable(value = "version", required = false) final String version, final Model model, final RedirectAttributes redirectAttributes) {
        try {
            openChannelService.publishApp(openChannelService.getApp(appId, version));
            model.addAttribute(TOAST_TYPE, PUBLISH);
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, ERROR);
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while publishing app");
        }
        redirectAttributes.addFlashAttribute(MODEL_MAP, model);
        return REDIRECT_APP;
    }

    /**
     * Change app status to given one
     *
     * @param appId              unique app id
     * @param status             new App status
     * @param model              Spring model
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view view
     */
    @GetMapping("/status/{appId}/{status}")
    public String changeStatus(@PathVariable("appId") final String appId, @PathVariable(value = "status", required = false) final String status, final Model model, final RedirectAttributes redirectAttributes) {
        try {
            openChannelService.changeAppStatus(appId, status);
            model.addAttribute(TOAST_TYPE, "update");
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, ERROR);
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while changing app status");
        }
        redirectAttributes.addFlashAttribute(MODEL_MAP, model);
        return REDIRECT_APP;
    }

}
