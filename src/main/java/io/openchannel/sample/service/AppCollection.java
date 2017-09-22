package io.openchannel.sample.service;

public enum AppCollection {
    MY_APP("myApps"),
    POPULAR_APPS("popular"),
    FEATURED_APPS("featured"),
    ALL_APPS("allApps");

    private String appType;

    AppCollection(String appType){
        this.appType = appType;
    }

    @Override
    public String toString() {
        return appType;
    }
}
