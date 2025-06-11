package com.hochschild.speed.back.ws.remote.firma_electronica.client.impl;

import com.hochschild.speed.back.config.FirmaElectronicaConfig;
import com.hochschild.speed.back.ws.model.firma_electronica.request.CreateContratoReqBean;
import com.hochschild.speed.back.ws.model.firma_electronica.response.CreateContratoResBean;
import com.hochschild.speed.back.ws.remote.ServiceGenerator;
import com.hochschild.speed.back.ws.remote.firma_electronica.client.ContratoClient;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hochschild.speed.back.ws.remote.firma_electronica.request.ContratoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

@Service
public class ContratoClientImpl implements ContratoClient {

    private static final Logger LOGGER = Logger.getLogger(ContratoClient.class.getName());
    private final ServiceGenerator serviceGenerator;

    @Autowired
    public ContratoClientImpl(FirmaElectronicaConfig firmaElectronicaConfig) {
        this.serviceGenerator = new ServiceGenerator(firmaElectronicaConfig.getRoot(), firmaElectronicaConfig.getConnectTimeout(), firmaElectronicaConfig.getReadTimeout());
    }

    @Override
    public int create(CreateContratoReqBean createContratoReqBean) {
        try {
            ContratoRequest contratoRequest = serviceGenerator.createService(ContratoRequest.class);
            Call<CreateContratoResBean> call = contratoRequest.create(createContratoReqBean);
            Response<CreateContratoResBean> responseRetrofit = call.execute();
            return responseRetrofit.code();
        } catch (SocketTimeoutException e) {
            return 200;
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
            return -1;
        }
    }

    @Override
    public int delete(String id) {
        try {
            ContratoRequest contratoRequest = serviceGenerator.createService(ContratoRequest.class);
            Call<CreateContratoResBean> call = contratoRequest.delete(id);
            Response<CreateContratoResBean> responseRetrofit = call.execute();
            return responseRetrofit.code();
        } catch (SocketTimeoutException e) {
            return 200;
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
            return -1;
        }
    }

    @Override
    public Map<String, Object> get(String id) {
        Map<String, Object> result;
        try {
            result = new HashMap<>();
            ContratoRequest contratoRequest = serviceGenerator.createService(ContratoRequest.class);
            Call<Map<String, Object>> call = contratoRequest.get(id);
            Response<Map<String, Object>> responseRetrofit = call.execute();
            switch (responseRetrofit.code()) {
                case 200:
                    result = responseRetrofit.body();
                    break;
                default:
                    result.put("serverCode", responseRetrofit.code());
            }
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            result = new HashMap<>();
            result.put("serverCode", 500);
            return result;
        }
    }
}