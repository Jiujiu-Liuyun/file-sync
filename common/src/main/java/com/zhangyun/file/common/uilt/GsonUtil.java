package com.zhangyun.file.common.uilt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class GsonUtil {
    private static final Gson gson = new GsonBuilder().create();


    public static <T> String toJsonString(T data) {
        return gson.toJson(data);
    }

    public static <T> T fromJson(String str, Type type) {
        return gson.fromJson(str, type);
    }

    public static String prettyPrint(String content) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()   //在序列化的时候不忽略null值
                .create();
        return gson.toJson(JsonParser.parseString(content));
    }
}
