package lwc.xihang.com.apptask.ui.vm;

import android.content.Context;
import android.databinding.ObservableField;

import lwc.xihang.com.apptask.entity.InspectionTask;
import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by Administrator on 2019/1/3.
 */

public class InspectionTaskItemViewModel extends BaseViewModel{
    public ObservableField<InspectionTask> entity;
    public InspectionTaskItemViewModel(Context context, ObservableField<InspectionTask> entity){
        super(context);
        this.entity = entity;
    }
}
