package qz.rg.newspaper.bean;

public class SettingItem {
    public static final int TYPE_NORMAL = 1;  // 普通项（点击跳转）
    public static final int TYPE_SWITCH = 2;  // 开关项（带开关）

    private int iconRes;      // 图标资源ID（如 R.drawable.ic_notification）
    private String title;     // 主标题（如 "新消息通知"）
    private String subtitle;  // 副标题（如 "开启后接收推送提醒"）
    private int type;         // 类型（TYPE_NORMAL/TYPE_SWITCH）
    private boolean isChecked;// 开关状态（仅 TYPE_SWITCH 有效）

    // 普通项构造（无开关状态）
    public SettingItem(int iconRes, String title, String subtitle, int type) {
        this.iconRes = iconRes;
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.isChecked = false; // 普通项默认关闭开关（无意义）
    }

    // 开关项构造（含开关状态）
    public SettingItem(int iconRes, String title, String subtitle, int type, boolean isChecked) {
        this.iconRes = iconRes;
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.isChecked = isChecked;
    }

    // Getter 方法
    public int getIconRes() { return iconRes; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public int getType() { return type; }
    public boolean isChecked() { return isChecked; }

    // Setter 方法（仅开关状态可修改）
    public void setChecked(boolean checked) { isChecked = checked; }
}