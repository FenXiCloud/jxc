package com.flyemu.share.dto;

import com.flyemu.share.entity.Vendors;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)

public class VendorsDto extends Vendors {
    private String categoryName;

}
