package lwc.xihang.com.apptask.service;

import lwc.xihang.com.apptask.entity.InspectionTask;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2019/1/8.
 * 修改检查任务接口
 * 方法:patch
 * 接口:api/inspectionTasks/{id}
 * 参数:id(要修改的检查任务Id)
 */

public interface UploadTaskResultService {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @PATCH("api/inspectionTasks/{id}")
    Observable<InspectionTask> upLoadTask
            (@Path("id") Long id, @Body RequestBody task);
}
