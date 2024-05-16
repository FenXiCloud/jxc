package com.flyemu.share.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.common.Constants;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Checkout;
import com.flyemu.share.entity.Customers;
import com.flyemu.share.entity.Order;
import com.flyemu.share.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
     * 新增商户
     *
     * @param checkout
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid Checkout checkout, @SaOrganizationId Long organizationId, @SaMerchantId Long merchantId) {
        Assert.isNull(checkout.getId(), "新增商户Id必须为空~");
        checkout.setMerchantId(merchantId);
        checkout.setOrganizationId(organizationId);
        checkoutService.save(checkout);
        return JsonResult.successful();
    }
    /**
     * 反结账
     *
     * @param checkout
     * @return
     */
    @PutMapping
    public JsonResult antiCheckout(@RequestBody @Valid Checkout checkout, @SaAccountVal AccountDto accountDto) {
        LocalDate checkDate = checkoutService.antiCheckout(accountDto.getOrganizationId(),accountDto.getMerchantId());
        SaSession session = StpUtil.getTokenSession();
        accountDto.setCheckDate(checkDate);
        session.set(Constants.SESSION_ACCOUNT, accountDto);
        return JsonResult.successful(checkDate);
    }

}
