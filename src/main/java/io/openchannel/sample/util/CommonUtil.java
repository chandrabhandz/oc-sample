package io.openchannel.sample.util;

import java.util.Random;

/**
 * CommonUtil.java : Contains common utility method
 */

public class CommonUtil {

    /**
     * Private constructor to hide the implicit one
     */
    private CommonUtil() {
    }

    /**
     * Util method to check if null or not
     *
     * @param o object to check for null
     * @return true/false
     */
    public static Boolean isNull(final Object o) {
        return o == null;
    }

    /**
     * Util method to check if image is base64 encoded
     *
     * @param base64Encoded base64 encoded string
     * @return Boolean
     */
    public static Boolean isBase64EncodedImage(String base64Encoded) {
        if (base64Encoded.length() > 10 && base64Encoded.contains("data:image/"))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    /**
     * Util method to get file extension from a base64 encoded image
     *
     * @param base64Encoded base64 encoded string
     * @return String image extension
     */
    private static String getFileExtFromBase64Encoded(String base64Encoded) {
        return base64Encoded.substring(0, base64Encoded.indexOf(';')).replace("data:image/", "");
    }

    /**
     * Util method to generate a random file name to be used for base64 encoded images
     *
     * @param base64Encoded base64 encoded string
     * @return random file name
     */
    public static String getFileNameFromBase64Encoded(String base64Encoded) {
        return "file" + base64Encoded.length() + new Random().nextInt() + "." + getFileExtFromBase64Encoded(base64Encoded);
    }

    /**
     * Finds extension from file name
     *
     * @param fileName file name
     * @return extension
     */
    public static String getExtension(final String fileName) {
    	
    	int index = fileName.lastIndexOf('.');
    	
    	if(index > 0)
    	{
    		return fileName.substring(index, fileName.length());
    	}
    	else
    	{
    		return "";
    	}
    }

    /**
     * Finds file name without extension from a complete filename
     *
     * @param fileName file name
     * @return extension trimmed file name
     */
    public static String getFileNameWithoutExt(final String fileName) {
    	
    	int index = fileName.lastIndexOf('.');
    	
    	if(index > 0)
    	{
	        String substring = fileName.substring(0, index);
	        if (substring.length() < 3) {
	            substring = substring + "_dz_" + new Random().nextInt();
	        }
	        
	        return substring;
    	}
    	else
    	{
    		return fileName;
    	}
    }

}
