package qz.rg.newspaper.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import qz.rg.newspaper.R;
import qz.rg.newspaper.activity.NewsDetailActivity;
import qz.rg.newspaper.adapter.NewsAdapter;
import qz.rg.newspaper.bean.News;
import qz.rg.newspaper.utils.Constant;
import qz.rg.newspaper.utils.HttpUtil;
import qz.rg.newspaper.utils.JsonParseUtil;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList = new ArrayList<>();

    private ImageView ivRefreshCircle;
    private RotateAnimation rotateAnimation;

    private float startY; // 触摸起始Y坐标
    private boolean isRefreshing = false; // 是否正在刷新
    private static final int TRIGGER_DISTANCE = 150; // 触发刷新的最小下拉距离（dp）

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initViews(view);
        fetchDataFromServer();

        // 跳转：设置适配器点击监听器
        adapter.setOnItemClickListener(news -> {
            // 创建 Intent 跳转到 NewsDetailActivity
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news", news); // 传递新闻标题
            startActivity(intent); // 启动 NewsDetailActivity
        });

        ivRefreshCircle = view.findViewById(R.id.iv_refresh_circle);
        recyclerView = view.findViewById(R.id.recycler_view);

        // 初始化旋转动画
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate);

        // 监听RecyclerView的触摸事件
        recyclerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = event.getY(); // 记录按下位置
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isRefreshing) break; // 正在刷新时不处理
                    float currentY = event.getY();
                    float deltaY = currentY - startY; // 下拉距离
                    if (deltaY > 0 && recyclerView.getChildAt(0).getTop() == 0) { // 顶部下拉
                        // 根据下拉距离调整刷新圆的透明度和大小（可选）
                        float progress = deltaY / TRIGGER_DISTANCE;
                        ivRefreshCircle.setAlpha(Math.min(progress, 1f)); // 透明度0~1
                        ivRefreshCircle.setScaleX(Math.min(progress, 1f)); // 缩放0~1
                        ivRefreshCircle.setScaleY(Math.min(progress, 1f));
                        ivRefreshCircle.setVisibility(View.VISIBLE);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float finalDeltaY = event.getY() - startY;
                    if (finalDeltaY >= TRIGGER_DISTANCE) { // 达到触发距离
                        startRefresh();
                    } else {
                        // 未达到触发距离，隐藏刷新圆
                        ivRefreshCircle.setVisibility(View.GONE);
                        ivRefreshCircle.setAlpha(1f);
                        ivRefreshCircle.setScaleX(1f);
                        ivRefreshCircle.setScaleY(1f);
                    }
                    break;
            }
            return false;
        });

        return view;
    }




    private void fetchDataFromServer() {
        String serverUrl = Constant.SERVER_URL+ "news.json";
        //发送GET请求
        HttpUtil.sendGetRequest(serverUrl, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    parseJsonData(jsonData);
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "网络请求失败", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter(getContext(), newsList);
        recyclerView.setAdapter(adapter);
    }

    private void parseJsonData(String json) {
        Type listType = new TypeToken<ArrayList<News>>() {
        }.getType();
        //调用JsonParseUtil
        List<News> tempList = JsonParseUtil.parseList(json, listType);
        Log.d("HomeFragment", "解析到数据条数: " + tempList.size()); // 检查数据量

        // 验证 contentBlocks 是否解析成功（可选）
        for (News news : tempList) {
            Log.d("HomeFragment", "新闻标题: " + news.getTitle());
            if (news.getContentBlocks() != null) {
                Log.d("HomeFragment", "contentBlocks 数量: " + news.getContentBlocks().size());
            }
        }

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                newsList.clear();
                newsList.addAll(tempList);
                adapter.notifyDataSetChanged();
            });
        }else {
            // 解析失败处理（如提示用户）
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), "数据解析失败", Toast.LENGTH_SHORT).show()
            );
        }
    }

    // 开始刷新（启动动画+模拟网络请求）
    private void startRefresh() {
        isRefreshing = true;
        ivRefreshCircle.startAnimation(rotateAnimation); // 启动旋转动画
        // 模拟网络请求（2秒后结束）
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            stopRefresh();
        }, 2000);
    }

    // 结束刷新（停止动画+隐藏）
    private void stopRefresh() {
        isRefreshing = false;
        ivRefreshCircle.clearAnimation(); // 停止动画
        ivRefreshCircle.setVisibility(View.GONE);
    }
}