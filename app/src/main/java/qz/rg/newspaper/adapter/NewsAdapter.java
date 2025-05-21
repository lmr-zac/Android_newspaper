package qz.rg.newspaper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import qz.rg.newspaper.bean.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList;
    private Context context;
    // 新增：点击监听器接口
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(News news);
    }
    // 新增：设置监听器方法
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
        holder.content.setText(news.getContent());
        holder.source.setText(news.getSource());
        holder.time.setText(news.getPublishTime());

        // 使用 Glide 加载图片
        Glide.with(context)
                .load(news.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "图片加载失败: " + news.getImageUrl());
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.image);

        // 新增：设置item点击事件
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
}