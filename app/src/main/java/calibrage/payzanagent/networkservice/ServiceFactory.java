package calibrage.payzanagent.networkservice;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import calibrage.payzanagent.BuildConfig;
import calibrage.payzanagent.utils.SharedPrefsData;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory  {

    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     *
     * @param clazz Java interface of the retrofit service
     * @return retrofit service with defined endpoint
     */
    public static <T> T createRetrofitService(Context context, final Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl(BuildConfig.LOCAL_URL)
                .client(getHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

 /*   public static <T> T createRetrofitServiceForPaypal(Context context, final Class<T> clazz) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.PAYPAL_URL)
                .client(getHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        return retrofit.create(clazz);
    }*/
    private static OkHttpClient getHttpClient(final Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        //Enable log in debug mode
        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
//            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            logInterceptor.addIntercept(logInterceptor);
            CustomResponseInterceptor  customResponseInterceptor = new CustomResponseInterceptor(context);

            httpClient.addInterceptor(customResponseInterceptor).build();
        }
//        if (!TextUtils.isEmpty(authToken)) {
//            AuthenticationInterceptor interceptor =
//                    new AuthenticationInterceptor(authToken);
//
//            if (!httpClient.interceptors().contains(interceptor)) {
//                httpClient.addInterceptor(interceptor);
//
//                builder.client(httpClient.build());
//                retrofit = builder.build();
//            }
//        }

        return httpClient.build();
    }

//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        Response response = chain.proceed(request);
//        if (response.code() != 200) {
//            Response r = null;
//            try { r = makeTokenRefreshCall(request, chain); }
//            catch (JSONException e) { e.printStackTrace(); }
//            return r;
//        }
//        Log.d("", "INTERCEPTED:$ "+response.toString());
//        return response;
//    }
//
//    private Response makeTokenRefreshCall(Request req, Chain chain) throws JSONException, IOException {
//        Log.d("", "Retrying new request");
//        /* fetch refreshed token, some synchronous API call, whatever */
//        String newToken = SharedPrefsData.getInstance();
//        /* make a new request which is same as the original one, except that its headers now contain a refreshed token */
//        Request newRequest;
//        newRequest = req.newBuilder().header("Authorization", "Bearer " + newToken).build();
//        Response another =  chain.proceed(newRequest);
//        while (another.code() != 200) {
//            makeTokenRefreshCall(newRequest, chain);
//        }
//        return another;
//    }
}
