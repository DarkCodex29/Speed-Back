package com.hochschild.speed.back.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author HEEDCOM S.A.C.
 * @since 25/02/2019
 */
public class InternationalizationBackend {

    private static final Logger LOG = LoggerFactory.getLogger(InternationalizationBackend.class);
    private static final String PROPERTY_NAME = "messages.label";
    private ResourceBundle bundle;

    public InternationalizationBackend(Locale locale) {
        bundle = ResourceBundle.getBundle(PROPERTY_NAME, locale, new EncodingControl("UTF-8"));
    }

    public InternationalizationBackend() {
        bundle = ResourceBundle.getBundle(PROPERTY_NAME, new EncodingControl("UTF-8"));
    }

    public String getPropertyValue(String propertyKey) {
        String data;
        try {
            data = bundle.getString(propertyKey);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            data = "";
        }
        return data;
    }
}
