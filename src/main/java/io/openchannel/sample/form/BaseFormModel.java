package io.openchannel.sample.form;

import org.json.simple.JSONObject;

/**
 * Base for form models to convert it into Json as of now
 */

public interface BaseFormModel {
    JSONObject toJson();
}
