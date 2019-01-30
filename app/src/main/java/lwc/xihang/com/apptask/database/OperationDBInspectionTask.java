package lwc.xihang.com.apptask.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/1/4.
 * 检查任务数据库的增删改查操作
 */

public class OperationDBInspectionTask {
    DataBaseHelper dataBaseHelper;
    Cursor cursor;
    Context context;
    SQLiteDatabase db;
    private ArrayList<String> list;
    private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
    public OperationDBInspectionTask(Context context) {
        this.context = context;
    }
    //    初始化数据库操作
    public void initdb() {
        File file = context.getFilesDir();
        String path = file.getAbsolutePath() + "/task.db";
        dataBaseHelper = new DataBaseHelper(context, path, 1);
        db = dataBaseHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from task", null);
    }
    //    向数据库中添加数据
    public void addInDataBase(Object[] objects) {
        initdb();
        db.execSQL("insert into task values(?,?,?,?,?,?,?,?,?,?)", objects);
    }
    //    返回数据库中的数据
    public ArrayList<ArrayList<String>> getSaveData() {
        initdb();
        while (cursor.moveToNext()) {
            list = new ArrayList<String>();
            list.add(cursor.getString(cursor.getColumnIndex("_id")));//0
            list.add(cursor.getString(cursor.getColumnIndex("taskId")));//1
            list.add(cursor.getString(cursor.getColumnIndex("taskNo")));//2
            list.add(cursor.getString(cursor.getColumnIndex("user_id")));//3
            list.add(cursor.getString(cursor.getColumnIndex("taskName")));//4
            list.add(cursor.getString(cursor.getColumnIndex("taskContent")));//5
            list.add(cursor.getString(cursor.getColumnIndex("finishTime")));//6
            list.add(cursor.getString(cursor.getColumnIndex("taskStatus")));//7
            list.add(cursor.getString(cursor.getColumnIndex("modify")));//8
            list.add(cursor.getString(cursor.getColumnIndex("deleteTask")));//9
            lists.add(list);
        }
        return lists;
    }
    // 清除数据库中的全部数据
    public void clearDB() {
        initdb();
        while (cursor.moveToNext()) {
            db.execSQL("delete from task where _id=?",
                    new Object[]{Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")))});
        }
        cursor.close();
    }
    // 删除数据库中的数据
    public void deleteData(Object[] objects){
        initdb();
        while (cursor.moveToNext()) {
            db.execSQL("delete from task where taskId=?",objects);
        }
        cursor.close();
    }
    // 修改数据库
    public void modifyInDataBase(Object[] objects) {
        initdb();
        db.execSQL("update task set " +
                "taskName=?,taskContent=?,finishTime=?,taskStatus=? where taskId=?",objects);
    }
    // 用来判读是否重复下载
    public boolean isHaveId(String id){
        boolean flag=false;
        initdb();
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("_id")).equals(id)){
                flag=true;
            }
        }
        return flag;
    }

}
