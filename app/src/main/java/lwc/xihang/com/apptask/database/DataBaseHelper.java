package lwc.xihang.com.apptask.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2019/1/4.
 * APP端数据库配置
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    // 用户数据库
    // 检查任务数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 检查任务数据库
        String sql_inspectionTask= "create table task(_id integer primary key autoincrement,taskId," +
                "taskNo,user_id,taskName,taskContent,finishTime,taskStatus,modify,deleteTask)"; //10字段
        // 创建数据库
        db.execSQL(sql_inspectionTask);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
