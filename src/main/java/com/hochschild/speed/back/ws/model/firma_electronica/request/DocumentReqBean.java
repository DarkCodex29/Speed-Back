package com.hochschild.speed.back.ws.model.firma_electronica.request;

import lombok.Data;
import java.io.Serializable;

public @Data
class DocumentReqBean implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
    private String name;
    private String base64;
}
