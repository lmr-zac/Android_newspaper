package qz.rg.newspaper.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        return view;
    }




    private void fetchDataFromServer() {
        String serverUrl = Constant.SERVER_URL+ "news.json";
        Log.d("HomeFragment", "请求 URL: " + serverUrl); // 打印 URL
        HttpUtil.sendGetRequest(serverUrl, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("HomeFragment", "响应码: " + response.code()); // 打印状态码
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    Log.d("HomeFragment", "响应数据: " + jsonData); // 打印原始数据
                    parseJsonData(jsonData);
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("HomeFragment", "请求失败: " + e.getMessage()); // 打印错误详情
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
}