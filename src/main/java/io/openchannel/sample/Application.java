package io.openchannel.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application.java : Entry point of application which bootstraps Spring boot.
 *
 * Created on 28/8/17 6:28 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

@SpringBootApplication
@ComponentScan("io.openchannel.sample")
public class Application {

    /**
     * Main method, which works as entry point for application
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
