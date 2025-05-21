// VideoActivity.java（修改后）
package qz.rg.newspaper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import qz.rg.newspaper.R;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private ImageView ivPlay, ivBack, ivMore;
    private TextView tvTitle;
    private LinearLayout llTitleBar;
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // 初始化标题栏控件
        llTitleBar = findViewById(R.id.ll_title_bar);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_news_title);
        ivMore = findViewById(R.id.iv_more);

        // 初始化视频控件
        videoView = findViewById(R.id.VideoView);
        ivPlay = findViewById(R.id.iv_play);

        // 设置返回按钮点击事件（关闭当前Activity）
        ivBack.setOnClickListener(v -> finish());

        // 设置三点按钮点击事件（示例：暂不处理，可扩展）
        ivMore.setOnClickListener(v -> {
            // 后续可添加分享、收藏等功能
        });

        // 接收并显示新闻标题
        String title = getIntent().getStringExtra("news_title");
        tvTitle.setText(title);

        // 初始化视频播放
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.apple;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // 绑定媒体控制器
        controller = new MediaController(this);
        videoView.setMediaController(controller);
        controller.setMediaPlayer(videoView);

        // 播放按钮点击事件
        ivPlay.setOnClickListener(v -> {
            ivPlay.setVisibility(View.INVISIBLE);
            videoView.start();
        });

        // 视频点击事件（显示播放按钮）
        videoView.setOnClickListener(v -> {
            if (!videoView.isPlaying()) {
                ivPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}