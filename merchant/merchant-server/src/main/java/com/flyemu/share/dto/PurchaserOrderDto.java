package com.flyemu.share.dto;

import com.flyemu.share.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)

public class PurchaserOrderDto extends Order {

    private String VendorsName;
}
