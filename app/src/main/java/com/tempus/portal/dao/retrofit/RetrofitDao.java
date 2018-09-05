package com.tempus.portal.dao.retrofit;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.tempus.portal.base.utils.NetworkConfig;
import com.tempus.portal.dao.ApiService;
import com.tempus.portal.dao.retrofit.convert.MyGsonConverterFactory;
import com.tempus.portal.dao.retrofit.convert.NullStringToEmptyAdapterFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * <Retrofit结合OKHttp实现>
 * <p>
 * 关于OkHttp的Interceptor：https://github.com/square/okhttp/wiki/Interceptors
 * <p>
 * 缓存相关： http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0115/3873.html
 * <p>
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0131/3930.html
 *
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */

public class RetrofitDao {
    private static RetrofitDao ourInstance;
    private static final String TAG = RetrofitDao.class.getSimpleName();

    private Retrofit mRetrofit;

    private OkHttpClient okHttpClient;

    private static ApiService mApiService;


    // 单例写法---双重检查锁模式
    public static RetrofitDao getInstance() {
        if (ourInstance == null) {
            synchronized (RetrofitDao.class) {
                if (ourInstance == null) ourInstance = new RetrofitDao();
            }
        }
        return ourInstance;
    }


    private RetrofitDao() {
        if (NetworkConfig.getBaseUrl() == null ||
                NetworkConfig.getBaseUrl().trim().equals("")) {
            throw new RuntimeException(
                    "网络模块必须设置在Application处调用 请求的地址 调用方法：NetworkConfig.setBaseUrl(String url)");
        }

        if (mRetrofit == null) {
            okHttpClient = new OkHttpClient.Builder().connectTimeout(10,
                    TimeUnit.SECONDS)
                                                     .writeTimeout(10,
                                                             TimeUnit.SECONDS)
                                                     .readTimeout(30,
                                                             TimeUnit.SECONDS)
                                                     //.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                                     .addInterceptor(
                                                             new HttpLoggingInterceptor()
                                                                     .setLevel(
                                                                             HttpLoggingInterceptor.Level.BODY))
                                                     .retryOnConnectionFailure(true)
                                                     .addInterceptor(
                                                             chain -> interceptResponse(
                                                                     chain))
                                                     //.readTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                                                     .cache(NetworkConfig.getCache())
                                                     .build();

            //mRetrofit = new Retrofit.Builder()
            //                                  .addConverterFactory(
            //                                          MyGsonConverterFactory.create(new Gson()))
            //                                  .addCallAdapterFactory(
            //                                          RxJavaCallAdapterFactory.create())
            //                                  .client(okHttpClient)
            //                                  .baseUrl(
            //                                          NetworkConfig.getBaseUrl())
            //                                  .build();
            //Gson gson = new GsonBuilder()
            //        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            //        .create();
            mRetrofit = new Retrofit.Builder().addConverterFactory(
                    MyGsonConverterFactory.create(new GsonBuilder()
                            .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create()))
                                              .addCallAdapterFactory(
                                                      RxJava2CallAdapterFactory.create())
                                              .client(okHttpClient)
                                              .baseUrl(
                                                      NetworkConfig.getBaseUrl())
                                              .build();
            mApiService = mRetrofit.create(ApiService.class);
        }
    }


    public ApiService getApiService() {
        return mApiService;
    }


    private Response interceptResponse(Interceptor.Chain chain)
            throws IOException {
        //获得url上面的请求参数
        Request original = chain.request();
        //    RequestBody body = original.body();
        Request.Builder requestBuilder = original.newBuilder();
        //if (original.method().equals("GET")) {
        //    Log.i("result", " GET");
        //    HttpUrl url = original.url();
        //    int size = url.querySize();
        //    if (size > 0) {
        //        TreeMap<String, Object> map = new TreeMap<>();
        //        for (int i = 0; i < size; i++) {
        //            String key = url.queryParameterName(i);
        //            String value = url.queryParameterValue(i);
        //            map.put(key, value);
        //        }
        //        url = url.newBuilder()
        //                 .addQueryParameter("channel", "1")
        //                 .addQueryParameter("localDateTime",
        //                         AppContext.LOCAL_DATE_TIME)
        //                 .addQueryParameter("userId",
        //                         !getUserId().equals("") ? getUserId() : "")
        //                 .addQueryParameter("stepCode", "1")
        //                 .addQueryParameter("accessSource", "3")
        //                 .addQueryParameter("version", Constant.INTER_VERSION)
        //                 .build();
        //        requestBuilder.url(url);
        //        requestBuilder.method(original.method(), body);
        //    }
        //}
        //else if (original.method().equals("POST")) {
        //    Log.i("result", " POST");
        //    if (body instanceof FormBody) {
        //        FormBody oldBody = (FormBody) body;
        //        FormBody.Builder newBody = new FormBody.Builder();
        //        TreeMap<String, Object> map = new TreeMap<>();
        //        for (int i = 0; i < oldBody.size(); i++) {
        //            String key = oldBody.name(i);
        //            String value = oldBody.value(i);
        //            newBody.add(key, value);
        //            map.put(key, value);
        //        }
        //        //newBody.add("channel", "1");              //（1-择机 2-T钱包）
        //        //newBody.add("localDateTime", AppContext.LOCAL_DATE_TIME);
        //        //if (!TextUtils.isEmpty(getUserId())) {
        //        //    newBody.add("userId", getUserId());
        //        //}
        //        //newBody.add("stepCode", "1");
        //        //newBody.add("accessSource", "3");//1-PC 2-IOS 3-Android 4-其他
        //        //newBody.add("version", Constant.INTER_VERSION);
        //        //AccessTokenInfo info = TokenManager.getInstance()
        //        //                                   .getSimpleTokenInfo();
        //        //if (info != null && !TextUtils.isEmpty(info.accessToken)) {
        //        //    newBody.add("accessToken", info.accessToken);
        //        //}
        //        SessionContext sessionContext = new SessionContext();
        //        sessionContext.channel = "1";
        //        sessionContext.localDateTime = AppContext.LOCAL_DATE_TIME;
        //        if (!TextUtils.isEmpty(getUserId())) {
        //            sessionContext.userId = getUserId();
        //        }
        //        sessionContext.stepCode = "1";
        //        sessionContext.accessSource = "3";
        //        sessionContext.version = Constant.INTER_VERSION;
        //        newBody.add("sessionContext",
        //                new Gson().toJson(sessionContext));
        //        requestBuilder.method(original.method(), newBody.build());
        //    }
        //}
        Request request = requestBuilder.build();
        Log.d(TAG, "url=" + request.url().toString());
        Response response = chain.proceed(request);
        return response;
    }


}
