package com.flyemu.share.repository;

import com.flyemu.share.entity.StockInventory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface StockInventoryRepository extends JpaRepositoryImplementation<StockInventory,Long> {
}
