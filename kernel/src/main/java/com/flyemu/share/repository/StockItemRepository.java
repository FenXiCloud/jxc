package com.flyemu.share.repository;

import com.flyemu.share.entity.StockItem;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface StockItemRepository extends JpaRepositoryImplementation<StockItem,Long> {
}
