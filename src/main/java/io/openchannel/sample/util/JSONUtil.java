package io.openchannel.sample.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * JSONUtil.java : Json util for application
 *
 * Created on 30/8/17 4:47 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public class JSONUtil {

    /**
     * Private constructor to hide the implicit one
     */
    private JSONUtil() {
    }

    /**
     * Gets the JSON ARRAY from given string
     *
     * @param object
     * @return
     */
    public static JSONArray getJSONArray(Object object) {
        JSONParser jsonParser = new JSONParser();
        try {
            return (JSONArray) jsonParser.parse(object.toString());
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse given Object", e);
        }
    }

    /**
     * Gets the JSON OBJECT from the given string
     *
     * @param object
     * @return
     */
    public static JSONObject getJSONObject(Object object) {
        JSONParser jsonParser = new JSONParser();
        try {
            return (JSONObject) jsonParser.parse(object.toString());
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse given Object", e);
        }
    }
}
