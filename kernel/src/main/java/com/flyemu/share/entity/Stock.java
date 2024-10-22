package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

/**
 * @功能描述: 库存表-存储每个产品的库存总量和加权平均成本。
 * @创建时间: 2024年04月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_warehouseId_productsId", columnNames = {"merchantId","organizationId","warehouseId", "productsId"})
})
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("产品ID")
    private Long productsId;

    @Comment("数量")
    private BigDecimal totalQuantity;

    @Comment("总成本")
    private BigDecimal weightedCost;

    @Comment("加权平均成本")
    private BigDecimal weightedAverageCost;

    @Comment("小计金额")
    private BigDecimal totalAmount;

    @Comment("最近入库成本")
    private BigDecimal inUnitCost = BigDecimal.ZERO;

    @Comment("仓库ID")
    @Column(nullable = false)
    private Long warehouseId;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;

}
