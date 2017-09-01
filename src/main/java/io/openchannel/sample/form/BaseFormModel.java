package io.openchannel.sample.form;

import org.json.simple.JSONObject;

/**
 * Base for form models to convert it into Json as of now
 *
 * Created on 1/9/17 6:10 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */

public interface BaseFormModel {
    JSONObject toJson();
}
