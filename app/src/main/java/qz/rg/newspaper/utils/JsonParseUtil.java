package qz.rg.newspaper.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonParseUtil {
    private static final Gson gson = new Gson(); // 单例 Gson 对象（线程安全）

    /**
     * 解析 JSON 为单个对象
     * @param json JSON 字符串
     * @param clazz 目标对象的 Class（如 News.class）
     * @param <T> 对象类型
     * @return 解析后的对象（失败返回 null）
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {

            return gson.fromJson(json, clazz);//gson.fromJson方法解析JSON数据
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null; // 或抛出自定义异常
        }
    }

    /**
     * 解析 JSON 为对象列表
     * @param json JSON 字符串
     * @param type 目标列表的 Type（如 new TypeToken<List<News>>(){}.getType()）
     * @param <T> 列表元素类型
     * @return 解析后的列表（失败返回空列表）
     */
    public static <T> List<T> parseList(String json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>(); // 或返回 null
        }
    }

    /**
     * 简化版：直接通过 TypeToken 解析列表（无需显式传递 Type）
     * @param json JSON 字符串
     * @param typeToken 目标列表的 TypeToken（如 new TypeToken<List<News>>(){}）
     * @param <T> 列表元素类型
     * @return 解析后的列表（失败返回空列表）
     */
    public static <T> List<T> parseList(String json, TypeToken<List<T>> typeToken) {
        return parseList(json, typeToken.getType());
    }
}
