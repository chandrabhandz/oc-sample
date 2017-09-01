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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
    public String getCreateAppPage(final Model model) {
        model.addAttribute("app", new AppFormModel());
        return "app/create";
    }

    @PostMapping("/create")
    public String createApp(@ModelAttribute final AppFormModel appFormModel, final Model model, final RedirectAttributes redirectAttributes) {
        JSONObject status = openChannelService.createApp(appFormModel);
        if(!CommonUtil.isNull(status.get("error"))) {
            JSONArray error = (JSONArray) status.get("error");
            String message = String.valueOf(((JSONObject) error.get(0)).get("message"));
            model.addAttribute("toast_type", "error");
            model.addAttribute("toast_message", message);
            redirectAttributes.addFlashAttribute(appFormModel);
            redirectAttributes.addFlashAttribute(model);
            return "redirect:/app/create/";
        }

        if(appFormModel.getPublish()) {
            model.addAttribute("toast_type", "publish");
        } else {
            model.addAttribute("toast_type", "create");
        }
        redirectAttributes.addFlashAttribute(model);
        LOGGER.debug("App Created ? : {}", status);
        return "redirect:/app/";
    }

}
