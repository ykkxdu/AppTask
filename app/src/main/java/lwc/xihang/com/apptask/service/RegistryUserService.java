package lwc.xihang.com.apptask.service;

import lwc.xihang.com.apptask.entity.User;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2019/1/8.
 */

public interface RegistryUserService {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/users/registry")
    Observable<User> registry(@Body RequestBody entity);
}
