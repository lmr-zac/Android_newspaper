package qz.rg.newspaper.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    // 静态方法：发送 GET 请求
    public static void sendGetRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();//管理 HTTP 请求的连接、超时、重试等策略
        //构建 HTTP 请求对象 Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        // 异步执行请求并回调结果
        client.newCall(request).enqueue(callback);
    }
}
