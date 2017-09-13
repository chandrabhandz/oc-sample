package io.openchannel.sample.service.impl;

import io.openchannel.sample.config.OpenChannelProperties;
import io.openchannel.sample.exception.ApplicationOperationException;
import io.openchannel.sample.form.AppFormModel;
import io.openchannel.sample.service.OpenChannelService;
import io.openchannel.sample.util.CommonUtil;
import io.openchannel.sample.util.JSONUtil;
import io.openchannel.sample.util.OpenChannelAPIUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * OpenChannelServiceImpl.java : Communicates through open channel APIs
 */

@Service
public class OpenChannelServiceImpl implements OpenChannelService {
    /**
     * Logger reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenChannelServiceImpl.class);

    /**
     * Endpoint for APP Search
     */
    private static final String ENDPOINT_APPS = "apps/versions";

    /**
     * Endpoint for stats
     */
    private static final String ENDPOINT_STATS = "stats/series/month/views";

    /**
     * Endpoint for files
     */
    private static final String ENDPOINT_FILES = "files";

    /**
     * Endpoint for creating/publish/delete app
     */
    private static final String ENDPOINT_CREATE_APP = "apps";

    /**
     * Endpoint for uninstalling app
     */
    private static final String ENDPOINT_UNINSTALL_APP = "ownership/apps";


    /**
     * Static Constants
     */
    private static final String URL_SEPARATOR = "/";

    /**
     * DeveloperID
     */
    private static final String DEVELOPER_ID = "developerId";

    /**
     * Query
     */
    private static final String QUERY = "query";

    /**
     * CustomData
     */
    private static final String CUSTOMDATA = "customData";

    /**
     * Errors
     */
    private static final String ERROR = "errors";

    /**
     * AppId
     */
    private static final String APP_ID = "appId";

    /**
     * Version
     */
    private static final String VERSION = "version";

    /**
     * Message
     */
    private static final String MESSAGE = "message";

    /**
     * URL to be appended in query
     * Versions
     */
    private static final String URL_VERSIONS = "/versions";

    /**
     * UserId
     */
    private static final String USER_ID = "userId";

    /**
     * OpenChannelAPIUtil which performs low level communication with APIs
     */
    private final OpenChannelAPIUtil openChannelAPIUtil;

    /**
     * OpenChannelProperties, loaded by spring from application.yml
     */
    private final OpenChannelProperties openChannelProperties;

    /**
     * Constructor Injection for the dependencies
     *
     * @param openChannelAPIUtil    open channel API util reference
     * @param openChannelProperties open channel properties wrapper
     */
    @Autowired
    public OpenChannelServiceImpl(OpenChannelAPIUtil openChannelAPIUtil, OpenChannelProperties openChannelProperties) {
        this.openChannelAPIUtil = openChannelAPIUtil;
        this.openChannelProperties = openChannelProperties;
    }

    /**
     * search for all apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    @Override
    public JSONObject searchAllApps() {
        String query = "{$or:[{\"status.value\":\"rejected\",isLatestVersion:true},{isLive:true},{\"status.value\":{$in:[\"inDevelopment\",\"inReview\",\"pending\"]}}]}";
        return searchApps(query);
    }

    /**
     * search for all approved apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    @Override
    public JSONObject searchApprovedApps() {
        String query = "{$or:[{\"status.value\":\"approved\",isLatestVersion:true},{isLive:true},{\"status.value\":{$in:[\"inDevelopment\"]}},{userId:"+ openChannelProperties.getUserId() +"}]}";
        return searchApps(query);
    }

    /**
     * search for apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    private JSONObject searchApps(String query) {
        try {
            return JSONUtil.getJSONObject(openChannelAPIUtil.sendGet(ENDPOINT_APPS, new OpenChannelAPIUtil.RequestParameter(DEVELOPER_ID, openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter(QUERY, query)));
        } catch (IOException e) {
            LOGGER.warn("Error while communicating with openchannel search api", e);
        }
        return new JSONObject();
    }

    /**
     * get stats based on developer id
     *
     * @return JSONArray of statistics
     */
    @Override
    public JSONArray getStatistics() {
        try {
            return JSONUtil.getJSONArray(openChannelAPIUtil.sendGet(ENDPOINT_STATS, new OpenChannelAPIUtil.RequestParameter(QUERY, "{developerId: '" + openChannelProperties.getDeveloperId() + "'}")));
        } catch (IOException e) {
            LOGGER.warn("Error while communicating with openchannel stats api", e);
        }
        return new JSONArray();
    }

    /**
     * Get stats based on developerid & appid
     *
     * @param appId unique app id
     * @return api response
     */
    @Override
    public JSONArray getStatistics(final String appId) {
        try {
            return JSONUtil.getJSONArray(openChannelAPIUtil.sendGet(ENDPOINT_STATS, new OpenChannelAPIUtil.RequestParameter(QUERY, "{developerId: '" + openChannelProperties.getDeveloperId() + "', appId: '" + appId + "'}")));
        } catch (IOException e) {
            LOGGER.warn("Error while communicating with openchannel stats api", e);
        }
        return new JSONArray();
    }


    /**
     * Uploads a file to openchannel API and gets information about uploaded file
     *
     * @param content file content which needs to be uploaded to openchanned API
     * @return file url
     */
    @Override
    public String uploadFiles(final File content) {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_FILES, OpenChannelAPIUtil.PostContentType.MULTIPART, new OpenChannelAPIUtil.RequestParameter("file", content)));
            if (!CommonUtil.isNull(apiResponse.get("fileUrl"))) {
                return apiResponse.get("fileUrl").toString();
            }
        } catch (IOException e) {
            LOGGER.warn("Error while uploading file to openchannel api", e);
        }
        return "";
    }

    /**
     * Creates an App to open channel marketplace
     *
     * @param appFormModel App form model which contains information about newly created app
     * @return Api Response
     */
    @Override
    public JSONObject createApp(final AppFormModel appFormModel) {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP, OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter(DEVELOPER_ID, openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("name", appFormModel.getName()), new OpenChannelAPIUtil.RequestParameter(CUSTOMDATA, appFormModel)));
            if (CommonUtil.isNull(apiResponse.get(ERROR)) && appFormModel.getPublish()) {
                appFormModel.setAppId(String.valueOf(apiResponse.get(APP_ID)));
                appFormModel.setVersion(String.valueOf(apiResponse.get(VERSION)));
                appFormModel.setName(String.valueOf(apiResponse.get("name")));
                JSONObject publishApiResponse = publishApp(appFormModel);
                if (!publishApiResponse.isEmpty()) {
                    return publishApiResponse;
                }
            }
            return apiResponse;
        } catch (IOException e) {
            LOGGER.warn("Error while submitting app to openchannel api", e);
        }
        return new JSONObject();
    }

    /**
     * Publish an app to open channel marketplace
     *
     * @param appFormModel App form model
     * @return Api response
     */
    @Override
    public JSONObject publishApp(final AppFormModel appFormModel) {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP + "/" + appFormModel.getAppId() + "/" + "publish", OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter(DEVELOPER_ID, openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter(VERSION, Integer.parseInt(appFormModel.getVersion()))));
            if (!CommonUtil.isNull(apiResponse.get(ERROR))) {
                JSONArray errors = (JSONArray) apiResponse.get(ERROR);
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get(MESSAGE)));
            }
            return apiResponse;
        } catch (IOException e) {
            LOGGER.debug("Error while publishing app to openchannel, Root cause : ", e);
            LOGGER.warn("Error while publishing app to openchannel");
            throw new ApplicationOperationException("Failed to publish", e);
        }
    }

    /**
     * Delete an app from open channel marketplace
     * If version is not supplied, complete app is deleted otherwise only specified version
     *
     * @param appId   unique appid
     * @param version version identified (Can be empty)
     * @return api response
     */
    @Override
    public JSONObject deleteApp(final String appId, final String version) {
        try {
            String finalPath = ENDPOINT_CREATE_APP + URL_SEPARATOR + appId;
            if (!CommonUtil.isNull(version) && !"".equals(version)) {
                finalPath = finalPath + URL_VERSIONS + version;
            }
            return JSONUtil.getJSONObject(openChannelAPIUtil.sendDelete(finalPath, new OpenChannelAPIUtil.RequestParameter(DEVELOPER_ID, openChannelProperties.getDeveloperId())));
        } catch (IOException e) {
            LOGGER.debug("Error while deleting app from openchannel api, Root cause : ", e);
            LOGGER.warn("Error while deleting app from openchannel api");
            throw new ApplicationOperationException("Failed to delete", e);
        }
    }

    /**
     * Updates an App to open channel marketplace
     *
     * @param appFormModel App form model which contains information about app to be updated
     * @return Api Response
     */
    @Override
    public JSONObject updateApp(final AppFormModel appFormModel) {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP + "/" + appFormModel.getAppId() + URL_VERSIONS + appFormModel.getVersion(), OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter(DEVELOPER_ID, openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("name", appFormModel.getName()), new OpenChannelAPIUtil.RequestParameter(CUSTOMDATA, appFormModel)));

            if (!CommonUtil.isNull(apiResponse.get(ERROR))) {
                JSONArray errors = (JSONArray) apiResponse.get(ERROR);
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get(MESSAGE)));
            }

            if (appFormModel.getPublish()) {
                JSONObject publishApiResponse = publishApp(appFormModel);
                if (!publishApiResponse.isEmpty()) {
                    return publishApiResponse;
                }
            }
            return apiResponse;
        } catch (IOException e) {
            LOGGER.debug("Error while updating app to openchannel api, Root cause : ", e);
            LOGGER.warn("Error while updating app to openchannel api");
            throw new ApplicationOperationException("Failed to update", e);
        }
    }

    /**
     * Fetches app data from openchannel api
     *
     * @param appId   unique app id
     * @param version app version
     * @return AppFormModel with all available details
     * @throws ApplicationOperationException can be thrown for errors
     */
    @Override
    public AppFormModel getApp(final String appId, final String version) {
            String path = ENDPOINT_CREATE_APP + URL_SEPARATOR + appId + URL_VERSIONS + version;

            JSONObject apiResponse = getAppJsonObject(path, DEVELOPER_ID, openChannelProperties.getDeveloperId());
            if (CommonUtil.isNull(apiResponse.get(APP_ID)) && !CommonUtil.isNull(apiResponse.get(ERROR))) {
                JSONArray errors = (JSONArray) apiResponse.get(ERROR);
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get(MESSAGE)));
            }
            AppFormModel appFormModel = AppFormModel.fromJson(((JSONObject) apiResponse.get(CUSTOMDATA)).toJSONString());
            appFormModel.setAppId(String.valueOf(apiResponse.get(APP_ID)));
            appFormModel.setVersion(String.valueOf(apiResponse.get(VERSION)));
            appFormModel.setName(String.valueOf(apiResponse.get("name")));
            return appFormModel;
    }

    /**
     * Change app status
     *
     * @param appId   unique app id
     * @return JsonObject with all available details
     * @throws ApplicationOperationException can be thrown for errors
     */
    @Override
    public JSONObject changeAppStatus(final String appId, final String status) {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP + "/" + appId + "/status", OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter(DEVELOPER_ID, openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("status", status)));
            if (!CommonUtil.isNull(apiResponse.get(ERROR))) {
                JSONArray errors = (JSONArray) apiResponse.get(ERROR);
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get(MESSAGE)));
            }
            return apiResponse;
        } catch (IOException e) {
            LOGGER.debug("Error while changing app status in openchannel api, Root cause : ", e);
            LOGGER.warn("Error while changing app status in openchannel api");
            throw new ApplicationOperationException("Failed to change app status", e);
        }
    }

    /**
     * search for all the apps based on category using loaded developer id
     *
     * @param category  app category
     * @return JsonObject which contains data returned from API
     */
    @Override
    public JSONObject searchAppsForCategory(String category){
        String query = "{'status.value':'approved','customData.category':'" + category + "'},{isLive:true},{'userId':'"+ openChannelProperties.getUserId() +"'}";
        return searchApps(query);
    }

    /**
     * Fetches app data using appId and version
     *
     * @param appId   unique app id
     * @param version app version
     * @return JsonObject which contains data returned from API
     */
    @Override
    public JSONObject getAppFromId(String appId, String version) {

            String path;
            if (!CommonUtil.isNull(version)) {
                path = ENDPOINT_CREATE_APP + URL_SEPARATOR + appId + URL_VERSIONS + version;
            } else {
                path = ENDPOINT_CREATE_APP + URL_SEPARATOR + appId;
            }

            return getAppJsonObject(path, USER_ID, openChannelProperties.getUserId());
    }

    /**
     * Uninstall app
     *
     * @param appId unique app id
     * @return JsonObject
     */
    @Override
    public JSONObject unInstallApp(final String appId) {
        try {
            return JSONUtil.getJSONObject(openChannelAPIUtil.sendDelete(ENDPOINT_UNINSTALL_APP + URL_SEPARATOR + appId, new OpenChannelAPIUtil.RequestParameter(USER_ID, openChannelProperties.getUserId())));
        } catch (Exception e) {
            LOGGER.debug("Error while uninstalling app");
            throw new ApplicationOperationException("Failed to uninstall app", e);
        }
    }

    /**
     * Install app
     *
     * @param appId unique app id
     * @param modelId unique model id
     * @return JsonObject
     */
    @Override
    public JSONObject installApp(final String appId, final String modelId){
        try {
            return JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_UNINSTALL_APP + URL_SEPARATOR + appId, OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter(USER_ID, openChannelProperties.getUserId()), new OpenChannelAPIUtil.RequestParameter("modelId", modelId)));
        } catch (Exception e) {
            LOGGER.debug("Error while uninstalling app");
            throw new ApplicationOperationException("Failed to uninstall app", e);
        }
    }

    /**
     * Get app details
     *
     * @param path api path
     * @param type user type
     * @param id   unique user id
     * @return JsonObject
     */
    private JSONObject getAppJsonObject(final String path, final String type, final String id) {
        try {
        return JSONUtil.getJSONObject(openChannelAPIUtil.sendGet(path, new OpenChannelAPIUtil.RequestParameter(type, id)));
        } catch (IOException e) {
            LOGGER.debug("Error while fetching app from openchannel api, Root cause : ", e);
            throw new ApplicationOperationException("Failed to get app details", e);
        }
    }

    /**
     * Search apps
     *
     * @param   queryParameter search parameter
     * @return JsonObject
     */
    @Override
    public JSONObject searchApp(String queryParameter){
        //TODO Need to implement
        return null;
    }

}