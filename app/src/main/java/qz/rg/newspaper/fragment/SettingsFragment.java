package qz.rg.newspaper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.adapter.SettingsAdapter;
import qz.rg.newspaper.bean.SettingItem;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView rvSettings = view.findViewById(R.id.rv_settings);
        rvSettings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSettings.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        // 初始化设置项数据
        List<SettingItem> settings = new ArrayList<>();
        settings.add(new SettingItem(R.drawable.ic_notification, "新消息通知", "开启后接收推送提醒", SettingItem.TYPE_SWITCH, true));
        settings.add(new SettingItem(R.drawable.ic_brightness, "自动调节亮度", "根据环境光调整屏幕亮度", SettingItem.TYPE_SWITCH, false));
        settings.add(new SettingItem(R.drawable.ic_storage, "清理缓存", "当前缓存：23MB", SettingItem.TYPE_NORMAL));
        settings.add(new SettingItem(R.drawable.ic_about, "关于我们", "版本：1.2.3", SettingItem.TYPE_NORMAL));

        // 设置适配器
        SettingsAdapter adapter = new SettingsAdapter(settings);
        rvSettings.setAdapter(adapter);

        // 设置点击事件
        adapter.setOnItemClickListener((position, item) -> {
            if (item.getType() == SettingItem.TYPE_NORMAL) {
                handleNormalItemClick(item);
            } else {
                handleSwitchItemChange(item);
            }
        });
    }

    private void handleNormalItemClick(SettingItem item) {
        // 普通项点击逻辑（如跳转关于页）
        if ("关于我们".equals(item.getTitle())) {
            // startActivity(new Intent(getContext(), AboutActivity.class));
        }
    }

    private void handleSwitchItemChange(SettingItem item) {
        // 开关状态变更逻辑（如保存到本地）
        // SharedPreferencesUtil.saveBoolean(item.getTitle(), item.isChecked());
    }
}