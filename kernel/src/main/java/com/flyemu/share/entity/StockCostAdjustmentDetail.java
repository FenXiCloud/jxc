package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

/**
 * @功能描述: 成本调整单详情
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class StockCostAdjustmentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("调整单Id")
    @Column(updatable = false)
    private Long adjustmentId;

    @Comment("仓库详情Id")
    @Column(updatable = false)
    private Long stockItemId;

    @Comment("可出库数量")
    private BigDecimal availableQuantity;

    @Comment("调整金额")
    private BigDecimal adjustmentAmount;

    @Comment("调整前单位成本")
    private BigDecimal beforeUnitCost;

    @Comment("调整前成本金额")
    private BigDecimal beforeCost;

    @Comment("调整后单位成本")
    private BigDecimal afterUnitCost;

    @Comment("调整后成本金额")
    private BigDecimal afterCost;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;

}
