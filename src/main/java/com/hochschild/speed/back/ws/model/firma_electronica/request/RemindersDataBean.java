package com.hochschild.speed.back.ws.model.firma_electronica.request;

import lombok.Data;

/**
 *
 * @author Luciano Carhuaricra - lcarhuaricra@consultorahdc.com
 * @date 11/03/2021
 */
public @Data
class RemindersDataBean {

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    private Integer frequency;
    private Integer maxAttempts;
}
