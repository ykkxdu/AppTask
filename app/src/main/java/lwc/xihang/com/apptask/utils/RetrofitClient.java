package lwc.xihang.com.apptask.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.goldze.mvvmhabit.BuildConfig;
import me.goldze.mvvmhabit.http.cookie.CookieJarImpl;
import me.goldze.mvvmhabit.http.cookie.store.PersistentCookieStore;
import me.goldze.mvvmhabit.http.interceptor.BaseInterceptor;
import me.goldze.mvvmhabit.http.interceptor.CaheInterceptor;
import me.goldze.mvvmhabit.http.interceptor.logging.Level;
import me.goldze.mvvmhabit.http.interceptor.logging.LoggingInterceptor;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {

    // 超时时间
    private static final int DEFAULT_TIMEOUT = 1200000;
    // 缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    // 服务端根路径
    private static String baseUrl = Configuration.HTTP_BASE_URL;
    //private static String baseUrl = null;
    private static Context mContext = Utils.getContext();

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static void settingIp(String ip) {
        baseUrl = "http://" + ip + "/";
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static RetrofitClient getInstanceNEW() {
        RetrofitClient retrofitClient = new RetrofitClient();
        return retrofitClient;
    }
    public static RetrofitClient getInstance(String url) {
        return new RetrofitClient(url);
    }

    public static RetrofitClient getInstance(String url, Map<String, String> headers) {
        return new RetrofitClient(url, headers);
    }

    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url) {
        this(url, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
            System.out.println("客户端得到的数据为:"+url);
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), Configuration.HTTP_CACHE_NAME);
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }

        okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .cache(cache)
            .addInterceptor(new BaseInterceptor(headers))
            .addInterceptor(new CaheInterceptor(mContext))
            //.addInterceptor(new CreateInterceptor())//拦截201，直接返回错误哦
            .addInterceptor(new LoggingInterceptor
                .Builder()//构建者模式
                .loggable(Configuration.DEBUG) //是否开启日志打印
                .setLevel(Level.BODY) //打印的等级
                .log(Platform.INFO) // 打印类型
                .request("Request") // request的Tag
                .response("Response")// Response的Tag
                .addHeader("version", BuildConfig.VERSION_NAME)//打印版本
                .build()
            )
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
            // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
            .build();
        retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(new NullOnEmptyConverterFactory()) //201一开始修改，将IO报错屏蔽掉
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(url)
            .build();
    }
    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

        return null;
    }
}
