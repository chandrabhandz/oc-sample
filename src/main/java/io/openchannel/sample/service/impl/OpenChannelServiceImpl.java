package io.openchannel.sample.service.impl;

import io.openchannel.sample.config.OpenChannelProperties;
import io.openchannel.sample.service.OpenChannelService;
import io.openchannel.sample.util.JSONUtil;
import io.openchannel.sample.util.OpenChannelAPIUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
