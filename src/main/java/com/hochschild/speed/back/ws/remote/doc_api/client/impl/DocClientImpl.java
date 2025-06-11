package com.hochschild.speed.back.ws.remote.doc_api.client.impl;

import com.hochschild.speed.back.config.DocApiConfig;
import com.hochschild.speed.back.ws.remote.ServiceGenerator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hochschild.speed.back.ws.remote.doc_api.client.DocClient;
import com.hochschild.speed.back.ws.remote.doc_api.request.DocRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

@Service
public class DocClientImpl implements DocClient {

    private static final Logger LOGGER = Logger.getLogger(DocClient.class.getName());
    private final ServiceGenerator serviceGenerator;
    @Autowired
    public DocClientImpl(DocApiConfig docApiConfig) {
        this.serviceGenerator = new ServiceGenerator(docApiConfig.getRoot(), docApiConfig.getConnectTimeout(), docApiConfig.getReadTimeout());
    }

    @Override
    public byte[] descargarDocumento(File file) {

        byte[] doc = null;

        DocRequest docRequest = serviceGenerator.createService(DocRequest.class);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));

        Call<ResponseBody> call = docRequest.create(filePart);

        try {
            Response<ResponseBody> responseRetrofit = call.execute();
            if (responseRetrofit.code() == 200) {

                InputStream input = responseRetrofit.body().byteStream();
                doc = IOUtils.toByteArray(input);

            } else {
                LOGGER.info("Error en el servicio");
            }

        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
        }
        return doc;
    }

    @Override
    public byte[] descargarDocumentoAndMerge(File fileA, File fileB) {
        byte[] doc = null;

        DocRequest docRequest = serviceGenerator.createService(DocRequest.class);
        MultipartBody.Part file1 = MultipartBody.Part.createFormData("file1", fileA.getName(),
                RequestBody.create(MediaType.parse("image/*"), fileA));

        MultipartBody.Part file2 = MultipartBody.Part.createFormData("file2", fileB.getName(),
                RequestBody.create(MediaType.parse("image/*"), fileB));

        Call<ResponseBody> call = docRequest.convertToPdfAndMerge(file1, file2);

        try {
            Response<ResponseBody> responseRetrofit = call.execute();
            if (responseRetrofit.code() == 200) {

                InputStream input = responseRetrofit.body().byteStream();
                doc = IOUtils.toByteArray(input);

            } else {
                LOGGER.info("Error en el servicio");
            }

        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
        }
        return doc;
    }
}