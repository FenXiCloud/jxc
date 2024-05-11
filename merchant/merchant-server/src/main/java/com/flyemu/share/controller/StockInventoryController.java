package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountVal;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.dto.AccountDto;
import com.flyemu.share.form.StockInventoryForm;
import com.flyemu.share.service.StockInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述: 盘点
 * @创建时间: 2024年05月07日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@RestController
@RequestMapping("/stockInventory")
@RequiredArgsConstructor
@SaCheckLogin
public class StockInventoryController {
    private final StockInventoryService stockInventoryService;

    /**
     * 盘点单列表
     *
     * @param merchantId
     * @param page
     * @param query
     * @return
     */
    @GetMapping
    public JsonResult list(Page page, StockInventoryService.Query query, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        query.setMerchantId(merchantId);
        query.setOrganizationId(organizationId);
        return JsonResult.successful(stockInventoryService.query(page, query));
    }

    /**
     * 新建盘点单的内容
     *
     * @param merchantId
     * @param page
     * @param selectQuery
     * @return
     */
    @GetMapping("/productsSelect")
    public JsonResult productsSelect(Page page, StockInventoryService.SelectQuery selectQuery, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(stockInventoryService.productsSelect(page,selectQuery, merchantId,organizationId));
    }


    /**
     * 保存盘点单
     *
     * @param inventoryForm
     * @param accountDto
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody StockInventoryForm inventoryForm, @SaAccountVal AccountDto accountDto) {
        stockInventoryService.save(inventoryForm, accountDto.getAdminId(), accountDto.getMerchantId(), accountDto.getOrganizationId(), accountDto.getMerchant().getCode());
        return JsonResult.successful();
    }



    /**
     * 盘点单详情
     *
     * @param merchantId
     * @param inventoryId
     * @return
     */
    @GetMapping("load/{inventoryId}")
    public JsonResult load(@SaMerchantId Long merchantId, @PathVariable Long inventoryId, @SaOrganizationId Long organizationId) {
        return JsonResult.successful(stockInventoryService.load( inventoryId,merchantId, organizationId));
    }


    /**
     * 删除盘点单
     *
     * @param inventoryId
     * @param merchantId
     * @return
     */
    @DeleteMapping("/{inventoryId}")
    public JsonResult delete(@PathVariable Long inventoryId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId) {
        stockInventoryService.delete(inventoryId, merchantId, organizationId);
        return JsonResult.successful();
    }


}
