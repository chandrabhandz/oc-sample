package io.openchannel.sample.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationConfiguration.java : Configuration class which defines extended configuration
 */

@Configuration
@EnableConfigurationProperties(OpenChannelProperties.class)
public class ApplicationConfiguration {
}
