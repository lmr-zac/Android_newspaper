package qz.rg.newspaper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.ContentBlock;
import qz.rg.newspaper.bean.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList;
    private Context context;
    // 点击监听器接口
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    // 设置监听器方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.source.setText(news.getSource());
        holder.time.setText(news.getPublishTime());

        // 提取简介：优先使用 contentBlocks 中的第一个 text 内容
        String content = "";
        List<ContentBlock> blocks = news.getContentBlocks();
        if (blocks != null && !blocks.isEmpty()) {
            for (ContentBlock block : blocks) {
                if ("text".equals(block.getType())) {
                    content = block.getContent();
                    break; // 取第一个文本块作为简介
                }
            }
        }
        // 备用：如果 contentBlocks 中无文本，使用原 content 字段
        if (content.isEmpty()) {
            content = news.getContent() == null ? "" : news.getContent();
        }
        holder.content.setText(content);

        // 提取主图：优先使用 contentBlocks 中的第一个 image 地址
        String imageUrl = "";
        if (blocks != null && !blocks.isEmpty()) {
            for (ContentBlock block : blocks) {
                if ("image".equals(block.getType())) {
                    imageUrl = block.getUrl();
                    break; // 取第一个图片块作为主图
                }
            }
        }
        // 备用：如果 contentBlocks 中无图片，使用原 imageUrl 字段
        if (imageUrl.isEmpty()) {
            imageUrl = news.getImageUrl() == null ? "" : news.getImageUrl();
        }

        // 使用封装的方法加载图片
        loadImage(holder.image, imageUrl);

        // 设置item点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(news); // 传递被点击的新闻对象
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, content, source, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.news_content);
            source = itemView.findViewById(R.id.news_source);
            time = itemView.findViewById(R.id.news_time);
        }
    }

    // 新增：封装图片加载逻辑的方法
    private void loadImage(ImageView imageView, String url) {
        Log.d("NewsAdapter", "尝试加载图片 URL: " + url);
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.error);
            return;
        }

        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "图片加载失败: " + url, e); // 添加异常信息
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }
}