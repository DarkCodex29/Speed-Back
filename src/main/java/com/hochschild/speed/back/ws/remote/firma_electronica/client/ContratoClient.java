package com.hochschild.speed.back.ws.remote.firma_electronica.client;

import com.hochschild.speed.back.ws.model.firma_electronica.request.CreateContratoReqBean;
import java.util.Map;

/**
 *
 * @author Luciano Carhuaricra - lcarhuaricra@consultorahdc.com
 * @date 04/08/2020
 */
public interface ContratoClient {
    
    int create(CreateContratoReqBean createContratoReqBean);
    
    int delete(String id);
    
    Map<String,Object> get(String id);

}

