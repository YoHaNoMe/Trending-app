package com.yohanome.moviediscussion.movieutils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class NetworkUtils {

    public final static String BASE_URL = "https://api.themoviedb.org/3/";
    public final static String SERVER_URL = "http://10.0.2.2:5000/";
    public final static String API_QUERY = "api_key";
    // TODO: Replace with your API KEY
    public final static String API_KEY = "";
    public final static String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";


    public static OkHttpClient attachApiKeyToEveryRequest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder().addQueryParameter(NetworkUtils.API_QUERY, NetworkUtils.API_KEY).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                }).build();
        return client;
    }

    public static String generateImageUrl(String imageUrl){
        return BASE_IMAGE_URL+imageUrl;
    }

}
