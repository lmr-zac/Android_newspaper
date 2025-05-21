package qz.rg.newspaper.adapter;

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
import qz.rg.newspaper.bean.FunctionItem;

public class MyFunctionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FunctionItem> items;
    private OnItemClickListener listener; // 修正：使用当前适配器的接口

    public MyFunctionAdapter(List<FunctionItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == FunctionItem.TYPE_NORMAL) {
            View view = inflater.inflate(R.layout.item_function_normal, parent, false);
            return new NormalViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_function_switch, parent, false);
            return new SwitchViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FunctionItem item = items.get(position);
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalHolder = (NormalViewHolder) holder;
            normalHolder.ivIcon.setImageResource(item.getIconRes());
            normalHolder.tvTitle.setText(item.getTitle());
            // 传递当前 item 而非 position
            normalHolder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        } else if (holder instanceof SwitchViewHolder) {
            SwitchViewHolder switchHolder = (SwitchViewHolder) holder;
            switchHolder.switchView.setText(item.getTitle());
            switchHolder.switchView.setChecked(item.isSwitchState());
            switchHolder.switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setSwitchState(isChecked);
                listener.onSwitchChanged(position, isChecked);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    // 点击回调接口：参数改为 FunctionItem 对象（或 News 对象，根据实际数据模型调整）
    public interface OnItemClickListener {
        void onItemClick(FunctionItem item); // 传入当前点击的功能项对象
        void onSwitchChanged(int position, boolean isChecked);
    }

    // 普通功能项 ViewHolder
    static class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_function_icon);
            tvTitle = itemView.findViewById(R.id.tv_function_title);
        }
    }

    // 带开关的功能项 ViewHolder
    static class SwitchViewHolder extends RecyclerView.ViewHolder {
        SwitchMaterial switchView;

        public SwitchViewHolder(@NonNull View itemView) {
            super(itemView);
            switchView = itemView.findViewById(R.id.switch_function);
        }
    }
}