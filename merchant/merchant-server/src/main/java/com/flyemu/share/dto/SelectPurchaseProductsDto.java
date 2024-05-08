package com.flyemu.share.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2023 杭州财汇餐谋科技有限公司 All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description :选择商品下拉列表</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2023年09月06日</li>
 * <li>@author     : ____′↘ts</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Data
public class SelectPurchaseProductsDto {
    private Long productsId;
    private String imgPath;
    private String productsCode;
    private String productsName;
    private String title;
    private String spec;
    private String unitName;
    private Long unitId;
    private BigDecimal price;
    private String path;

    private List<UnitPrice> unitPrice;

    public void setTitle() {
        this.title = this.productsCode + " " + this.productsName;
    }

}
