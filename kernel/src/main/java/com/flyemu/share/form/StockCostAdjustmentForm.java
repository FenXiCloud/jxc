package com.flyemu.share.form;

import com.flyemu.share.entity.StockCostAdjustmentDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StockCostAdjustmentForm {
    private Long stockId;
    private Long productsId;
    private Long warehouseId;
    private BigDecimal amount;
    private List<StockCostAdjustmentDetail> detailList;

}
