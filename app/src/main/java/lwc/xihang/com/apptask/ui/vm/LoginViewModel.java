package lwc.xihang.com.apptask.ui.vm;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import lwc.xihang.com.apptask.R;
import lwc.xihang.com.apptask.entity.User;
import lwc.xihang.com.apptask.service.LoginService;
import lwc.xihang.com.apptask.service.RegistryUserService;
import lwc.xihang.com.apptask.ui.activity.MainActivity;
import lwc.xihang.com.apptask.utils.Configuration;
import lwc.xihang.com.apptask.utils.RetrofitClient;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.RequestBody;
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
    // 注册按钮点击事件
    public BindingCommand registryClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            registryUser();
        }
    });
    /**
     * 登录系统实现逻辑
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
                        User user = response;
                        SharedPreferences preferences =
                                getSharedPreferences(Configuration.SharedPreferencesLogin);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLogin", true);
                        editor.putString("username", user.getUserName());
                        editor.putLong("userId", Long.parseLong(user.getId()));
                        editor.commit();
                        startActivity(MainActivity.class);
                        dismissDialog();
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
    // 注册用户实现逻辑
    public void registryUser(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.regisitry_user_layout,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        builder.create();
        final AlertDialog dialog=builder.create();
        dialog.show();
        // 初始化编辑框组件
        final EditText r_username=view.findViewById(R.id.registry_username_id);
        final EditText r_password=view.findViewById(R.id.registry_password_id);
        final EditText confirm_password=view.findViewById(R.id.registry_confirm_password_id);
        final Button saveUser=view.findViewById(R.id.save_user_id);
        final Button cancelUser=view.findViewById(R.id.cancel_user_id);
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(r_username.getText().toString()!=null){
                    if(r_password.getText().toString()!=null&&confirm_password.getText().toString()!=null){
                        if(r_password.getText().toString().equals(confirm_password.getText().toString())){
                            User user=new User();
                            user.setUserName(r_username.getText().toString());
                            user.setPassword(r_password.getText().toString());
                            Gson gson = new Gson();
                            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(
                                    "application/json;charset=UTF-8"), gson.toJson(user));
                            RetrofitClient.getInstance().create(RegistryUserService.class)
                                    .registry(body)
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
                                            ToastUtils.showLong("注册成功");
                                            userName.set(r_username.getText().toString());
                                            password.set(r_password.getText().toString());
                                            dismissDialog();
                                            dialog.dismiss();
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            dismissDialog();
                                            ToastUtils.showShort("注册失败！用户名重复!");
                                            throwable.printStackTrace();
                                        }
                                    });
                        }
                    }else {
                        ToastUtils.showLong("密码为空!");
                    }
                }else {
                    ToastUtils.showLong("用户名为空!");
                }


            }
        });
        cancelUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
