package io.openchannel.sample.util;

import io.openchannel.sample.config.OpenChannelProperties;
import io.openchannel.sample.form.BaseFormModel;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
public class OpenChannelAPIUtil {
    /**
     * Application Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenChannelAPIUtil.class);

    /**
     * Empty string reference
     */
    private static final String EMPTY_STRING = "";

    /**
     * OpenChannelProperties loaded by spring boot
     */
    private OpenChannelProperties openChannelProperties;

    /**
     * Constructor injection for dependencies
     *
     * @param openChannelProperties open channel properties loaded by spring
     */
    @Autowired
    public OpenChannelAPIUtil(final OpenChannelProperties openChannelProperties) {
        this.openChannelProperties = openChannelProperties;
    }

    /**
     * Send a GET request to specified API url and gets response as String
     *
     * @param path              API path to be invoked
     * @param requestParameters list of request parameters
     * @return response returned from API
     * @throws IOException throws an IOException if request can't be completed
     */
    public String sendGet(final String path, final RequestParameter... requestParameters) throws IOException {
        final CloseableHttpClient httpClient = getHttpClient();
        final HttpGet httpGet = new HttpGet(buildURI(path) + "?" + buildGetRequestParams(requestParameters));
        try {
            httpGet.addHeader(new BasicHeader("Content-Type", "application/json"));
            return getString(httpGet, httpClient);
        } catch (IOException e) {
            LOGGER.warn("unable to complete request {}", e.getMessage());
            throw e;
        } finally {
            try {
                httpGet.releaseConnection();
                httpClient.close();
            } catch (IOException e) {
                LOGGER.warn("unable to close HTTP resources {}", e.getMessage());
            }
        }
    }

    /**
     * Send a POST request to specified API url and gets response as String
     *
     * @param path              API path to be invoked
     * @param postContentType   ContentType of request (Multipart/JSON)
     * @param requestParameters list of request parameters
     * @return response returned from API
     * @throws IOException throws an IOException if request can't be completed
     */
    public String sendPost(final String path, final PostContentType postContentType, final RequestParameter... requestParameters) throws IOException {
        final CloseableHttpClient httpClient = getHttpClient();
        final HttpPost httpPost = new HttpPost(buildURI(path));
        try {
            if (PostContentType.MULTIPART.equals(postContentType))
                httpPost.setEntity(buildMultipartRequestParams(requestParameters));
            else
                httpPost.setEntity(buildJsonRequestParams(requestParameters));
            return getString(httpPost, httpClient);
        } catch (IOException e) {
            LOGGER.warn("unable to complete request {}", e.getMessage());
            throw e;
        } finally {
            try {
                httpPost.releaseConnection();
                httpClient.close();
            } catch (IOException e) {
                LOGGER.warn("unable to close HTTP resources {}", e.getMessage());
            }
        }
    }

    /**
     * Send a DELETE request to specified API url and gets response as String
     *
     * @param path              API path to be invoked
     * @param requestParameters list of request parameters
     * @return response returned from API
     * @throws IOException throws an IOException if request can't be completed
     */
    public String sendDelete(final String path, final RequestParameter... requestParameters) throws IOException {
        final CloseableHttpClient httpClient = getHttpClient();
        final HttpDelete httpDelete = new HttpDelete(buildURI(path) + "?" + buildGetRequestParams(requestParameters));
        try {
            return getString(httpDelete, httpClient);
        } catch (IOException e) {
            LOGGER.warn("unable to complete request {}", e.getMessage());
            throw e;
        } finally {
            try {
                httpDelete.releaseConnection();
                httpClient.close();
            } catch (IOException e) {
                LOGGER.warn("unable to close HTTP resources {}", e.getMessage());
            }
        }
    }

    /**
     * builds URIEncoded Request parameter
     *
     * @param requestParameters list of request parameters
     * @return URL Encoded string
     */
    private String buildGetRequestParams(final RequestParameter... requestParameters) {
        if (requestParameters.length == 0)
            return EMPTY_STRING;
        final StringBuilder stringBuilder = new StringBuilder();

        try {
            stringBuilder.append(requestParameters[0].getEncoded());
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Unable to send request parameter {}", requestParameters[0].toString());
        }


        for (int i = 1; i < requestParameters.length; i++) {
            try {
                stringBuilder.append("&").append(requestParameters[i].getEncoded());
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn("Unable to send request parameter {}", requestParameters[i].toString());
            }
        }

        return stringBuilder.toString();

    }

    /**
     * builds multipart form entity for multipart requests
     *
     * @param requestParameters list of request parameters
     * @return Multipart Entity
     */
    private HttpEntity buildMultipartRequestParams(final RequestParameter... requestParameters) {
        final MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (int i = 0; i < requestParameters.length; i++) {
            if (requestParameters[i].getValue() instanceof String) {
                multipartEntityBuilder.addTextBody(requestParameters[i].getName(), String.valueOf(requestParameters[i].getValue()));
            } else if (requestParameters[i].getValue() instanceof File) {
                multipartEntityBuilder.addBinaryBody(requestParameters[i].getName(), (File) requestParameters[i].getValue(), ContentType.APPLICATION_OCTET_STREAM, ((File) requestParameters[i].getValue()).getName());
            } else {
                throw new UnsupportedOperationException("multipart other than String & File is not supported at this moment");
            }
        }

        return multipartEntityBuilder.build();
    }

    /**
     * builds json request entity
     *
     * @param requestParameters list of reuqest parameters
     * @return StringEntity
     */
    private HttpEntity buildJsonRequestParams(final RequestParameter... requestParameters) {
        final JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < requestParameters.length; i++) {
            if (requestParameters[i].getValue() instanceof BaseFormModel) {
                jsonObject.put(requestParameters[i].getName(), ((BaseFormModel) requestParameters[i].getValue()).toJson());
            } else {
                jsonObject.put(requestParameters[i].getName(), requestParameters[i].getValue());
            }
        }
        return new StringEntity(jsonObject.toJSONString(), ContentType.APPLICATION_JSON);
    }

    /**
     * @param requestBase Generic request base
     * @param httpClient  HttpClient object
     * @return HttpEntity reference to be consumed by invoking methods
     * @throws IOException throws an IOException if request can't be completed
     */
    private HttpEntity send(final HttpRequestBase requestBase, final CloseableHttpClient httpClient) throws IOException {
        buildHeaders(requestBase);
        CloseableHttpResponse httpResponse = httpClient.execute(requestBase);
        return httpResponse.getEntity();
    }

    /**
     * Send a HTTPRequest and return response in String
     *
     * @param requestBase Generic HTTP Request
     * @param httpClient  HttpClient object
     * @return Response converted to string
     * @throws IOException throws an IOException if request can't be completed
     */
    private String getString(final HttpRequestBase requestBase, final CloseableHttpClient httpClient) throws IOException {
        return EntityUtils.toString(send(requestBase, httpClient));
    }

    /**
     * Builds URI for API Communication
     *
     * @param path API Path to be used
     * @return complete URI
     */
    private String buildURI(final String path) {
        return getURIFormatted(openChannelProperties.getApiUrl()) + getURIFormatted(openChannelProperties.getApiVersion()) + getURIFormatted(path);
    }

    /**
     * Get formatted URI to lower down malformed URI errors
     *
     * @param path API Path to be used
     * @return formatted URI
     */
    private String getURIFormatted(final String path) {
        return path.endsWith("/") ? path : path + "/";
    }

    /**
     * Builds required header to be used for communication
     *
     * @param requestBase Generic HTTP Request
     */
    private void buildHeaders(HttpRequestBase requestBase) {
        String b64 = encodeB64(openChannelProperties.getMarketplaceId() + ":" + openChannelProperties.getSecret());
        requestBase.addHeader(new BasicHeader("Authorization", "Basic " + b64));
    }

    /**
     * Base64 Encode util method
     *
     * @param text text to be encoded
     * @return encoded base64 string
     */
    private String encodeB64(final String text) {
        return String.valueOf(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Creates a HTTPClient based on ssl flag,
     * return SSL Enabled HTTPClient if flag is on
     * otherwise, simple HTTPClient
     *
     * @return CloseableHTTPClient reference
     * @throws IOException throws an IOException if HTTPClient can't be created
     */
    private CloseableHttpClient getHttpClient() throws IOException {
        CloseableHttpClient client;
        if (openChannelProperties.getSslEnabled()) {
            try {
                SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
                HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
                SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
                client = HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                throw new IOException("Unable to create SSL Enabled HTTP Client");
            }
        } else {
            client = HttpClientBuilder.create().build();
        }

        return client;
    }

    /**
     * Enum for Post Request Content type
     */
    public enum PostContentType {
        MULTIPART, JSON
    }

    /**
     * RequestParameter class : wrapper for API Request parameters
     */
    public static class RequestParameter {

        /**
         * Name of parameter
         */
        private final String name;

        /**
         * value of parameter
         */
        private final Object value;

        /**
         * Simple Name-Value pair constructor
         *
         * @param name
         * @param value
         */
        public RequestParameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Getter for name
         *
         * @return
         */
        private String getName() {
            return name;
        }

        /**
         * Getter for string value
         *
         * @return
         */
        private Object getValue() {
            return value;
        }

        /**
         * Gets encoded url parameter which can be used in GET request
         *
         * @return encoded url parameter
         * @throws UnsupportedEncodingException
         */
        public String getEncoded() throws UnsupportedEncodingException {
            try {
                return getName() + "=" + URLEncoder.encode(String.valueOf(getValue()), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn("Unable to encode request parameters");
                throw e;
            }
        }

        @Override
        public String toString() {
            return "RequestParameter{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

}
