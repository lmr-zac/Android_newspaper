package qz.rg.newspaper.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.User;
import qz.rg.newspaper.utils.UserManager;

public class UserStatsFragment extends Fragment {
    private TextView tvReadCount, tvCollectCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_stats, container, false);
        tvReadCount = view.findViewById(R.id.tv_read_count);
        tvCollectCount = view.findViewById(R.id.tv_collect_count);
        checkLoginAndLoadData();
        //返回给HomeFragment
        return inflater.inflate(R.layout.fragment_user_stats, container, false);
    }

    private void checkLoginAndLoadData() {
        User user = UserManager.getInstance().getCurrentUser();
        if (user == null) {
            tvReadCount.setText("登录后查看");
            tvCollectCount.setText("登录后查看");
            return;
        }
        // 模拟加载数据（实际从网络/数据库获取）
        loadStatsFromServer(user.getUserId());
    }

    private void loadStatsFromServer(String userId) {
        // 假设获取到阅读量 128，收藏量 23
        animateCount(tvReadCount, 0, 128);
        animateCount(tvCollectCount, 0, 23);
    }

    private void animateCount(TextView textView, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(800);
        animator.addUpdateListener(animation -> {
            textView.setText(String.valueOf(animation.getAnimatedValue()));
        });
        animator.start();
    }
}
