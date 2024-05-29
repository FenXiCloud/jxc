package com.flyemu.share.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * @功能描述: 财务映射
 * @创建时间: 2024年05月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_merchantId_organizationId", columnNames = {"merchantId", "organizationId"}),
})
public class RelationCw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("是否关联")
    @Column(nullable = false)
    @ColumnDefault("b'0'")
    private Boolean isRelation;

    @Comment("更新时间")
    private LocalDateTime updateDate;

    @Comment("操作人ID")
    private Long adminId;

    @Comment("帐套ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long accountSetsId;

    @Comment("单位名称")
    private String companyName;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;


}
