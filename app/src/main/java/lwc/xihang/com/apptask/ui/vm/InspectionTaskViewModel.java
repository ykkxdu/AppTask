package lwc.xihang.com.apptask.ui.vm;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;

import lwc.xihang.com.apptask.BR;
import lwc.xihang.com.apptask.R;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import rx.functions.Action0;

/**
 * Created by Administrator on 2019/1/3.
 */

public class InspectionTaskViewModel extends BaseViewModel{
    public final ObservableList<InspectionTaskItemViewModel>
            observableList = new ObservableArrayList<>();
    public final ItemBinding<InspectionTaskItemViewModel>
            itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_inspection_task);


    // 修改数据
    public BindingCommand modifyItemClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
        }
    });
    // 删除数据
    public BindingCommand deleteItemClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
        }
    });
}
