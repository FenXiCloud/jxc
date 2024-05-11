package com.flyemu.share.dto;

import com.flyemu.share.entity.StockInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)

public class StockInventoryDto extends StockInventory {
    private String inOrderCode;
    private String outOrderCode;
}
