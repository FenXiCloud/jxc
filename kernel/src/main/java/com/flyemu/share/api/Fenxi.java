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
import com.flyemu.share.dto.VoucherDto;
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
        JSONObject res = this.executeJson(Dict.create(), "/subject/voucher/select/"+accountSetsId);
        log.info("科目", res);
        return res.getJSONArray("data");
    }

    public JSONObject loadVoucher(Long accountSetsId,Long voucherId) {
        JSONObject res = this.executeJson(Dict.create(), "/voucher/"+accountSetsId+"/"+voucherId);
        log.info("凭证详情", res);
        return res.getJSONObject("data");
    }


    public JSONObject createVoucher(Long accountSetsId, VoucherDto dto) {
        HttpRequest post = HttpUtil.createPost(API_SITE + "/voucher/"+accountSetsId);
        post.body(JSON.toJSONString(dto), "application/json");
        return execute(post, 0);
    }

    private JSONObject executeJson(Dict params, String api) {
        HttpRequest request = HttpUtil.createGet(API_SITE + api);
        request.header("cookie","fv3token=f74f2747-eab1-4784-add1-4c8f918a49d9; Max-Age=2592000; Expires=Fri, 28 Jun 2024 14:27:40 +0800; Path=/");
        HttpResponse response = request.execute();
        if (response.isOk()) {
            log.debug("workflowFormsSchemasProcessCodes：{}", response.bodyBytes());
            return JSON.parseObject(response.bodyBytes());
        } else {
            throw new ServiceException("workflowFormsSchemasProcessCodes HttpCode：" + response.getStatus());
        }
    }

    private JSONObject execute(HttpRequest post, int retry) {
        post.header("cookie","fv3token=f74f2747-eab1-4784-add1-4c8f918a49d9; Max-Age=2592000; Expires=Fri, 28 Jun 2024 14:27:40 +0800; Path=/");
        HttpResponse response = post.execute();
        if (response.isOk()) {
            JSONObject jsonObject = JSON.parseObject(response.body());
            if (0 == jsonObject.getIntValue("error_code")) {
                return jsonObject.getJSONObject("data");
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
