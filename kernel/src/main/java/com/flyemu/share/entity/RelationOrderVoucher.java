package com.flyemu.share.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @功能描述: 财务订单凭证
 * @创建时间: 2024年05月29日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_orderId", columnNames = {"merchantId", "organizationId","orderId"}),
})
public class RelationOrderVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("订单ID")
    private Long orderId;

    @Comment("创建时间")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Comment("操作人ID")
    private Long adminId;

    @Comment("凭证")
    private String word;

    @Comment("凭证号")
    private Integer code;

    @Comment("创建时间")
    private LocalDate voucherDate;

    @Comment("凭证ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long voucherId;

    @Comment("帐套ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long accountSetsId;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;


}
