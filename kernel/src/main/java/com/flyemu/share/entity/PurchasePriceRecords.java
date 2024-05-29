package com.flyemu.share.entity;

import com.flyemu.share.dto.UnitPrice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

/**
 * @功能描述: 供货商商品价格
 * @创建时间: 2024年05月07日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_vendorsId_productsId", columnNames = {"merchantId","organizationId","vendorsId", "productsId"})
})
public class PurchasePriceRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vendorsId;

    @Column(nullable = false)
    private Long merchantId;

    @Column(nullable = false)
    private Long organizationId;

    private Long productsId;

    @Comment("单位")
    private Long unitId;

    @Comment("单位价格")
    private BigDecimal price;

    @Comment("单位辅助价格")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<UnitPrice> unitPrice;
}