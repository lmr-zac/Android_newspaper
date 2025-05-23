package qz.rg.newspaper.bean;

import android.os.Parcel;
import android.os.Parcelable;

// 实现 Parcelable 接口
public class ContentBlock implements Parcelable {
    private String type;    // "text"/"image"/"video"
    private String content; // 文字内容（当type=text时）
    private String url;     // 媒体地址（当type=image/video时）

    // 构造函数
    public ContentBlock() {}

    protected ContentBlock(Parcel in) {
        type = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Parcelable.Creator<ContentBlock> CREATOR = new Parcelable.Creator<ContentBlock>() {
        @Override
        public ContentBlock createFromParcel(Parcel in) {
            return new ContentBlock(in);
        }

        @Override
        public ContentBlock[] newArray(int size) {
            return new ContentBlock[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 实现 describeContents 方法
    @Override
    public int describeContents() {
        return 0;
    }

    // 实现 writeToParcel 方法
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(content);
        dest.writeString(url);
    }
}