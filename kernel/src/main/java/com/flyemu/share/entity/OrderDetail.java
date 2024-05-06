package com.flyemu.share.entity;

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
    private Integer orderId;

    @Comment("产品ID")
    private Long productsId;

    @Comment("单据日期")
    private LocalDate billDate;

    @Comment("系统数量,盘点使用")
    private BigDecimal sysQuantity;

    @Comment("数量")
    private BigDecimal quantity;

    @Comment("单价")
    private BigDecimal price;

    @Comment("小计金额")
    private BigDecimal totalAmount;

    @Comment("折扣金额")
    private BigDecimal discountAmount;

    @Comment("基本单位ID")
    private Integer unitId;

    @Comment("批次号")
    private String batchDate;

    @Comment("备注")
    private String remark;

    @Comment("组织ID")
    @Column(nullable = false)
    private Integer organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Integer merchantId;





}
