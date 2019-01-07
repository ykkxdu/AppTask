package lwc.xihang.com.apptask.ui.vm;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import java.util.ArrayList;
import java.util.Calendar;
import lwc.xihang.com.apptask.BR;
import lwc.xihang.com.apptask.R;
import lwc.xihang.com.apptask.database.OperationDBInspectionTask;
import lwc.xihang.com.apptask.entity.InspectionTask;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
/**
 * Created by Administrator on 2019/1/3.
 */
public class InspectionTaskViewModel extends BaseViewModel{
    private Activity activity;
    // 任务状态选择框
    private ListPopupWindow lpwShowStatus;
    private String[] listShowStatus;
    OperationDBInspectionTask operationDBInspectionTask=new OperationDBInspectionTask(context);
    @Override
    public void onCreateView() {
        super.onCreateView();
    }
    public InspectionTaskViewModel(Activity activity){
        this.activity=activity;
        initListView();
    }
    public InspectionTaskViewModel(){
        initListView();
    }

    public final ObservableList<InspectionTaskItemViewModel>
            observableList = new ObservableArrayList<>();
    public final ItemBinding<InspectionTaskItemViewModel>
            itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_inspection_task);
    // 序号
    public static ObservableField<String> taskNo=new ObservableField<>();
    public static ObservableField<String> taskName=new ObservableField<>();
    public static ObservableField<String> taskContent=new ObservableField<>();
    public static ObservableField<String> finishTime=new ObservableField<>();
    public static ObservableField<String> taskStatus=new ObservableField<>();
    public static ObservableField<String> modify=new ObservableField<>();
    public static ObservableField<String> delete=new ObservableField<>();
    private void initListView(){
        observableList.clear();
        ObservableField<InspectionTask> header=new ObservableField<>(new InspectionTask(
                "序号",
                "任务ID",
                "任务名称",
                "任务内容",
                "完成时间",
                "任务状态",
                "修改",
                "删除"));
        observableList.add(new InspectionTaskItemViewModel(getActivity(),header));
        // 从web端读入数据
        ArrayList<ArrayList<String>> lists=operationDBInspectionTask.getSaveData();
        for(int i=0;i<lists.size();i++){
            InspectionTask task=new InspectionTask(
                    String.valueOf(i+1),
                    lists.get(i).get(1),
                    lists.get(i).get(4),
                    lists.get(i).get(5),
                    lists.get(i).get(6),
                    lists.get(i).get(7),
                    lists.get(i).get(8),
                    lists.get(i).get(9));
            ObservableField<InspectionTask> taskObservableField=new ObservableField<>(task);
            InspectionTaskItemViewModel inspectionTaskItemViewModel
                    =new InspectionTaskItemViewModel(getActivity(),taskObservableField);
            observableList.add(inspectionTaskItemViewModel);
        }
    }
    // 修改检查任务逻辑
    public void modifyTaskData(final ObservableField<InspectionTask> entity){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.inspection_task_modify_dialog,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        // 初始化编辑框组件
        final EditText nameEdit=view.findViewById(R.id.task_name_id);
        final EditText contentEdit=view.findViewById(R.id.task_content_id);
        final EditText finishTimeEdit=view.findViewById(R.id.finish_time_id);
        final EditText statusEdit=view.findViewById(R.id.task_status_id);
        final Calendar calendar = Calendar.getInstance();
        finishTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        finishTimeEdit.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
                    }
                }, calendar.get(
                        Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        nameEdit.setText(entity.get().getTaskName());
        contentEdit.setText(entity.get().getTaskContent());
        finishTimeEdit.setText(entity.get().getFinishTime());
        statusEdit.setText(entity.get().getTaskStatus());
        builder.setView(view);
        builder.create();
        final AlertDialog dialog=builder.create();
        Button save=view.findViewById(R.id.save_id);
        Button cancel=view.findViewById(R.id.cancle_id);
        final String taskID=entity.get().getId();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object[] objects=new Object[]{
                        nameEdit.getText(),
                        contentEdit.getText(),
                        finishTimeEdit.getText(),
                        statusEdit.getText(),
                        taskID};

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        // 任务状态下拉选择菜单
        listShowStatus=new String[]{"未完成","完成"};
        lpwShowStatus=new ListPopupWindow(getActivity());
        lpwShowStatus.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, listShowStatus));
        lpwShowStatus.setAnchorView(statusEdit);
        lpwShowStatus.setModal(true);
        statusEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lpwShowStatus.show();
            }
        });
        lpwShowStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listShowStatus[position];
                statusEdit.setText(item);
                lpwShowStatus.dismiss();
            }
        });
    }
}
