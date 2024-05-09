package com.flyemu.share.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SelectProductsDto {
    private Long productsId;
    private Long categoryId;
    private String imgPath;
    private String productsCode;
    private String productsName;
    private String category;
    private String pinyin;

    private String title;
    private String specification;
    private String unitName;
    private Long unitId;
    private String path;
    private Long orderUnitId;
    private String orderUnitName;
    private BigDecimal price;
    private Double orderNum;
    private Double quantity;
    private BigDecimal amount;

    private Long warehouseId;

    private Boolean enableMultiUnit;

    private String multiUnit;
    private List<UnitPrice> unitPrice;

    public void setTitle() {
        this.title = this.productsCode + " " + this.productsName;
    }


}
