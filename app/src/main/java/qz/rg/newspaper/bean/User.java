package qz.rg.newspaper.bean;

public class User {
    private String avatarUrl; // 用户头像 URL
    private String nickname;  // 用户昵称
    private String userId;    // 用户 ID（可选）
    // 其他属性：手机号、邮箱等

    // 构造方法
    public User(int avatarUrl, String nickname) {
        this.avatarUrl = String.valueOf(avatarUrl);
        this.nickname = nickname;
    }

    // Getter 方法（供外部读取数据）
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    // Setter 方法（供外部更新数据，可选）
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }
}
