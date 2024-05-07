package com.flyemu.share.controller;

import cn.hutool.core.lang.Assert;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.entity.Warehouses;
import com.flyemu.share.service.WarehousesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehousesController {

    private final WarehousesService warehousesService;


    @GetMapping
    public JsonResult list(Page page,WarehousesService.Query query ,@SaAccountVal AccountDto accountDto) {
        query.setOrganizationId(accountDto.getOrganizationId());
        query.setMerchantId(accountDto.getMerchantId());
        return JsonResult.successful(warehousesService.query(page,query));
    }


    @PostMapping
    public JsonResult save(@RequestBody @Valid Warehouses warehouses, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        Assert.isNull(warehouses.getId(), "新增Id必须为空~");
        warehouses.setMerchantId(merchantId);
        warehouses.setOrganizationId(organizationId);
        warehousesService.save(warehouses);
        return JsonResult.successful();
    }


    @PutMapping
    public JsonResult update(@RequestBody @Valid Warehouses warehouses, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        Assert.notNull(warehouses.getId(), "更新Id不允许为空~");
        warehouses.setMerchantId(merchantId);
        warehouses.setOrganizationId(organizationId);
        warehousesService.save(warehouses);
        return JsonResult.successful();
    }


    @DeleteMapping("/{warehousesId}")
    public JsonResult delete(@PathVariable Long warehousesId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        warehousesService.delete(warehousesId,merchantId,organizationId);
        return JsonResult.successful();
    }

    @GetMapping("select")
    public JsonResult select(@SaAccountVal AccountDto accountDto) {
        return JsonResult.successful(warehousesService.select(accountDto.getMerchantId(),accountDto.getOrganizationId()));
    }
}
