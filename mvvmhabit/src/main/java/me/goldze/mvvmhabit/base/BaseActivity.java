package me.goldze.mvvmhabit.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.bus.RxBus;


/**
 * Created by goldze on 2017/6/15.
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseActivity {
    protected V binding;
    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParam();

        initViewDataBinding();

        initData();

        initViewObservable();

        viewModel.onCreateView();

        viewModel.registerRxBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(this);
        viewModel.removeRxBus();
        viewModel.onDestroyView();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView());
        binding.setVariable(initVariableId(), viewModel = initViewModel());
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }

    @Override
    public void initParam() {

    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView();

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public abstract VM initViewModel();

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    /**
     * 
     * @param fContainerId fragment container id
     * @param fragment fragment
     */
    public void startFragment(int fContainerId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentById(fContainerId) == null) {
            fragmentTransaction.add(fContainerId, fragment);
        } else {
            fragmentTransaction.replace(fContainerId, fragment);
        }
        fragmentTransaction.commit();
    }
}
