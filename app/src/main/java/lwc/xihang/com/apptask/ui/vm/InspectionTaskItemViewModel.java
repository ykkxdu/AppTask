package lwc.xihang.com.apptask.ui.vm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private Activity activity;
    public InspectionTaskItemViewModel(Activity activity, ObservableField<InspectionTask> entity){
        this.entity = entity;
        this.activity=activity;
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
         if(entity.get().getId().equals("序号")) return;
         new AlertDialog.Builder(activity)
                 .setTitle("确认删除巡检任务吗?")
                 .setNegativeButton("取消",null)
                 .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(activity,"删除成功!",Toast.LENGTH_LONG).show();
                     }
                 }).show();
        }
    });
}
