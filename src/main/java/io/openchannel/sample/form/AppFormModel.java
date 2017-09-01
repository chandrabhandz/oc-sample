package io.openchannel.sample.form;

/**
 * AppFormModel.java : FormModel wrapper for an App which defines basic properties of an app which are visible to a user/other component of application
 * Created on 31/8/17 1:23 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public class AppFormModel {
    private String applicationName;
    private String summary;
    private String description;
    private String icon;
    private String images;
    private String files;
    private String category;
    private String websiteUrl;
    private String videoUrl;
    private String tncFlag;

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTncFlag() {
        return tncFlag;
    }

    public void setTncFlag(String tncFlag) {
        this.tncFlag = tncFlag;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "AppFormModel{" +
                "applicationName='" + applicationName + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", images='" + images + '\'' +
                ", files='" + files + '\'' +
                ", category='" + category + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", tncFlag='" + tncFlag + '\'' +
                '}';
    }
}
