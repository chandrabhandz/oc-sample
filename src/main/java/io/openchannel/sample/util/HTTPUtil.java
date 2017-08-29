package io.openchannel.sample.util;

import io.openchannel.sample.config.OpenChannelProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * HTTPUtil.java : Responsible for make HTTP Calls to external api
 * <p>
 * Created on 29/8/17 6:22 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Service
public class HTTPUtil {

    private OpenChannelProperties openChannelProperties;

    @Autowired
    public HTTPUtil(final OpenChannelProperties openChannelProperties) {
        this.openChannelProperties = openChannelProperties;
    }

    public void sendGet(final String path) {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet httpGet = new HttpGet(buildURI(path));

    }

    private String buildURI(final String path) {
        return getURIFormatted(openChannelProperties.getApiUrl()) + getURIFormatted(openChannelProperties.getApiVersion()) + getURIFormatted(path);
    }

    private String getURIFormatted(final String path) {
        return path.endsWith("/") ? path : path + "/";
    }

    private void buildHeaders(HttpEntityEnclosingRequestBase requestBase) {
        requestBase.addHeader(new BasicHeader("Content-type", "application/json"));
        requestBase.addHeader(new BasicHeader("Authorization", "Basic " + encodeB64(openChannelProperties.getMarketplaceId() + ":" + openChannelProperties.getSecret())));
    }

    private String encodeB64(final String text) {
        return String.valueOf(Base64.getEncoder().encode(text.getBytes()));
    }

    private HttpClient getHttpClient() {
        HttpClient client = null;
        if (openChannelProperties.getSslEnabled()) {


        } else {
            client = HttpClientBuilder.create().build();
        }

        return client;
    }

}
