package com.flyemu.share.entity;

import com.flyemu.share.enums.StockType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @功能描述: 单据明细
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
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("订单主表ID")
    private Long orderId;

    @Comment("产品ID")
    private Long productsId;

    @Comment("单据日期")
    private LocalDate billDate;

    @Comment("基本数量比例")
    private Double num;

    @Comment("系统数量,盘点使用")
    private BigDecimal sysQuantity;

    @Comment("下单数量")
    private BigDecimal orderQuantity;

    @Comment("采购单位ID")
    private Long orderUnitId;

    @Comment("采购单位名称")
    private String orderUnitName;

    @Comment("基础单位ID")
    private Long unitId;

    @Comment("基础单位名称")
    private String unitName;

    @Comment("单价")
    private BigDecimal orderPrice;

    @Comment("折扣率")
    @Column(nullable = false, columnDefinition = "double default '100.00'", precision = 3, scale = 2)
    private Double discount;

    @Comment("优惠金额")
    private BigDecimal discountAmount ;

    @Comment("折后金额")
    private BigDecimal discountedAmount ;

    @Comment("批次号")
    private String batchDate;

    @Comment("备注")
    private String remark;

    @Comment("仓库ID")
    @Column(nullable = false)
    private Long warehouseId;

    @Comment("出库数量")
    private BigDecimal  outQuantity;

    @Comment("单位成本")
    private BigDecimal unitCost;

    @Comment("成本")
    private BigDecimal cost;

    @Comment("仓库类型")
    @Column(nullable = false,length = 32)
    @Enumerated(EnumType.STRING)
    private StockType stockType;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;


}
