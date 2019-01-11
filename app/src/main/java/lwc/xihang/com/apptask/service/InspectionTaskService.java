package lwc.xihang.com.apptask.service;


import lwc.xihang.com.apptask.entity.BaseWrapper;
import lwc.xihang.com.apptask.entity.InspectionTask;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2019/1/7.
 */

public interface InspectionTaskService {
    @GET("api/inspectionTasks?projection=all")
    Observable<BaseWrapper<InspectionTask>> queryAll();
}
