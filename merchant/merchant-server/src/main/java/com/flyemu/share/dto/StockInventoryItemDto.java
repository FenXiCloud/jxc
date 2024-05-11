package com.flyemu.share.dto;

import com.flyemu.share.entity.StockInventoryItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=false)

public class StockInventoryItemDto extends StockInventoryItem {
    private String warehouses;
    private String productsName;
    private String productsCode;
    private String specification;
    private String categoryName;
}
