package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

/**
 * @功能描述: 盘点明细单
 * @创建时间: 2024年05月11日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table
public class StockInventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("盘点单ID")
    private Long stockInventoryId;

    @Comment("产品ID")
    private Long productsId;

    @Comment("基础单位ID")
    private Long unitId;

    @Comment("基础单位名称")
    private String unitName;

    @Comment("系统数量")
    private BigDecimal sysQuantity;

    @Comment("盘点数量")
    private BigDecimal inventoryQuantity;

    @Comment("备注")
    private String remark;

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
