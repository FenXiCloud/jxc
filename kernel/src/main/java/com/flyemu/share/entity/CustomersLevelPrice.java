package com.flyemu.share.entity;

import com.flyemu.share.dto.UnitPrice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @功能描述: 客户等级价格
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_products_customersCategory", columnNames = {"merchantId", "productsId", "customersLevelId"})
})
public class CustomersLevelPrice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("商品id")
    private Long productsId;

    @Comment("客户分类id")
    private Long customersLevelId;

    @Comment("基础单位")
    private Long unitId;

    @Comment("基础单位价格")
    private BigDecimal price;

    @Comment("客户单位辅助价格")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<UnitPrice> unitPrice;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;

}

