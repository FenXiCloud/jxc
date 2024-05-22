package com.flyemu.share.dto;

import com.flyemu.share.entity.StockCostAdjustment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)

public class StockCostAdjustmentDto extends StockCostAdjustment {

    private String productsCode;
    private String productsName;
    private String warehouseName;
    private String categoryName;
    private String createName;

}
