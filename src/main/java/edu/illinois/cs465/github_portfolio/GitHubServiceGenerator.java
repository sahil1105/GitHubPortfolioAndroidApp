package edu.illinois.cs465.github_portfolio;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * GitHubServiceGenerator Class :- Class used to represent an abstract service that
 * makes API calls and returns data as an instance of the specified class.
 * Reference: http://www.baeldung.com/retrofit
 * @author sahil1105
 */
public class GitHubServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    //Builder with a converter type of JSON.
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    //HTTP client to use to make the actual calls to the API
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    /**
     * @param serviceClass The Interface to use to make the API calls.
     * @param <S> The type class.
     * @return A Service specific to the request, created using retrofit.
     */
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
