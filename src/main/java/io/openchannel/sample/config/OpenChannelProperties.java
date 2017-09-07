package io.openchannel.sample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

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
}
