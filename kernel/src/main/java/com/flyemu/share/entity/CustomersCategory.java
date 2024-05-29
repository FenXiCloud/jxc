package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * @功能描述: 客户分类
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
        @UniqueConstraint(name = "uc_customers_category_name", columnNames = {"merchantId", "organizationId", "name"}),
})
public class CustomersCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String name;

    private String code;

    @Comment("父id")
    private Long pid;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;

}
