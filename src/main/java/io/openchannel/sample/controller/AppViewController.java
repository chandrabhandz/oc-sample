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
     * Toast constants
     */
    private static final String TOAST_TYPE = "toast_type";
    private static final String TOAST_MESSAGE = "toast_message";

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
        int views = 0;
        for (int i = 0; i < statistics.size(); i++) {
            JSONArray statsJsonArray = (JSONArray) statistics.get(i);
            views += Integer.valueOf(String.valueOf(statsJsonArray.get(1)));
        }

        model.addAttribute("inDevelopment", Boolean.FALSE);
        JSONArray list = (JSONArray) appList.get("list");
        if (!CommonUtil.isNull(list))
            for (int i = list.size() - 1; i >= 0; i--) {
                JSONObject app = (JSONObject) list.get(i);
                JSONObject status = (JSONObject) app.get("status");
                if ("inDevelopment".equals(status.get("value"))) {
                    model.addAttribute("inDevelopment", Boolean.TRUE);
                    break;
                }
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
    public String getCreateAppPage(final Model model) {
        if(!model.containsAttribute("app"))
            model.addAttribute("app", new AppFormModel());
        return "app/create";
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
            if (!CommonUtil.isNull(status.get("errors"))) {
                JSONArray error = (JSONArray) status.get("errors");
                String message = String.valueOf(((JSONObject) error.get(0)).get("message"));
                model.addAttribute(TOAST_TYPE, "error");
                model.addAttribute(TOAST_MESSAGE, message);
                redirectAttributes.addFlashAttribute("app", appFormModel);
                redirectAttributes.addFlashAttribute("modelMap", model);
                return "redirect:/app/create/";
            }

            if (appFormModel.getPublish()) {
                model.addAttribute(TOAST_TYPE, "publish");
            } else {
                model.addAttribute(TOAST_TYPE, "create");
            }
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, "error");
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while creating app", e);
        }
        redirectAttributes.addFlashAttribute("modelMap", model);
        LOGGER.debug("App Created ? : {}", status);
        return "redirect:/app/";
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
            int views = 0;
            for (int i = 0; i < statistics.size(); i++) {
                JSONArray statsJsonArray = (JSONArray) statistics.get(i);
                views += Integer.valueOf(String.valueOf(statsJsonArray.get(1)));
            }
            model.addAttribute("views", views);
            model.addAttribute("statistics", statistics.toJSONString());
            return "app/edit";
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, "error");
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("modelMap", model);
            LOGGER.debug("Error while getting app information");
            return "redirect:/app/";
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
            if (!CommonUtil.isNull(status.get("error"))) {
                JSONArray error = (JSONArray) status.get("errors");
                String message = String.valueOf(((JSONObject) error.get(0)).get("message"));
                model.addAttribute(TOAST_TYPE, "error");
                model.addAttribute(TOAST_MESSAGE, message);
                redirectAttributes.addFlashAttribute("app", appFormModel);
                redirectAttributes.addFlashAttribute("modelMap", model);
                return "redirect:/app/edit/" + appFormModel.getAppId() + "/" + appFormModel.getVersion();
            }

            if (appFormModel.getPublish()) {
                model.addAttribute(TOAST_TYPE, "publish");
            } else {
                model.addAttribute(TOAST_TYPE, "update");
            }
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, "error");
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while updating app", e);
            redirectAttributes.addFlashAttribute("modelMap", model);
            return "redirect:/app/edit/" + appFormModel.getAppId() + "/" + appFormModel.getVersion();
        }
        redirectAttributes.addFlashAttribute("modelMap", model);
        LOGGER.debug("App Updated ? : {}", String.valueOf(status));
        return "redirect:/app/";
    }

    /**
     * deletes an existing app
     *
     * @param appId              unique app id
     * @param version            app version to be edited
     * @param redirectAttributes Redirect attributes to pass values to another controller
     * @return view
     */
    @GetMapping("/delete/{appId}/{version}")
    public String deleteApp(@PathVariable("appId") final String appId, @PathVariable(value = "version", required = false) final String version, final Model model, final RedirectAttributes redirectAttributes) {
        try {
            openChannelService.deleteApp(appId, version);
            model.addAttribute(TOAST_TYPE, "status");
            model.addAttribute(TOAST_MESSAGE, "App has been deleted");
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, "error");
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while deleting app");
        }
        redirectAttributes.addFlashAttribute("modelMap", model);
        return "redirect:/app/";
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
            model.addAttribute(TOAST_TYPE, "publish");
        } catch (Exception e) {
            model.addAttribute(TOAST_TYPE, "error");
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while publishing app");
        }
        redirectAttributes.addFlashAttribute("modelMap", model);
        return "redirect:/app/";
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
            model.addAttribute(TOAST_TYPE, "error");
            model.addAttribute(TOAST_MESSAGE, e.getLocalizedMessage());
            LOGGER.debug("Error while changing app status");
        }
        redirectAttributes.addFlashAttribute("modelMap", model);
        return "redirect:/app/";
    }

}
