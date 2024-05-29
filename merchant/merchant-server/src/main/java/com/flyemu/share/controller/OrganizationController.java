package com.flyemu.share.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.common.Constants;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.dto.OrganizationDto;
import com.flyemu.share.entity.Organization;
import com.flyemu.share.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;


    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, @SaMerchantId Long merchantId, OrganizationService.Query query) {
        query.setMerchantId(merchantId);
        return JsonResult.successful(organizationService.query(page, query));
    }

    @GetMapping("all")
    public JsonResult listAll(@SaMerchantId Long merchantId) {
        return JsonResult.successful(organizationService.listAll(merchantId));
    }

    /**
     * 新增组织
     *
     * @param organization
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid OrganizationDto organization, @SaMerchantId Long merchantId) {
        Assert.isNull(organization.getId(), "新增组织Id必须为空~");
        organization.setCurrent(false);
        organization.setMerchantId(merchantId);
        organization.setEnabled(true);
        organization.setStartDate(organization.getStartDate());
        organizationService.save(organization);
        return JsonResult.successful();
    }

    /**
     * 更新组织
     *
     * @param organization
     * @return
     */
    @PutMapping
    public JsonResult update(@RequestBody @Valid Organization organization) {
        Assert.notNull(organization.getId(), "更新组织Id不允许为空~");
        organizationService.update(organization);
        return JsonResult.successful();
    }

    /**
     * 修改默认组织
     * @param merchantId
     * @param orgId
     * @return
     */
    @PutMapping("/current/{orgId}")
    public  JsonResult changeCurrent(@SaMerchantId Long merchantId,@PathVariable Long orgId){
        Organization organization = organizationService.changeOrgCurrent(merchantId,orgId);
        AccountDto accountDto = (AccountDto) StpUtil.getTokenSession().get(Constants.SESSION_ACCOUNT);
        accountDto.setOrganization(organization);
        SaSession session = StpUtil.getTokenSession();
        session.set(Constants.SESSION_ACCOUNT, accountDto);
        return JsonResult.successful();
    }

    /**
     * 删除组织
     *
     * @param organizationId
     * @return
     */
    @DeleteMapping("/{organizationId}")
    public JsonResult delete(@PathVariable Long organizationId, Long merchantId) {
        organizationService.delete(merchantId, organizationId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaMerchantId Long merchantId) {
        return JsonResult.successful(organizationService.select(merchantId));
    }
}
