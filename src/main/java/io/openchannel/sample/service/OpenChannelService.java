package io.openchannel.sample.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * OpenChannelService.java : OpenChannelService which serves as abstraction layer between implementation and usage
 *
 * Created on 29/8/17 5:36 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public interface OpenChannelService {
    /**
     * search for apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    JSONObject searchApps();

    /**
     *
     * @return JSONArray returns array of statistics
     */
    JSONArray getStatistics();
}
