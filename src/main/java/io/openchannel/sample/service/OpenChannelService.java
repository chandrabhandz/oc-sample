package io.openchannel.sample.service;

import io.openchannel.sample.form.AppFormModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

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

    /**
     * Uploads a file to openchannel API and gets information about uploaded file
     * @param content
     * @return file url
     */
    String uploadFiles(final File content);

    /**
     * Creates an app to openchannel market place
     * @param appFormModel
     * @return
     */
    JSONObject createApp(final AppFormModel appFormModel);

    /**
     * Publish an app to open channel marketplace
     * @param appFormModel App form model
     * @return True if app is published successfully
     */
    Boolean publishApp(final AppFormModel appFormModel);
}
