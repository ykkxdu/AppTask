package lwc.xihang.com.apptask.entity;

/**
 * Created by Administrator on 2019/1/11.
 * 蓝牙标签实体
 */

public class BlueLabel {
    // 蓝牙Id
    private String id;
    // 蓝牙标签
    private String blueToothId;
    // 蓝牙的名字
    private String blueToothName;

    public BlueLabel(String id, String blueToothId, String blueToothName) {
        this.id = id;
        this.blueToothId = blueToothId;
        this.blueToothName = blueToothName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlueToothId() {
        return blueToothId;
    }

    public void setBlueToothId(String blueToothId) {
        this.blueToothId = blueToothId;
    }

    public String getBlueToothName() {
        return blueToothName;
    }

    public void setBlueToothName(String blueToothName) {
        this.blueToothName = blueToothName;
    }
}
