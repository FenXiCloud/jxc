package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @功能描述: 单据管理
 * @创建时间: 2024年04月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "merchantId"})})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("订单号")
    @Column(length = 64, unique = true)
    private String code;

    @Comment("单据日期")
    private LocalDate billDate;

    @Comment("订单原价总额")
    private BigDecimal totalAmount ;

    @Comment("优惠金额")
    private BigDecimal discountAmount ;

    @Comment("折后金额")
    private BigDecimal discountedAmount ;

    @Comment("订单类型")
    private OrderType orderType;

    @Comment("业务类型")
    private BusinessType  businessType;

    @Comment("订单状态")
    private OrderStatus orderStatus;

    @Comment("创建时间")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Comment("创建人")
    private Integer userId;

    @Comment("审核时间")
    private LocalDateTime checkOutTime;

    @Comment("审核人")
    private Integer checkId;

    @Comment("备注")
    private String remark;

    @Comment("供货商ID")
    private Integer vendorsId;

    @Comment("客户ID")
    private Integer customersId;

    @Comment("仓库ID")
    private Integer warehouseId;

    @Comment("调拨入库仓库ID")
    private Integer inWarehouseId;

    @Comment("调拨出库仓库ID")
    private Integer outWarehouseId;

    @Comment("组织ID")
    @Column(nullable = false)
    private Integer organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Integer merchantId;

    public enum OrderType{
        销售出库单,销售退货单,采购入库单,采购退货单,其他出库单,其他入库单,盘点单,调拨单
    }

    public enum BusinessType{
        盘盈,其他入库,盘亏,其他出库
    }

    public enum OrderStatus{
        已保存,已审核
    }
}