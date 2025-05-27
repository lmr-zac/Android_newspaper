package qz.rg.newspaper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import qz.rg.newspaper.adapter.VideoNewsAdapter;
import qz.rg.newspaper.bean.News;
import qz.rg.newspaper.utils.Constant;
import qz.rg.newspaper.utils.HttpUtil;
import qz.rg.newspaper.utils.JsonParseUtil;

public class HotFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideoNewsAdapter adapter;
    private List<News> hotNewsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);          // 初始化RecyclerView和适配器
        initSwipeRefresh(view);   // 初始化下拉刷新
        fetchHotNewsFromServer(); // 首次加载数据
    }

    /**
     * 初始化RecyclerView和适配器（复用HomeFragment逻辑）
     */
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new VideoNewsAdapter(getContext(), hotNewsList);
        recyclerView.setAdapter(adapter);

        // 设置新闻点击跳转（与HomeFragment一致）
        ((VideoNewsAdapter) adapter).setOnItemClickListener(news -> {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news", news);
            startActivity(intent);
        });
    }

    /**
     * 初始化下拉刷新组件
     */
    private void initSwipeRefresh(View view) {
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        // 设置刷新进度条颜色（与应用主题一致）
        swipeRefresh.setColorSchemeResources(R.color.teal_200, R.color.purple_500);
        // 下拉刷新监听：重新加载数据
        swipeRefresh.setOnRefreshListener(this::fetchHotNewsFromServer);
    }

    /**
     * 从服务器获取热点新闻数据（核心网络请求逻辑）
     */
    private void fetchHotNewsFromServer() {
        // 构造热点新闻接口URL（假设后端接口为 hot_news.json）
        String hotNewsUrl = Constant.SERVER_URL + "hot_news.json";

        HttpUtil.sendGetRequest(hotNewsUrl, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showToast("网络请求失败（状态码：" + response.code() + "）");
                    return;
                }

                // 解析响应数据
                String jsonData = response.body().string();
                parseNewsData(jsonData);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 网络请求失败（如无网络）
                showToast("网络连接失败：" + e.getMessage());
            }
        });
    }

    /**
     * 解析JSON数据并更新列表（复用HomeFragment解析逻辑）
     */
    private void parseNewsData(String json) {
        // 定义Gson的TypeToken（与HomeFragment一致）
        Type listType = new TypeToken<ArrayList<News>>() {}.getType();
        List<News> tempList = JsonParseUtil.parseList(json, listType);

        // 主线程更新UI
        requireActivity().runOnUiThread(() -> {
            swipeRefresh.setRefreshing(false); // 停止刷新动画
            if (tempList == null || tempList.isEmpty()) {
                showToast("暂无热点新闻数据");
                return;
            }

            // 更新列表数据
            hotNewsList.clear();
            hotNewsList.addAll(tempList);
            adapter.notifyDataSetChanged();
        });
    }

    /**
     * 主线程显示Toast（封装方法）
     */
    private void showToast(String message) {
        requireActivity().runOnUiThread(() -> {
            swipeRefresh.setRefreshing(false); // 失败时停止刷新
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}