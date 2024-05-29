package com.flyemu.share.dto;

import com.flyemu.share.entity.StockCostAdjustment;
import com.flyemu.share.entity.StockCostAdjustmentDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)

public class StockCostAdjustmentDetailDto extends StockCostAdjustmentDetail {
    private String orderCode;
    private String vendorsName;
    private String unitName;
    private BigDecimal quantity;
}
