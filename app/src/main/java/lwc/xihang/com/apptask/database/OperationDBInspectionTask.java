package lwc.xihang.com.apptask.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/1/4.
 */

public class OperationDBInspectionTask {
    DataBaseHelper dataBaseHelper;
    Cursor cursor;
    Context context;
    SQLiteDatabase db;
    private ArrayList<String> list;
    private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> getLists() {
        return lists;
    }
    public OperationDBInspectionTask(Context context) {
        this.context = context;
    }
    //    初始化数据库操作
    public void initdb() {
        File file = context.getFilesDir();
        String path = file.getAbsolutePath() + "/inspectionTask.db";
        dataBaseHelper = new DataBaseHelper(context, path, 1);
        db = dataBaseHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from inspectionTask", null);
    }
    //    向数据库中添加数据
    public void addInDataBase(Object[] objects) {
        initdb();
        db.execSQL("insert into inspectionTask values(?,?,?,?,?,?,?,?,?,?)", objects);
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
            list.add(cursor.getString(cursor.getColumnIndex("delete")));//9
            lists.add(list);
        }
        return lists;
    }
    // 清除数据库
    public void clearDB() {
        initdb();
        while (cursor.moveToNext()) {
            db.execSQL("delete from inspectionTask where _id=?",
                    new Object[]{Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")))});
        }
        cursor.close();
    }
    // 修改数据库
    public void modifyInDataBase(Object[] objects) {
        initdb();
        db.execSQL("update administrativeIdentification set " +
                "taskName=?,taskContent=?,finishTime=?,taskStatus=? where _id=?",objects);
    }

}
