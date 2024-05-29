package com.flyemu.share.dto;

import com.flyemu.share.entity.Stock;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StockDto extends Stock {
    private Long parentId;
    private String treeId;
    private String pTreeId;
    private String warehouseName;
    private String categoryName;
    private String productsName;
    private String imgPath;
    private String productsCode;
    private String unitsName;
}
