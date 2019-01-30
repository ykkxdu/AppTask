package lwc.xihang.com.apptask.service;

import lwc.xihang.com.apptask.entity.User;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2019/1/4
 */

public interface LoginService {
    /**
     * 登录界面接口
     * 方法:post
     * 接口:/login
     * 参数：username(用户名)，password(密码)
    * */
    @FormUrlEncoded
    @POST("login")
    Observable<User> login(
            @Field("username") String username,
            @Field("password") String password);
    /**
     * 退出系统接口
     * 方法:post
     * 接口:/logout
     * 参数:无
     * */
    @POST("logout")
    Observable<String> logout();
}
