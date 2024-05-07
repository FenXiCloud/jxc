package com.flyemu.share.dto;

import com.flyemu.share.entity.PurchasePrice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class PurchaserPriceDto extends PurchasePrice {
    private Long inputUnitId;

    private BigDecimal inputPrice;

    private String inputUnitName;
}