package lwc.xihang.com.apptask.entity;

/**
 * Created by Administrator on 2019/1/3.
 */

public class InspectionTask {
    // 任务序号
    private String id;
    // 任务名字
    private String taskName;
    // 任务内容
    private String taskContent;
    // 完成时间
    private String finishTime;
    // 任务状态
    private String taskStatus;
    // 修改
    private String modify;
    // 删除
    private String delete;
    // 序号
    private String  num;
    private BlueLabel blueLabel;
    private User user;

    public BlueLabel getBlueLabel() {
        return blueLabel;
    }

    public void setBlueLabel(BlueLabel blueLabel) {
        this.blueLabel = blueLabel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public InspectionTask(String num,
                          String id,
                          String taskName,
                          String taskContent,
                          String finishTime,
                          String taskStatus,
                          String modify,
                          String delete){
        this.id=id;
        this.num=num;
        this.taskName=taskName;
        this.taskContent=taskContent;
        this.finishTime=finishTime;
        this.taskStatus=taskStatus;
        this.modify=modify;
        this.delete=delete;
    }

    public InspectionTask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
