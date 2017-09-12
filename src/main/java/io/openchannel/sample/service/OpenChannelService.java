package io.openchannel.sample.service;

import io.openchannel.sample.form.AppFormModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;

/**
 * OpenChannelService.java : OpenChannelService which serves as abstraction layer between implementation and usage
 */

public interface OpenChannelService {
    /**
     * search for all the apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    JSONObject searchAllApps();

    /**
     * search for all the approved apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    JSONObject searchApprovedApps();

    /**
     * @return JSONArray returns array of statistics
     */
    JSONArray getStatistics();

    /**
     * Get stats based on developerid & appid
     *
     * @param appId unique app id
     * @return api response
     */
    JSONArray getStatistics(String appId);

    /**
     * Uploads a file to openchannel API and gets information about uploaded file
     *
     * @param content
     * @return file url
     */
    String uploadFiles(final File content);

    /**
     * Creates an app to openchannel market place
     *
     * @param appFormModel
     * @return
     */
    JSONObject createApp(final AppFormModel appFormModel);

    /**
     * Publish an app to open channel marketplace
     *
     * @param appFormModel App form model
     * @return Api Response
     */
    JSONObject publishApp(final AppFormModel appFormModel);

    /**
     * Delete an app from open channel marketplace
     * If version is not supplied, complete app is deleted otherwise only specified version
     *
     * @param appId   unique appid
     * @param version version identified (Can be empty)
     * @return api response
     */
    JSONObject deleteApp(String appId, String version);

    /**
     * Updates an App to open channel marketplace
     *
     * @param appFormModel App form model which contains information about app to be updated
     * @return Api Response
     */
    JSONObject updateApp(AppFormModel appFormModel);

    /**
     * Fetches app data from openchannel api
     *
     * @param appId   unique app id
     * @param version app version
     * @return AppFormModel
     */
    AppFormModel getApp(String appId, String version);

    /**
     * Change app status
     *
     * @param appId  unique app id
     * @param status new app status
     * @return ApiResponse
     * @throws RuntimeException
     */
    JSONObject changeAppStatus(String appId, String status);

    /**
     * search for all the apps based on category using loaded developer id
     *
     * @param category  app category
     * @return JsonObject which contains data returned from API
     */
    JSONObject searchAppsForCategory(String category);

    /**
     * Fetches app data using appId and version
     *
     * @param appId   unique app id
     * @param version app version
     * @return JsonObject which contains data returned from API
     */
    JSONObject getAppFromId(String appId, String version);

    /**
     * Uninstall app
     *
     * @param appId unique app id
     * @return JsonObject
     */
    JSONObject unInstallApp(final String appId);

    /**
     * Install app
     *
     * @param appId unique app id
     * @return JsonObject
     */
    JSONObject installApp(final String appId);

}
