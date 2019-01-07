package lwc.xihang.com.apptask.service;

import lwc.xihang.com.apptask.entity.User;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2019/1/4.
 */

public interface LoginService {
    @FormUrlEncoded
    @POST("login")
    Observable<User> login(
            @Field("username") String username,
            @Field("password") String password);

    @POST("logout")
    Observable<String> logout();
}
