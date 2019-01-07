package lwc.xihang.com.apptask.ui.vm;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.view.View;

import lwc.xihang.com.apptask.entity.User;
import lwc.xihang.com.apptask.service.LoginService;
import lwc.xihang.com.apptask.ui.activity.MainActivity;
import lwc.xihang.com.apptask.utils.Configuration;
import lwc.xihang.com.apptask.utils.RetrofitClient;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import rx.functions.Action0;
import rx.functions.Action1;
/**
 * Created on 2017/7/17.
 */
public class LoginViewModel extends BaseViewModel {
    public LoginViewModel(Context context) {
        super(context);
    }
    // 用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("admin");
    // 密码的绑定
    public ObservableField<String> password = new ObservableField<>("pass");
    // 用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    // 封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        // 密码开关观察者
        public ObservableBoolean pSwitchObservable = new ObservableBoolean(false);
    }
    // 清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            userName.set("");
        }
    });
    // 密码显示开关(你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            //让观察者的数据改变,在View层的监听则会被调用
            uc.pSwitchObservable.set(!uc.pSwitchObservable.get());
        }
    });
    // 用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new Action1<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });
    // 登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            login();
        }
    });
    /**
     * 网络登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //真实登录，从服务端请求数据
        RetrofitClient.getInstance().create(LoginService.class)
                .login(userName.get(), password.get())
                .compose(RxUtils.bindToLifecycle(getActivity()))
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog();
                    }
                })
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User response) {
                        dismissDialog();
                        User user = response;
                        SharedPreferences preferences =
                                getSharedPreferences(Configuration.SharedPreferencesLogin);
                        SharedPreferences.Editor editor = preferences.edit();
                         editor.putBoolean("isLogin", true);
                        editor.putString("username", user.getUserName());
                        editor.putLong("userId", Long.parseLong(user.getId()));
                        editor.commit();
                        startActivity(MainActivity.class);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissDialog();
                        ToastUtils.showShort("请求异常");
                        throwable.printStackTrace();
                    }
                });
    }
    public void gotoMain() {
        startActivity(MainActivity.class);
    }

}
