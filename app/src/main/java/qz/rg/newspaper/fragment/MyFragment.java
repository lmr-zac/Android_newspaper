package qz.rg.newspaper.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import qz.rg.newspaper.activity.CollectionActivity;
import qz.rg.newspaper.activity.FeedbackActivity;
import qz.rg.newspaper.activity.HistoryActivity;
import qz.rg.newspaper.activity.MessagesActivity;
import qz.rg.newspaper.activity.SettingsActivity;
import qz.rg.newspaper.utils.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import qz.rg.newspaper.R;
import qz.rg.newspaper.activity.LoginActivity;
import qz.rg.newspaper.adapter.MyFunctionAdapter;
import qz.rg.newspaper.bean.User;
import qz.rg.newspaper.bean.FunctionItem;


public class MyFragment extends Fragment {

    private RecyclerView rvFunctions;
    private MyFunctionAdapter adapter;
    private ImageView ivAvatar;
    private TextView tvNickname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        // 加载统计卡片Fragment到容器中
        loadUserStatsFragment();
        initViews(view);
        setupFunctions();
        setupUserInfo();
        return view;
    }

    // 加载统计卡片Fragment到容器中
    private void loadUserStatsFragment() {
        // 获取Fragment管理器
        FragmentManager fragmentManager = getChildFragmentManager();
        // 开始Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 创建统计卡片Fragment实例
        UserStatsFragment statsFragment = new UserStatsFragment();

        // 将statsFragment替换到fragment_container容器中
        transaction.replace(R.id.fragment_container, statsFragment);

        // 提交事务（必须调用）
        transaction.commit();
    }

    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvNickname = view.findViewById(R.id.tv_nickname);
        rvFunctions = view.findViewById(R.id.rv_functions);

        // 设置RecyclerView
        rvFunctions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFunctions.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    private void setupFunctions() {
        List<FunctionItem> items = new ArrayList<>();
        items.add(new FunctionItem(R.drawable.ic_collect, "我的收藏", FunctionItem.TYPE_NORMAL));
        items.add(new FunctionItem(R.drawable.ic_history, "浏览历史", FunctionItem.TYPE_NORMAL));
        items.add(new FunctionItem(R.drawable.ic_message, "消息通知", FunctionItem.TYPE_NORMAL));
        items.add(new FunctionItem(R.drawable.ic_settings, "系统设置", FunctionItem.TYPE_NORMAL));
        items.add(new FunctionItem(R.drawable.ic_feedback, "意见反馈", FunctionItem.TYPE_NORMAL));

        adapter = new MyFunctionAdapter(items, new MyFunctionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FunctionItem item) { // 参数改为 FunctionItem
                handleFunctionClick(item); // 传递 item 对象
            }

            @Override
            public void onSwitchChanged(int position, boolean isChecked) {

            }
        });
        rvFunctions.setAdapter(adapter);
    }


    private void handleFunctionClick(FunctionItem item) {
        switch (item.getTitle()) {
            case "我的收藏": // 收藏
                navigateToCollection();
                break;
            case "浏览历史": // 历史
                navigateToHistory();
                break;
            case "消息通知": // 消息
                navigateToMessages();
                break;
            case "系统设置": // 消息
                navigateToSettings();
                break;
            case "意见反馈": // 反馈
                navigateToFeedback();
                break;
        }
    }

    private void setupUserInfo() {
        // 从本地获取用户信息
        User user = UserManager.getInstance().getCurrentUser();
        if (user != null) {
            // 将String类型的资源ID转换为int
            try {
                int avatarResId = Integer.parseInt(user.getAvatarUrl());
                // 加载本地资源ID对应的图片
                Glide.with(this)
                        .load(avatarResId)  // 直接加载int类型的资源ID
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(ivAvatar);
                tvNickname.setText(user.getNickname());
            } catch (NumberFormatException e) {
                // 处理转换失败的情况（如avatarUrl格式错误）
                ivAvatar.setImageResource(R.drawable.ic_default_avatar);
                e.printStackTrace();
            }
        } else {
            // 未登录状态
            ivAvatar.setImageResource(R.drawable.ic_default_avatar);
            tvNickname.setText("点击登录");
        }

        // 点击事件保持不变
        ivAvatar.setOnClickListener(v -> navigateToLoginOrProfile());
        tvNickname.setOnClickListener(v -> navigateToLoginOrProfile());
    }

    // 在MyFragment的onActivityResult方法中添加（需重写）
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) { // 1001是登录请求码
            setupUserInfo(); // 重新加载用户信息
        }
    }

    // 其他导航方法...
    private void navigateToLoginOrProfile() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivityForResult(intent, 1001); // 使用startActivityForResult
    }
    private void navigateToCollection() {
        // 跳转到我的收藏页面
        Intent intent = new Intent(requireContext(), CollectionActivity.class);
        startActivity(intent);
    }

    private void navigateToHistory() {
        // 跳转到浏览历史页面
        Intent intent = new Intent(requireContext(), HistoryActivity.class);
        startActivity(intent);
    }

    private void navigateToMessages() {
        // 跳转到消息通知页面
        Intent intent = new Intent(requireContext(), MessagesActivity.class);
        startActivity(intent);
    }

    private void navigateToSettings() {
        // 跳转到系统设置页面
        Intent intent = new Intent(requireContext(), SettingsActivity.class);
        startActivity(intent);
    }
    private void navigateToFeedback() {
        Intent intent = new Intent(requireContext(), FeedbackActivity.class);
        startActivity(intent);
    }
}