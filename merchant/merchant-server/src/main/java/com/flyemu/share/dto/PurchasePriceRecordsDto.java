package com.flyemu.share.dto;

import com.flyemu.share.entity.PurchasePriceRecords;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class PurchasePriceRecordsDto extends PurchasePriceRecords {
    private Long inputUnitId;

    private BigDecimal inputPrice;

    private String inputUnitName;
}