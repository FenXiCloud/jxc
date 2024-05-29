package com.flyemu.share.dto;

import com.flyemu.share.entity.OrderDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=false)

public class OrderDetailDto extends OrderDetail {
    private String customersName;
    private String vendorsName;
    private String productsCode;
    private String productsName;
    private String warehouseName;
    private String categoryName;
    private String orderCode;
    private LocalDate billDate;
}
