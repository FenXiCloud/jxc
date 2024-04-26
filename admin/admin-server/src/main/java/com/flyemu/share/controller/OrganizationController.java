package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.Organization;
import com.flyemu.share.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
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
    public JsonResult list(Page page, OrganizationService.Query query) {
        return JsonResult.successful(organizationService.query(page, query));
    }

    @GetMapping("all")
    public JsonResult listAll(Integer merchantId) {
        return JsonResult.successful(organizationService.listAll(merchantId));
    }

    /**
     * 新增组织
     *
     * @param organization
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid Organization organization) {
        Assert.isNull(organization.getId(), "新增组织Id必须为空~");
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
        organizationService.save(organization);
        return JsonResult.successful();
    }

    /**
     * 删除组织
     *
     * @param organizationId
     * @return
     */
    @DeleteMapping("/{organizationId}")
    public JsonResult delete(@PathVariable Integer organizationId) {
        organizationService.delete(organizationId);
        return JsonResult.successful();
    }
}
