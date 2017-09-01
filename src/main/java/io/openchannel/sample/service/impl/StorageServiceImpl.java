package io.openchannel.sample.service.impl;

import io.openchannel.sample.service.StorageService;
import io.openchannel.sample.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * StorageServiceImpl.java : Implementation of storage service which handles file resource handling
 * <p>
 * Created on 31/8/17 3:41 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Service
public class StorageServiceImpl implements StorageService {
    /**
     * Logger Reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    /**
     * Saves a file to system temporary storage which is a volatile storage in most of the servers
     *
     * @param fileName name of file
     * @param bytes    binary data
     * @return File object
     * @throws IOException throws an IOException
     */
    @Override
    public File save(String fileName, byte[] bytes) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            File file = File.createTempFile(CommonUtil.getFileNameWithoutExt(fileName), CommonUtil.getExtension(fileName));
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (IOException e) {
            LOGGER.warn("Unable to file to temporary location");
            throw e;
        } finally {
            if (!CommonUtil.isNull(fileOutputStream)) {
                fileOutputStream.close();
            }
        }
    }
}
