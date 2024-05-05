package com.flyemu.share.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.common.Constants;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Checkout;
import com.flyemu.share.entity.Order;
import com.flyemu.share.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, CheckoutService.Query query, @SaAccountVal AccountDto accountDto) {
        if (accountDto.getOrganizationId() != null) {
            query.setOrganizationId(accountDto.getOrganizationId());
        }
        return JsonResult.successful(checkoutService.query(page, query));
    }

    /**
     * 检查门店是否可以新增初期数据
     * @param accountDto
     * @return
     */
    @GetMapping("/check/data")
    public JsonResult checkInit(@SaAccountVal AccountDto accountDto) {
        return JsonResult.successful(checkoutService.checkInit(accountDto.getOrganizationId()));
    }

    /**
     * 结账、检测
     *
     * @param checkout
     * @return
     */

    @PostMapping("/check")
    public JsonResult check(@RequestBody @Valid Checkout checkout, @SaAccountVal AccountDto accountDto) {
        Map<String, Object> data = new HashMap<>();
        List<Order> notData = checkoutService.check(checkout, accountDto.getOrganizationId(), accountDto.getCheckout());
        data.put("notData", notData);
        if (CollUtil.isEmpty(notData)) {
            SaSession session = StpUtil.getTokenSession();
            Checkout minCheck = checkoutService.queryMinDate(accountDto.getOrganizationId());
            accountDto.setCheckout(minCheck);
            session.set(Constants.SESSION_ACCOUNT, accountDto);
            data.put("minCheck", minCheck);
        }
        return JsonResult.successful(data);
    }

    /**
     * 反结账
     *
     * @param checkout
     * @return
     */
    @PutMapping
    public JsonResult antiCheckout(@RequestBody @Valid Checkout checkout, @SaAccountVal AccountDto accountDto) {
        checkoutService.antiCheckout(checkout, accountDto.getCheckout());
        Checkout minCheck = checkoutService.queryMinDate(accountDto.getOrganizationId());
        SaSession session = StpUtil.getTokenSession();
        accountDto.setCheckout(minCheck);
        session.set(Constants.SESSION_ACCOUNT, accountDto);
        return JsonResult.successful(minCheck);
    }

}
