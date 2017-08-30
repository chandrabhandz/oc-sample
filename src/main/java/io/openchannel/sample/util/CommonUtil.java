package io.openchannel.sample.util;

/**
 * Created on 30/8/17 2:30 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public class CommonUtil {

    /**
     * Private constructor to hide the implicit one
     */
    private CommonUtil() {
    }

    /**
     * Util method to check if null or not
     * @param o object to check for null
     * @return true/false
     */
    public static Boolean isNull(final Object o) {
        return o == null;
    }
}
