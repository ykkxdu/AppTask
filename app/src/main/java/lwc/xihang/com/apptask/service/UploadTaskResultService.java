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
 */

public interface UploadTaskResultService {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @PATCH("api/inspectionTasks/{id}")
    Observable<InspectionTask> upLoadTask
            (@Path("id") Long id, @Body RequestBody task);
}
