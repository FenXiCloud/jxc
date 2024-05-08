package com.flyemu.share.repository;

import com.flyemu.share.entity.Stock;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface StockRepository extends JpaRepositoryImplementation<Stock,Long> {
}
