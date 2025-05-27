package qz.rg.newspaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.News;

public class VideoNewsAdapter extends RecyclerView.Adapter<VideoNewsAdapter.ViewHolder> {

    private Context context;
    private List<News> newsList;
    private OnItemClickListener listener; // 点击监听器

    // 构造函数
    public VideoNewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        // 序号：position+1（从1开始）
        holder.sequence.setText(String.format("%d.", position + 1));
        // 标题：直接使用news的title字段
        holder.title.setText(news.getTitle());

        // 点击事件（跳转到详情页）
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder（绑定新布局中的控件）
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sequence; // 序号
        TextView title;    // 标题

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sequence = itemView.findViewById(R.id.tv_sequence);
            title = itemView.findViewById(R.id.tv_title);
        }
    }

    // 点击监听器接口（与原NewsAdapter一致）
    public interface OnItemClickListener {
        void onItemClick(News news);
    }
}