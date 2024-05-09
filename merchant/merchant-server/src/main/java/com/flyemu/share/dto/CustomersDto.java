package com.flyemu.share.dto;

import com.flyemu.share.entity.Customers;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CustomersDto extends Customers {
    private String categoryName;
    private String levelName;
}
