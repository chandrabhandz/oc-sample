package io.openchannel.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application.java : Entry point of application which bootstraps Spring boot.
 */

@SpringBootApplication
@ComponentScan("io.openchannel.sample")
public class Application {

    /**
     * Main method, which works as entry point for application
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
