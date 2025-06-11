package com.hochschild.speed.back.ws.model.firma_electronica.request;

import java.util.Map;
import lombok.Data;

/**
 *
 * @author Luciano Carhuaricra - lcarhuaricra@consultorahdc.com
 * @date 26/11/2021
 */
public @Data class PrefilledItemReqBean {

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
    
    private String target;
    
    private Map<String,Object> value;

}

