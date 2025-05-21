package qz.rg.newspaper.utils;

import qz.rg.newspaper.bean.User;

//负责管理数据的获取和更新
public class UserManager {
    private static UserManager instance;

    private User currentUser; // 当前用户对象

    // 私有化构造方法，避免外部实例化
    private UserManager() {
        // 初始化逻辑（如从本地存储加载用户信息）
        currentUser = loadUserFromSharedPreferences();
    }

    // 获取单例实例
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // 获取当前用户
    public User getCurrentUser() {
        return currentUser;
    }

    // 更新用户信息（如登录后调用）
    public void setCurrentUser(User user) {
        this.currentUser = user;
        saveUserToSharedPreferences(user); // 保存到本地存储
    }

    // 从 SharedPreferences 加载用户信息（示例）
    private User loadUserFromSharedPreferences() {
        // 实际逻辑：通过 Context 获取 SharedPreferences 并解析
        return null; // 未登录时返回 null
    }

    // 保存用户信息到 SharedPreferences（示例）
    private void saveUserToSharedPreferences(User user) {
        // 实际逻辑：通过 SharedPreferences.Editor 存储数据
    }

}
