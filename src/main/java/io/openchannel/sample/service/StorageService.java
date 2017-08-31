package io.openchannel.sample.service;

import java.io.File;
import java.io.IOException;

/**
 * Created on 31/8/17 3:40 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public interface StorageService {
    File save(final String fileName, final byte[] bytes) throws IOException;
}
