package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.flyemu.share.annotation.SaAdminId;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.common.Constants;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.RelationCw;
import com.flyemu.share.service.RelationCwService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relationCw")
@RequiredArgsConstructor
@SaCheckLogin
public class RelationCwController {
    private final RelationCwService relationCwService;

    /**
     * 加载对应的帐套信息
     * @return
     */
    @GetMapping("/loadAccountSets")
    public JsonResult loadAccountSets() {
        return JsonResult.successful(relationCwService.loadAccountSets());
    }

    /**
     * 加载组织关联财务系统信息
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping
    public JsonResult load(@SaMerchantId Long merchantId, @SaOrganizationId Long organizationId){
        return JsonResult.successful(relationCwService.load(merchantId,organizationId));
    }

    /**
     * 保存或更新关联信息
     *
     * @param relationCw
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid RelationCw relationCw, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId, @SaAdminId Long adminId) {
        relationCw.setMerchantId(merchantId);
        relationCw.setAdminId(adminId);
        relationCw.setOrganizationId(organizationId);
        Long accountSetsId =  relationCwService.save(relationCw);

        AccountDto accountDto = (AccountDto) StpUtil.getTokenSession().get(Constants.SESSION_ACCOUNT);
        accountDto.setAccountSetsId(accountSetsId);
        SaSession session = StpUtil.getTokenSession();
        session.set(Constants.SESSION_ACCOUNT, accountDto);
        return JsonResult.successful();
    }

}
