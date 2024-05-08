package com.flyemu.share.dto;

import com.flyemu.share.entity.Stock;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StockDto extends Stock {
    private Long parentId;
    private String nm;
    private String pnm;
    private String warehousesName;
    private String categoryName;
    private String productsName;
    private String imgPath;
    private String productsCode;
    private String unitsName;
}
