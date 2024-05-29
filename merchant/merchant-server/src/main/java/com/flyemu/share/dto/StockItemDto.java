package com.flyemu.share.dto;

import com.flyemu.share.entity.StockItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=false)

public class StockItemDto extends StockItem {
    private String customersName;
    private String vendorsName;
    private String productsCode;
    private String productsName;
    private String warehouseName;
    private String categoryName;
    private String unitName;
    private String orderCode;
    private Long StockItemId;
    private BigDecimal adjustmentAmount = BigDecimal.ZERO;
    private BigDecimal beforeUnitCost;
    private BigDecimal inQuantity;
    private BigDecimal outQuantity;
    private BigDecimal inTotalAmount;
    private BigDecimal outTotalAmount;
    private LocalDate billDate;
}
