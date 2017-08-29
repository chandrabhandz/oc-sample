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
@ComponentScan({"io.openchannel.sample.config", "io.openchannel.sample.controller"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
