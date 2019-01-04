package lwc.xihang.com.apptask.ui.activity;
import android.databinding.Observable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import lwc.xihang.com.apptask.BR;
import lwc.xihang.com.apptask.R;
import lwc.xihang.com.apptask.databinding.ActivityLoginBinding;
import lwc.xihang.com.apptask.ui.vm.LoginViewModel;
import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 一个MVVM模式的登陆界面
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    private LoginViewModel loginViewModel;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        loginViewModel = new LoginViewModel(this);
        return  loginViewModel;
    }
    @Override
    public void initViewObservable() {
        // 监听ViewModel中pSwitchObservable的变化,
        // 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时
        // 会回调该方法
        viewModel.uc.pSwitchObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //pSwitchObservable是boolean类型的观察者,所以可以直接使用它的值改变密码开关的图标
                if (viewModel.uc.pSwitchObservable.get()) {
                    // 密码可见
                    // 在xml中定义id后,使用binding可以直接拿到这个view的引用,
                    // 不再需要findViewById去找控件了
                    binding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw_press);
                    binding.etPassword.
                            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    binding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw);
                    binding.etPassword.
                            setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

}
