package com.hochschild.speed.back.ws.remote;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class ServiceGenerator {

    private final String wsRoot;
    private final Integer connectTimeout;
    private final Integer readTimeout;

    public ServiceGenerator(String wsRoot, Integer connectTimeout, Integer readTimeout) {
        this.wsRoot = wsRoot;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public <S> S createService(Class<S> serviceClass) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        try {
            // Crea un TrustManager que no valide los certificados
            TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];  // Asegúrate de no devolver null
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    }
                }
            };

            // Inicializa un contexto SSL con el TrustManager personalizado
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Configura el cliente OkHttp para usar el SSLContext sin verificar los certificados
            httpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);

            // Configura el timeout y el interceptor de log
            httpClientBuilder.addInterceptor(interceptor)
                    .connectTimeout(this.connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(this.readTimeout, TimeUnit.SECONDS)
                    .hostnameVerifier((hostname, session) -> true); // Acepta cualquier nombre de host

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient httpClient = httpClientBuilder.build();

        // Configura Retrofit con el cliente OkHttp personalizado
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.wsRoot)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
