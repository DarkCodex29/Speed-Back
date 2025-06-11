package com.hochschild.speed.back.ws.remote.firma_electronica.request;

import java.util.Map;
import com.hochschild.speed.back.ws.model.firma_electronica.request.CreateContratoReqBean;
import com.hochschild.speed.back.ws.model.firma_electronica.response.CreateContratoResBean;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContratoRequest {

    @POST("contrato/create")
    Call<CreateContratoResBean> create(@Body CreateContratoReqBean createContratoReqBean);
    
    @DELETE("contrato/{id}")
    Call<CreateContratoResBean> delete(@Path("id") String id);
    
    @GET("contrato/{id}")
    Call<Map<String,Object>> get(@Path("id") String id);
    
}
