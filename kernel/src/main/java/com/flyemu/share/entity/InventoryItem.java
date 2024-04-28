package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @功能描述: 库存明细表
 * @创建时间: 2024年04月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("单号")
    private String code;

    @Comment("产品ID")
    private Integer productsId;

    @Comment("单据日期")
    private LocalDate billDate;

    @Comment("库存业务类型")
    private InventoryType inventoryType;

    @Comment("数量")
    private BigDecimal quantity;

    @Comment("可出库数量")
    private BigDecimal  availableQuantity;

    @Comment("单位成本")
    private BigDecimal unitCost;

    @Comment("小计金额")
    private BigDecimal totalAmount;

    @Comment("批次号")
    private String batchDate;

    @Comment("组织ID")
    @Column(nullable = false)
    private Integer organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Integer merchantId;

    public enum InventoryType {
        采购入库,采购退货,销售出库,销售退货,盘盈,盘亏,其他出库,其他入库,调拨
    }



}
