package lwc.xihang.com.apptask.ui.vm;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.ObservableField;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import lwc.xihang.com.apptask.database.OperationDBInspectionTask;
import lwc.xihang.com.apptask.entity.BaseWrapper;
import lwc.xihang.com.apptask.entity.InspectionTask;
import lwc.xihang.com.apptask.service.InspectionTaskService;
import lwc.xihang.com.apptask.service.LoginService;
import lwc.xihang.com.apptask.service.UploadTaskResultService;
import lwc.xihang.com.apptask.ui.activity.LoginActivity;
import lwc.xihang.com.apptask.utils.Configuration;
import lwc.xihang.com.apptask.utils.RetrofitClient;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.RequestBody;
import rx.functions.Action0;
import rx.functions.Action1;
/**
 * Created on 2017/12/19.
 */
public class MainViewModel extends BaseViewModel {
    public ObservableField<String> userName = new ObservableField<>("");
    OperationDBInspectionTask operationDBInspectionTask=new OperationDBInspectionTask(context);
    private AlertDialog alertDialog3;
    final List<String> result = new ArrayList<>();
    public MainViewModel(Context context) {
        super(context);
        // 同步用户名操作，使和登录界面的额用户名一样。
        SharedPreferences preferences = getSharedPreferences(Configuration.SharedPreferencesLogin);
        String userName_login = preferences.getString("username","admin");
        userName.set(userName_login);
    }
    // 上传检查结果
    public void uploadTaskResult(){
         OperationDBInspectionTask operation=new OperationDBInspectionTask(context);
         // 从数据库中读取所有的检查任务
        ArrayList<ArrayList<String>> lists=operation.getSaveData();
        // 用来储存所有已经完成的检查任务
        ArrayList<ArrayList<String>> compliteTasks = new ArrayList<>();
        for(int i=0;i<lists.size();i++){
            if(lists.get(i).get(7).equals("完成")){
                compliteTasks.add(lists.get(i));
            }
        }
        result.clear();
        for(int m=0;m<compliteTasks.size();m++){
            result.add(compliteTasks.get(m).get(1)+","+
                        compliteTasks.get(m).get(4)+","+
                        compliteTasks.get(m).get(7));
        }
        result.add("全选");
        final String[] items = result.toArray(new String[result.size()]);
        final List<String> isHasChoosed = new ArrayList<>();
        // 弹出框初始化
        final AlertDialog.Builder alter = new AlertDialog.Builder(context);
        alter.setTitle("选择要上传的巡检任务结果");
        alter.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked) {
                    if(which==items.length-1)
                    {
                        for(int i=0;i<items.length-1;i++)
                        {
                            isHasChoosed.add(items[i]);
                        }
                    }else {
                        isHasChoosed.add(items[which]);
                    }
                } else {
                    isHasChoosed.remove(items[which]);
                }
            }
        });
        alter.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDialog();
                for(String s:isHasChoosed){
                    findTaskResult(s.split(",")[0]);
                }
            }
        });
        alter.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
            }
        });
        alertDialog3 = alter.create();
        alertDialog3.show();
        dismissDialog();


    }
    public void findTaskResult(String s){
        OperationDBInspectionTask task=new OperationDBInspectionTask(context);
        InspectionTask inspectionTask=null;
        ArrayList<ArrayList<String>> lists=task.getSaveData();
        for(ArrayList<String> list:lists){
            if(list.get(1).equals(s)){
                inspectionTask=new InspectionTask(
                        list.get(0),
                        list.get(1),
                        list.get(4),
                        list.get(5),
                        list.get(6),
                        list.get(7),
                        list.get(8),
                        list.get(9));
                break;
            }
        }
        Gson gs = new Gson();
        RequestBody task_body = RequestBody.create(okhttp3.MediaType.parse(
                "application/json;charset=UTF-8"),
                gs.toJson(inspectionTask));
        RetrofitClient.getInstance().create(UploadTaskResultService.class)
                .upLoadTask(Long.parseLong(s),task_body)
                .compose(RxUtils.bindToLifecycle(context))
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        dismissDialog();
                        ToastUtils.showLong("上传成功!");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        dismissDialog();
                    }
                });
    }
    //  下载检查任务
    public void downLoadTask(){
        RetrofitClient.getInstance().create(InspectionTaskService.class)
                .queryAll()
                .compose(RxUtils.bindToLifecycle(context))
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog();
                    }
                })
                .subscribe(new Action1<BaseWrapper<InspectionTask>>() {
                    @Override
                    public void call(BaseWrapper<InspectionTask> response) {
                        final List<InspectionTask> tasks = response.get_embedded().get("inspectionTasks");
                        result.clear();
                        for (InspectionTask task : tasks) {
                            result.add(task.getId() + ","+task.getTaskContent() + "," +task.getTaskName());
                        }
                        result.add("全选");
                        final String[] items = result.toArray(new String[result.size()]);
                        final List<String> hasChoosed = new ArrayList<>();
                        // 创建一个AlertDialog建造者
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        // 设置标题
                        alertDialogBuilder.setTitle("选择下载任务");
                        alertDialogBuilder.setMultiChoiceItems(
                                items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            hasChoosed.add(items[which]);
                                            // 判断是否全部选中，如果点击全部选中，则下载所有的检查任务。
                                            if (which == items.length - 1) {
                                                for (int i = 0; i < items.length - 1; i++) {
                                                    hasChoosed.add(items[i]);
                                                }
                                            }
                                        } else {
                                            hasChoosed.remove(items[which]);
                                        }
                                    }
                                });
                        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //TODO 业务逻辑代码
                                showDialog();
                                for (String s : hasChoosed) {
                                    downInspectionTask(tasks, s.split(",")[0]);
                                }
                                // 刷新页面
                               new InspectionTaskViewModel();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO 业务逻辑代码
                                // 关闭提示框
                                alertDialog3.dismiss();
                            }
                        });
                        alertDialog3 = alertDialogBuilder.create();
                        alertDialog3.show();
                        dismissDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissDialog();
                        ToastUtils.showShort(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }
    // 将选择的巡检任务保存到本地数据库中
    public void downInspectionTask(List<InspectionTask> tasks, String id){
        for(InspectionTask task:tasks){
            if(task.getId().equals(id)){
                Object[] objects=new Object[]{
                        null,
                        task.getId(),
                        task.getId(),
                        task.getId(),
                        task.getTaskName(),
                        task.getTaskContent(),
                        task.getFinishTime(),
                        task.getTaskStatus(),
                        "修改",
                        "删除"};
                boolean isHaveId=operationDBInspectionTask.isHaveId(task.getId());
                if(!isHaveId) {
                    operationDBInspectionTask.addInDataBase(objects);
                }else {
                    ToastUtils.showShort("此桥已经下载!");
                }
            }
        }
        dismissDialog();
    }
    // 退出登录操作
    public void logout(){
        RetrofitClient.getInstance().create(LoginService.class)
                .logout()
                .compose(RxUtils.bindToLifecycle(context))
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String response) { // response 204 NO_CONTENT
                        dismissDialog();
                        SharedPreferences preferences = getSharedPreferences(Configuration.SharedPreferencesLogin);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("isLogin");
                        editor.remove("username");
                        editor.apply();
                        ToastUtils.showShort("成功退出登录!");
                        startActivity(LoginActivity.class);
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

}
