package com.isel.sincroapp.util;

import android.content.Context;
import android.util.Log;

import com.isel.sincroapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {
    public static SSLContext getSSLConfig(Context context) {
        try {
            /*CertificateFactory cf = null;
            cf = CertificateFactory.getInstance("PKCS12");

            Certificate ca;
            try (InputStream cert = context.getResources().openRawResource(R.raw.sincroserver)) {
                ca = cf.generateCertificate(cert);
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext;*/
            KeyStore clientStore = KeyStore.getInstance("BKS");
            try (InputStream cert = context.getResources().openRawResource(R.raw.sincroserver)) {
                clientStore.load(cert, "sincroserver".toCharArray());
            }

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            tmf.init(clientStore);

            sc.init(null, tmf.getTrustManagers(),null);

            return sc;
        } catch (Exception e) {
            Log.e("SINCRO_APP", "I got an error", e);
        }

        return null;
    }

    public static Retrofit getRetrofit(Context context) {
        OkHttpClient okHttp = new OkHttpClient.Builder()
                .sslSocketFactory(Utils.getSSLConfig(context).getSocketFactory())
                .hostnameVerifier(new HostnameVerifier(){
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }})
                .build();

        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_url))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build();
    }

    public static Retrofit getRetrofit(Context context, final String token) {
        OkHttpClient okHttp = new OkHttpClient.Builder()
                .sslSocketFactory(Utils.getSSLConfig(context).getSocketFactory())
                .hostnameVerifier(new HostnameVerifier(){
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }})
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization", token).build();
                        return chain.proceed(request);
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_url))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build();
    }
}
