package lwc.xihang.com.apptask.ui.fragment;

import lwc.xihang.com.apptask.BR;
import lwc.xihang.com.apptask.R;
import lwc.xihang.com.apptask.databinding.FragmentInspectionTaskBinding;
import lwc.xihang.com.apptask.ui.vm.InspectionTaskViewModel;
import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by Administrator on 2019/1/3.
 */

public class InspectionTaskFragment extends BaseFragment<FragmentInspectionTaskBinding, InspectionTaskViewModel>{
    @Override
    public int initContentView() {
        return R.layout.fragment_inspection_task;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public InspectionTaskViewModel initViewModel() {
        return new InspectionTaskViewModel();
    }
}
