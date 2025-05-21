package qz.rg.newspaper.bean;

public class FunctionItem {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SWITCH = 1;

    private int iconRes;
    private String title;
    private int type;
    private boolean switchState; // 仅当type为SWITCH时有效

    public FunctionItem(int iconRes, String title, int type) {
        this.iconRes = iconRes;
        this.title = title;
        this.type = type;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }
}
