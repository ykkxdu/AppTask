package lwc.xihang.com.apptask.entity;

/**
 * Created by Administrator on 2019/1/4.
 * 用户实体
 */

public class User {
    // 用户ID
    private String id;
    // 用户名
    private String userName;
    // 密码
    private String password;
    public User() {
    }
    public User(String id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
