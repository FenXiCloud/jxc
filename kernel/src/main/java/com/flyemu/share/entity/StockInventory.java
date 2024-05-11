package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @功能描述: 盘点
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
public class StockInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("订单号")
    @Column(length = 64, unique = true)
    private String code;

    @Comment("盘点日期")
    private LocalDate billDate;

    @Comment("盘点日期")
    private LocalDate stockDate;

    @Comment("盘盈订单ID")
    private Long inOrderId;

    @Comment("盘亏订单ID")
    private Long outOrderId;

    @Comment("创建时间")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Comment("创建人")
    private Long userId;

    @Comment("备注")
    private String remark;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;
}
