// NewsDetailActivity.java（修改后）
package qz.rg.newspaper.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.ContentBlock;
import qz.rg.newspaper.bean.News;

public class NewsDetailActivity extends AppCompatActivity {
    private LinearLayout llContent; // 内容容器
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // 初始化控件
        llContent = findViewById(R.id.ll_content);
        tvTitle = findViewById(R.id.tv_title);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        // 获取传递的News对象（需确保News实现Parcelable）
        News news = getIntent().getParcelableExtra("news");
        if (news == null) {
            finish();
            return;
        }
        tvTitle.setText(news.getTitle());

        // 渲染内容块
        renderContentBlocks(news.getContentBlocks());
    }

    private void renderContentBlocks(List<ContentBlock> blocks) {
        for (ContentBlock block : blocks) {
            switch (block.getType()) {
                case "text":
                    addTextView(block.getContent());
                    break;
                case "image":
                    addImageView(block.getUrl());
                    break;
                case "video":
                    addVideoView(block.getUrl());
                    break;
            }
        }
    }

    // 添加文字块
    private void addTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setLineSpacing(8, 1.2f); // 行间距
        tv.setPadding(0, 16, 0, 16); // 上下内边距
        llContent.addView(tv);
    }

    // 添加图片块
    private void addImageView(String imageUrl) {
        ImageView iv = new ImageView(this);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300 // 图片高度固定300dp，可根据需求调整
        ));
        iv.setPadding(0, 8, 0, 8);

        // 使用Glide加载网络图片
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(iv);
        llContent.addView(iv);
    }

    // 添加视频块
    private void addVideoView(String videoUrl) {
        VideoView videoView = new VideoView(this);
        videoView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300 // 视频区域高度
        ));
        videoView.setPadding(0, 8, 0, 8);

        // 绑定媒体控制器
        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        controller.setMediaPlayer(videoView);

        // 加载视频
        videoView.setVideoURI(Uri.parse(videoUrl));
        llContent.addView(videoView);
    }
}