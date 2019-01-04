package lwc.xihang.com.apptask.ui.vm;

import android.content.Context;
import android.databinding.ObservableField;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import lwc.xihang.com.apptask.entity.InspectionTask;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import rx.functions.Action0;

/**
 * Created by Administrator on 2019/1/3.
 */

public class InspectionTaskItemViewModel extends BaseViewModel{
    public ObservableField<InspectionTask> entity;
    public InspectionTaskItemViewModel(Context context, ObservableField<InspectionTask> entity){
        super(context);
        this.entity = entity;
    }
    // 修改数据
    public BindingCommand modifyItemClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
           if(entity.get().getId().equals("序号")) return;
           new InspectionTaskViewModel().modifyTaskData(entity);
        }
    });
    // 删除数据
    public BindingCommand deleteItemClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            Toast.makeText(context,"删除数据",Toast.LENGTH_LONG).show();
        }
    });
}
