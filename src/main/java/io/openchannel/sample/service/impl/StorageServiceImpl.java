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
 * Created on 31/8/17 3:41 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Override
    public File save(String fileName, byte[] bytes) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            File file = File.createTempFile(getFileNameWithoutExt(fileName), getExtension(fileName));
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (IOException e) {
            LOGGER.warn("Unable to file to temporary location");
            throw e;
        } finally {
            if(!CommonUtil.isNull(fileOutputStream)) {
                fileOutputStream.close();
            }
        }
    }

    private String getExtension(final String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
    }

    private String getFileNameWithoutExt(final String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.') - 1);
    }
}
