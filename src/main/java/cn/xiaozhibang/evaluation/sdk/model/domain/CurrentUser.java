package cn.xiaozhibang.evaluation.sdk.model.domain;

/**
 * 当前用户信息
 *
 * @author generated
 */
public class CurrentUser {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String userAvatar;

    public CurrentUser() {
    }

    public CurrentUser(String id, String nickName, String userAvatar) {
        this.id = id;
        this.nickName = nickName;
        this.userAvatar = userAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
