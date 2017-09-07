package io.openchannel.sample.controller;

import io.openchannel.sample.service.OpenChannelService;
import io.openchannel.sample.service.StorageService;
import io.openchannel.sample.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FilesController.java : Files Controller, which is responsible for providing a common layer to upload files to server/openchannel api
 */

@Controller
@RequestMapping("/files")
public class FilesController {
    /**
     * Logger reference
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);

    /**
     * OpenChannelService for communicating with open channel API
     */
    private OpenChannelService openChannelService;

    /**
     * Storage Service for handling File operations
     */
    private StorageService storageService;

    /**
     * Constructor injection
     *
     * @param openChannelService open channel service reference
     * @param storageService     storage service reference
     */
    @Autowired
    public FilesController(OpenChannelService openChannelService, StorageService storageService) {
        this.openChannelService = openChannelService;
        this.storageService = storageService;
    }

    /**
     * Post Request mapping for uploading a new file to server and later on to openchannel API.
     *
     * @param multipartFile multipartfile request
     * @return File URL or Empty String
     */
    @PostMapping(value = "/upload", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE}, headers = "Accept='**/*'")
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            if (CommonUtil.isBase64EncodedImage(multipartFile.getOriginalFilename())) {
                return openChannelService.uploadFiles(storageService.save(CommonUtil.getFileNameFromBase64Encoded(multipartFile.getOriginalFilename()), multipartFile.getBytes()));
            } else {
                return openChannelService.uploadFiles(storageService.save(multipartFile.getOriginalFilename(), multipartFile.getBytes()));
            }
        } catch (IOException e) {
            LOGGER.warn("Unable to upload file", e);
        }
        return "";
    }

}
