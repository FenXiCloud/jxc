package com.flyemu.share.api;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyemu.share.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public final class Fenxi {
    private final static TimedCache<String,String> timedCache = CacheUtil.newTimedCache(300000);

    private final String API_SITE = "http://localhost:8012";

    public JSONObject loadAccountSets() {
        JSONObject res = this.executeJson(Dict.create(), "/init");
        log.info("帐套信息", res);
        return res.getJSONObject("data").getJSONObject("user");
    }

    public JSONArray loadSubject(Long accountSetsId) {
        JSONObject res = this.executeJson(Dict.create(), "/accountBook/subject/"+accountSetsId);
        log.info("帐套信息", res);
        return res.getJSONArray("data");
    }

    private JSONObject executeJson(Dict params, String api) {
        HttpRequest request = HttpUtil.createGet(API_SITE + api);
        request.header("cookie","fv3token=c3032c9f-93f1-4669-9f1d-87c9d41b87cc; Max-Age=2592000; Expires=Thu, 27 Jun 2024 17:01:06 +0800; Path=/");
        HttpResponse response = request.execute();
        if (response.isOk()) {
            log.debug("workflowFormsSchemasProcessCodes：{}", response.bodyBytes());
            return JSON.parseObject(response.bodyBytes());
        } else {
            throw new ServiceException("workflowFormsSchemasProcessCodes HttpCode：" + response.getStatus());
        }
    }

    private JSONObject execute(HttpRequest post, int retry) {
        HttpResponse response = post.execute();
        if (response.isOk()) {
            JSONObject jsonObject = JSON.parseObject(response.body());
            if (0 == jsonObject.getIntValue("error_code")) {
                return jsonObject;
            } else if (88 == jsonObject.getIntValue("error_code") && retry < 10) {
                log.error("限流了，2后准备重试...");
                ThreadUtil.safeSleep(2);
                return execute(post, retry + 1);
            }
            throw new HttpException(jsonObject.getString("errmsg"));
        }
        throw new HttpException("{}请求失败", post.getUrl());
    }
}
