package com.hochschild.speed.back.ws.model.firma_electronica.request;

import lombok.Data;

/**
 *
 * @author Luciano Carhuaricra - lcarhuaricra@consultorahdc.com
 * @date 11/03/2021
 */
public @Data
class FlagsBean {

    public RemindersDataBean getRemindersData() {
        return remindersData;
    }

    public void setRemindersData(RemindersDataBean remindersData) {
        this.remindersData = remindersData;
    }
    private RemindersDataBean remindersData;
}
