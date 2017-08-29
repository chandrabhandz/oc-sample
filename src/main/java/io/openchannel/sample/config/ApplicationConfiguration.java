package io.openchannel.sample.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationConfiguration.java : Configuration class which defines extended configuration
 *
 * Created on 28/8/17 7:20 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@Configuration
@EnableConfigurationProperties(OpenChannelProperties.class)
public class ApplicationConfiguration {
}
