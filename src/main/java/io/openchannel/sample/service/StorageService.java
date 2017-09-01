package io.openchannel.sample.service;

import java.io.File;
import java.io.IOException;

/**
 * StorageService.java : Interface which defines abstract layer for Storage functionality
 *
 * Created on 31/8/17 3:40 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public interface StorageService {
    /**
     *  concrete body to provide file saving mechanism
     * @param fileName name of file
     * @param bytes binary content
     * @return saved file
     * @throws IOException can throw IOException
     */
    File save(final String fileName, final byte[] bytes) throws IOException;
}
