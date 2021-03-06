package io.openchannel.sample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;

/**
 * OpenChannelProperties.java : works as loaded "openchannel" properties from application.yml to be
 * available within application for further usage
 */

@ConfigurationProperties(prefix = "openchannel")
@Validated
public class OpenChannelProperties {
    /**
     * Marketplace id, obtained from openchannel console > settings > credentials
     * Validated with NotNull i.e. compulsory field
     */
    @NotNull
    private String marketplaceId;

    /**
     * marketplace secret, obtained from openchannel console > settings > credentials
     * Validated with NotNull i.e. compulsory field
     */
    @NotNull
    private String secret;

    /**
     * DeveloperID
     * Validated with NotNull
     */
    @NotNull
    private String developerId;

    /**
     * Marketplace API url, obtained from openchannel console > settings > credentials
     * Validated with NotNull i.e. compulsory field
     */
    @NotNull
    private String apiUrl;

    /**
     * OpenChannel API Version
     * Validated with NotNull
     */
    @NotNull
    private String apiVersion;

    /**
     * SSL flag for api url
     */
    private Boolean sslEnabled = Boolean.FALSE;

    /**
     * userID
     * Validated with NotNull
     */
    @NotNull
    private String userId;

    /**
     * Getter for ssl flag
     *
     * @return
     */
    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    /**
     * Setter for ssl flag
     *
     * @param sslEnabled
     */
    public void setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    /**
     * Getter for marketplaceid
     *
     * @return
     */
    public String getMarketplaceId() {
        if(marketplaceId.equals("")) {
            throw new InvalidParameterException("MarketplaceId must be set with your marketplace API credentials");
        }
        return marketplaceId;
    }

    /**
     * Setter for marketplaceid
     *
     * @param marketplaceId openchannel unique marketplace id
     */
    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    /**
     * Getter for secret
     *
     * @return
     */
    public String getSecret() {
        if(secret.equals("")) {
            throw new InvalidParameterException("Marketplace secret must be set with your marketplace API credentials");
        }
        return secret;
    }

    /**
     * Setter for secret
     *
     * @param secret openchannel API secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Getter for api url
     *
     * @return
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * Setter for api url
     *
     * @param apiUrl Marketplace API url
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * Getter for developer id
     *
     * @return
     */
    public String getDeveloperId() {
        return developerId;
    }

    /**
     * Setter for developer id
     *
     * @param developerId developer id to be used
     */
    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    /**
     * Getter for API Version
     *
     * @return
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Setter for API Version
     *
     * @param apiVersion api version string
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Getter for user id
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter for user id
     *
     * @param userId user id to be used
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
