package io.openchannel.sample.controller;

import io.openchannel.sample.service.OpenChannelService;
import io.openchannel.sample.service.StorageService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created on 31/8/17 3:00 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Controller
@RequestMapping("/files")
public class FilesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);

    private OpenChannelService openChannelService;
    private StorageService storageService;

    @Autowired
    public FilesController(OpenChannelService openChannelService, StorageService storageService) {
        this.openChannelService = openChannelService;
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return openChannelService.uploadFiles(storageService.save(multipartFile.getName(), multipartFile.getBytes()));
        } catch (IOException e) {
            LOGGER.warn("Unable to upload file", e);
        }
        return "";
    }

}
