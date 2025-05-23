package qz.rg.newspaper.bean;

import android.os.Parcel;
import android.os.Parcelable;
import qz.rg.newspaper.bean.ContentBlock;
import java.util.List;

public class News implements Parcelable {
    private String title;       // 新闻标题
    private String content;     // 新闻简介
    private String imageUrl;    // 图片URL
    private String source;      // 新闻来源
    private String publishTime; // 发布时间
    private List<ContentBlock> contentBlocks; // 新增：多内容块列表

    public List<ContentBlock> getContentBlocks() {
        return contentBlocks;
    }

    public void setContentBlocks(List<ContentBlock> contentBlocks) {
        this.contentBlocks = contentBlocks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    // Parcelable实现
    protected News(Parcel in) {
        title = in.readString();
        content = in.readString();
        imageUrl = in.readString();
        source = in.readString();
        publishTime = in.readString();
        // 读取contentBlocks列表（需处理List<ContentBlock>的反序列化）
        contentBlocks = in.createTypedArrayList(ContentBlock.CREATOR);
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(imageUrl);
        dest.writeString(source);
        dest.writeString(publishTime);
        // 序列化contentBlocks列表
        dest.writeTypedList(contentBlocks);
    }
}
