package com.flyemu.share.repository;

import com.flyemu.share.entity.StockInventoryItem;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface StockInventoryItemRepository extends JpaRepositoryImplementation<StockInventoryItem,Long> {
}
