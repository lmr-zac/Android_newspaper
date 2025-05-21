package qz.rg.newspaper.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        items.add(new FunctionItem(R.drawable.ic_night, "夜间模式", FunctionItem.TYPE_SWITCH));
        items.add(new FunctionItem(R.drawable.ic_settings, "系统设置", FunctionItem.TYPE_NORMAL));
        items.add(new FunctionItem(R.drawable.ic_feedback, "意见反馈", FunctionItem.TYPE_NORMAL));

        adapter = new MyFunctionAdapter(items, new MyFunctionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FunctionItem item) { // 参数改为 FunctionItem
                handleFunctionClick(item); // 传递 item 对象
            }

            @Override
            public void onSwitchChanged(int position, boolean isChecked) {
                if (position == 3) {
                    toggleNightMode(isChecked);
                }
            }
        });
        rvFunctions.setAdapter(adapter);
    }

    private void setupUserInfo() {
        // 从本地获取用户信息
        User user = UserManager.getInstance().getCurrentUser(); // 正确：getInstance()
        if (user != null) {
            Glide.with(this)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_default_avatar)
                    .into(ivAvatar);
            tvNickname.setText(user.getNickname());
        }

        // 点击用户信息区域
        ivAvatar.setOnClickListener(v -> navigateToLoginOrProfile());
        tvNickname.setOnClickListener(v -> navigateToLoginOrProfile());
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
            case "夜间模式": // 设置
                navigateToSettings();
                break;
            case "意见反馈": // 反馈
                navigateToFeedback();
                break;
        }
    }



    private void toggleNightMode(boolean enable) {
        // 实现夜间模式切换逻辑
        AppCompatDelegate.setDefaultNightMode(
                enable ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
        requireActivity().recreate();
    }

    // 其他导航方法...
    private void navigateToLoginOrProfile() {
        // 启动登录或个人资料 Activity
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }
    private void navigateToCollection() {
        // 实现跳转到收藏页面的逻辑（如 Intent 跳转）
    }
    private void navigateToFeedback() {
    }

    private void navigateToSettings() {
    }

    private void navigateToMessages() {
    }

    private void navigateToHistory() {
    }
}