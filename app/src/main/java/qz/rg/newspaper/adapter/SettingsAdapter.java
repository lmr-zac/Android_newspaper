package qz.rg.newspaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.SettingItem;

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SettingItem> settings;
    private static OnItemClickListener listener;

    public SettingsAdapter(List<SettingItem> settings) {
        this.settings = settings;
    }

    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return settings.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == SettingItem.TYPE_SWITCH) {
            // 加载开关项布局
            View view = inflater.inflate(R.layout.item_setting_switch, parent, false);
            return new SwitchViewHolder(view);
        } else {
            // 加载普通项布局
            View view = inflater.inflate(R.layout.item_setting_normal, parent, false);
            return new NormalViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettingItem item = settings.get(position);
        if (holder instanceof SwitchViewHolder) {
            ((SwitchViewHolder) holder).bind(item);
        } else {
            ((NormalViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    // 普通项 ViewHolder
    static class NormalViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivIcon;
        private final TextView tvTitle;
        private final TextView tvSubtitle;

        NormalViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        }

        void bind(SettingItem item) {
            ivIcon.setImageResource(item.getIconRes());
            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSubtitle());
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(getAdapterPosition(), item);
            });
        }
    }

    // 开关项 ViewHolder
    static class SwitchViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivIcon;
        private final TextView tvTitle;
        private final TextView tvSubtitle;
        private final SwitchMaterial switchBtn;

        SwitchViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            switchBtn = itemView.findViewById(R.id.switch_btn);
        }

        void bind(SettingItem item) {
            ivIcon.setImageResource(item.getIconRes());
            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSubtitle());
            switchBtn.setChecked(item.isChecked());
            switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
                if (listener != null) listener.onItemClick(getAdapterPosition(), item);
            });
        }
    }

    // 点击监听器接口
    public interface OnItemClickListener {
        void onItemClick(int position, SettingItem item);
    }
}