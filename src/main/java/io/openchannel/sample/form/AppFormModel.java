package io.openchannel.sample.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openchannel.sample.exception.ApplicationOperationException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * AppFormModel.java : FormModel wrapper for an App which defines basic properties of an app which are visible to a user/other component of application
 */

public class AppFormModel implements BaseFormModel, Serializable {

    @JsonIgnore
    private String appId;
    @JsonIgnore
    private String name;
    private String summary;
    private String description;
    private String icon;
    private List<String> images;
    private List<String> files;
    private List<String> category;
    private String website;
    private String video;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private String tncFlag;
    @JsonIgnore
    private String publish;
    @JsonIgnore
    private String version;

    public static AppFormModel fromJson(final String json) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(json, AppFormModel.class);
        } catch (IOException e) {
            throw new ApplicationOperationException("Can't convert from json", e);
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTncFlag() {
        return tncFlag;
    }

    public void setTncFlag(String tncFlag) {
        this.tncFlag = tncFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getPublish() {
        return Boolean.valueOf(publish);
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    @Override
    public JSONObject toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return (JSONObject) new JSONParser().parse(objectMapper.writeValueAsString(this));
        } catch (JsonProcessingException | ParseException e) {
            throw new ApplicationOperationException("Can't convert to json", e);
        }
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
