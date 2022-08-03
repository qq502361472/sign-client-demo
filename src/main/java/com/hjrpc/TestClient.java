package com.hjrpc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.hjrpc.util.SignUtil;

import java.util.HashMap;
import java.util.Map;

public class TestClient {
    public static void main(String[] args) {
//        String accessKeyId = "test-key1";
//        String accessKeySecret = "L5nqjXlcziKIDa6b";
        String accessKeyId = "test-key2";
        String accessKeySecret = "mSlUAzz5ff9ViP2H";

        // @RequestBody 读取的JSON参数
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("username", "张三");
        bodyMap.put("age", 18);
        String companyName = "一家很强的公司";
        String bodyJson = JSONUtil.toJsonStr(bodyMap);

        // @PathVariable 读取的参数
        int id = 3;

        // @RequestParam （问号拼接的参数）
        Map<String, String[]> params = new HashMap<>();
        params.put("companyName", new String[]{companyName});

        // 当前时间的时间戳
        long timestamp = System.currentTimeMillis();

        // 生成签名字符串
        String sign = SignUtil.generatorSign(bodyJson, params, CollectionUtil.newArrayList(String.valueOf(id))
                , accessKeyId, accessKeySecret, timestamp);
        // 请求url
        String url = "http://localhost:8080/testSignature/" + id + "?companyName=" + companyName;

        HttpResponse httpResponse = HttpRequest.post(url)
                .header("timestamp", String.valueOf(timestamp))
                .header("accessKeyId", accessKeyId)
                .header("sign", sign)
                .form("companyName",companyName)
                .body(bodyJson)
                .execute();
        System.out.println(httpResponse);
    }
}
