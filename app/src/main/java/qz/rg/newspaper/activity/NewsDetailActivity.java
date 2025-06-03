// NewsDetailActivity.java（修改后）
package qz.rg.newspaper.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.ContentBlock;
import qz.rg.newspaper.bean.News;

public class NewsDetailActivity extends AppCompatActivity {
    private LinearLayout llContent; // 内容容器
    private TextView tvTitle;
    private List<MediaPlayer> mediaPlayers = new ArrayList<>(); // 管理所有MediaPlayer实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // 初始化控件
        llContent = findViewById(R.id.ll_content);
        tvTitle = findViewById(R.id.tv_title);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        // 获取传递的News对象
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
                300 // 图片高度固定300dp
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
        // 1. 创建SurfaceView用于显示视频画面
        SurfaceView surfaceView = new SurfaceView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300 // 视频区域高度
        );
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        surfaceView.setLayoutParams(layoutParams);
        surfaceView.setPadding(0, 8, 0, 8);
        llContent.addView(surfaceView);

        // 2. 获取SurfaceHolder并设置回调（监听Surface生命周期）
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            private MediaPlayer mediaPlayer; // 声明MediaPlayer（需在合适生命周期释放）
            private MediaController mediaController;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Surface创建完成，初始化MediaPlayer
                mediaPlayer = new MediaPlayer();
                try {
                    // 设置数据源
                    mediaPlayer.setDataSource(NewsDetailActivity.this, Uri.parse(videoUrl));
                    // 关联Surface显示画面
                    mediaPlayer.setDisplay(holder);
                    // 异步准备（避免阻塞主线程）
                    mediaPlayer.prepareAsync();

                    // 设置准备完成监听（准备完成后才能播放）
                    mediaPlayer.setOnPreparedListener(mp -> {
                        // 初始化媒体控制器（绑定到当前视频区域）
                        mediaController = new MediaController(NewsDetailActivity.this);
                        mediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
                            @Override public void start() { mediaPlayer.start(); }
                            @Override public void pause() { mediaPlayer.pause(); }
                            @Override public int getDuration() { return mediaPlayer.getDuration(); }
                            @Override public int getCurrentPosition() { return mediaPlayer.getCurrentPosition(); }
                            @Override public void seekTo(int pos) { mediaPlayer.seekTo(pos); }
                            @Override public boolean isPlaying() { return mediaPlayer.isPlaying(); }
                            @Override public int getBufferPercentage() { return 0; } // 可选实现缓冲进度
                            @Override public boolean canPause() { return true; }
                            @Override public boolean canSeekBackward() { return true; }
                            @Override public boolean canSeekForward() { return true; }
                            @Override public int getAudioSessionId() { return mediaPlayer.getAudioSessionId(); }
                        });
                        mediaController.setAnchorView(surfaceView); // 控制器锚定到SurfaceView
                        mediaController.show(); // 显示控制器

                        // 设置SurfaceView的点击监听
                        surfaceView.setOnClickListener(v -> {
                            if (mediaController != null) {
                                mediaController.show(); // 点击时重新显示控制器
                            }
                        });
                    });

                    // 设置错误监听
                    mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                        // 处理播放错误（如显示提示）
                        Toast.makeText(NewsDetailActivity.this, "视频播放失败", Toast.LENGTH_SHORT).show();
                        return true; // 标记错误已处理
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NewsDetailActivity.this, "视频地址错误", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Surface尺寸变化时（如屏幕旋转），可调整MediaPlayer参数（可选）
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // Surface销毁时释放MediaPlayer
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                if (mediaController != null) {
                    mediaController.hide();
                    mediaController = null;
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 暂停所有正在播放的视频
        for (MediaPlayer mp : mediaPlayers) {
            if (mp.isPlaying()) {
                mp.pause();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放所有MediaPlayer资源
        for (MediaPlayer mp : mediaPlayers) {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
        }
        mediaPlayers.clear();
    }
}