package com.flyemu.share.form;

import com.flyemu.share.entity.StockInventory;
import com.flyemu.share.entity.StockInventoryItem;
import lombok.Data;

import java.util.List;

@Data
public class StockInventoryForm {
    private StockInventory inventory;
    private List<StockInventoryItem> itemList;

}
