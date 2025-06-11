package com.hochschild.speed.back.ws.remote.doc_api.request;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

public interface DocRequest {

    @Multipart
    @POST("doc/convertToPdf")
    @Streaming
    Call<ResponseBody> create(@Part MultipartBody.Part filePart);
    
    @Multipart
    @POST("doc/convertToPdfAndMerge")
    @Streaming
    Call<ResponseBody> convertToPdfAndMerge(@Part MultipartBody.Part filePartA, @Part MultipartBody.Part filePartB);
}
