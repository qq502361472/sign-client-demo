package com.hjrpc.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class SignUtil {

    public static String generatorSign(String body, Map<String, String[]> params, Collection<String> paths
            , String accessKeyId, String accessKeySecret, long timestamp) {
        String allParamsString = getAllParamsString(body, params, paths, accessKeyId, timestamp);
        return generatorSign(allParamsString, accessKeySecret);
    }

    public static String generatorSign(String allParamsString, String accessKeySecret) {
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, accessKeySecret.getBytes(StandardCharsets.UTF_8));
        return hMac.digestHex(allParamsString);
    }

    public static String getAllParamsString(String body, Map<String, String[]> params, Collection<String> paths
            , String accessKeyId, long timestamp) {
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotBlank(body)) {
            sb.append(body).append('#');
        }

        if (CollectionUtil.isNotEmpty(params)) {
            params.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(paramEntry -> {
                        String paramValue = Arrays.stream(paramEntry.getValue()).sorted().collect(Collectors.joining(","));
                        sb.append(paramEntry.getKey()).append("=").append(paramValue).append('#');
                    });
        }

        if (CollectionUtil.isNotEmpty(paths)) {
            String pathValues = String.join(",", paths);
            sb.append(pathValues).append('#');
        }

        // 拼接secret和时间戳
        sb.append("accessKeyId=")
                .append(accessKeyId)
                .append("#timestamp=").append(timestamp);
        return sb.toString();
    }
}
