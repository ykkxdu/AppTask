package lwc.xihang.com.apptask.service;


import lwc.xihang.com.apptask.entity.BaseWrapper;
import lwc.xihang.com.apptask.entity.InspectionTask;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2019/1/7.
 * 下载巡检任务的接口
 * 方法:get
 * 接口:api/inspectionTasks?projection=all
 * 参数:无
 */

public interface InspectionTaskService {
    @GET("api/inspectionTasks?projection=all")
    Observable<BaseWrapper<InspectionTask>> queryAll();
}
