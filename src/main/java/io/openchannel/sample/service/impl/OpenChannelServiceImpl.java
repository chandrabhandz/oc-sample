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
 * <p>
 * Created on 29/8/17 5:36 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
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
     * search for apps using loaded developer id
     *
     * @return JsonObject which contains data returned from API
     */
    @Override
    public JSONObject searchApps() {
        try {
            return JSONUtil.getJSONObject(openChannelAPIUtil.sendGet(ENDPOINT_APPS, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("query", "{$or:[{\"status.value\":\"rejected\",isLatestVersion:true},{isLive:true},{\"status.value\":{$in:[\"inDevelopment\",\"inReview\",\"pending\"]}}]}")));
        } catch (IOException e) {
            LOGGER.warn("Error while communicating with openchannel search api", e);
        }
        return new JSONObject();
    }

    /**
     * get stats based on developer id
     *
     * @return
     */
    @Override
    public JSONArray getStatistics() {
        try {
            return JSONUtil.getJSONArray(openChannelAPIUtil.sendGet(ENDPOINT_STATS, new OpenChannelAPIUtil.RequestParameter("query", "{developerId: '" + openChannelProperties.getDeveloperId() + "'}")));
        } catch (IOException e) {
            LOGGER.warn("Error while communicating with openchannel stats api", e);
        }
        return new JSONArray();
    }

    /**
     * Get stats based on developerid & appid
     * @param appId unique app id
     * @return api response
     */
    @Override
    public JSONArray getStatistics(final String appId) {
        try {
            return JSONUtil.getJSONArray(openChannelAPIUtil.sendGet(ENDPOINT_STATS, new OpenChannelAPIUtil.RequestParameter("query", "{developerId: '" + openChannelProperties.getDeveloperId() + "', appId: '" + appId + "'}")));
        } catch (IOException e) {
            LOGGER.warn("Error while communicating with openchannel stats api", e);
        }
        return new JSONArray();
    }



    /**
     * Uploads a file to openchannel API and gets information about uploaded file
     *
     * @param content
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
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP, OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("name", appFormModel.getName()), new OpenChannelAPIUtil.RequestParameter("customData", appFormModel)));
            if (CommonUtil.isNull(apiResponse.get("errors")) && appFormModel.getPublish()) {
                appFormModel.setAppId(String.valueOf(apiResponse.get("appId")));
                appFormModel.setVersion(String.valueOf(apiResponse.get("version")));
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
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP + "/" + appFormModel.getAppId() + "/" + "publish", OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("version", Integer.parseInt(appFormModel.getVersion()))));
            if (!CommonUtil.isNull(apiResponse.get("errors"))) {
                JSONArray errors = (JSONArray) apiResponse.get("errors");
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get("message")));
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
            String finalPath = ENDPOINT_CREATE_APP + "/" + appId;
            if (!"".equals(version)) {
                finalPath = finalPath + "/versions/" + version;
            }
            return JSONUtil.getJSONObject(openChannelAPIUtil.sendDelete(finalPath, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId())));
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
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP + "/" + appFormModel.getAppId() + "/versions/" + appFormModel.getVersion(), OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("name", appFormModel.getName()), new OpenChannelAPIUtil.RequestParameter("customData", appFormModel)));

            if(!CommonUtil.isNull(apiResponse.get("errors"))) {
                JSONArray errors = (JSONArray) apiResponse.get("errors");
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get("message")));
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
    public AppFormModel getApp(final String appId, final String version) throws ApplicationOperationException {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendGet(ENDPOINT_CREATE_APP + "/" + appId + "/versions/" + version, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId())));
            if (CommonUtil.isNull(apiResponse.get("appId")) && !CommonUtil.isNull(apiResponse.get("errors"))) {
                JSONArray errors = (JSONArray) apiResponse.get("errors");
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get("message")));
            }
            AppFormModel appFormModel = AppFormModel.fromJson(((JSONObject) apiResponse.get("customData")).toJSONString());
            appFormModel.setAppId(String.valueOf(apiResponse.get("appId")));
            appFormModel.setVersion(String.valueOf(apiResponse.get("version")));
            return appFormModel;
        } catch (IOException e) {
            LOGGER.debug("Error while fetching app from openchannel api, Root cause : ", e);
            LOGGER.warn("Error while updating app to openchannel api");
            throw new ApplicationOperationException("Failed to get app details", e);
        }
    }

    /**
     * Change app status
     *
     * @param appId   unique app id
     * @param version app version
     * @return JsonObject with all available details
     * @throws ApplicationOperationException can be thrown for errors
     */
    @Override
    public JSONObject changeAppStatus(final String appId, final String status) throws ApplicationOperationException {
        try {
            JSONObject apiResponse = JSONUtil.getJSONObject(openChannelAPIUtil.sendPost(ENDPOINT_CREATE_APP + "/" + appId + "/status", OpenChannelAPIUtil.PostContentType.JSON, new OpenChannelAPIUtil.RequestParameter("developerId", openChannelProperties.getDeveloperId()), new OpenChannelAPIUtil.RequestParameter("status", status)));
            if (!CommonUtil.isNull(apiResponse.get("errors"))) {
                JSONArray errors = (JSONArray) apiResponse.get("errors");
                throw new ApplicationOperationException(String.valueOf(((JSONObject) errors.get(0)).get("message")));
            }
            return apiResponse;
        } catch (IOException e) {
            LOGGER.debug("Error while changing app status in openchannel api, Root cause : ", e);
            LOGGER.warn("Error while changing app status in openchannel api");
            throw new ApplicationOperationException("Failed to change app status", e);
        }
    }

}
