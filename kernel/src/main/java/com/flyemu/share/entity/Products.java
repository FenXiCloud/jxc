package com.flyemu.share.entity;

import com.flyemu.share.dto.UnitPrice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 * @功能描述: 商品管理
 * @创建时间: 2024年04月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
@DynamicUpdate
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String code;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 32)
    private String pinyin;

    private String specification;//規格

    private String remark;

    private Boolean enabled;

    @Column(nullable = false)
    private Integer unitId;

    @Column(nullable = false)
    private Integer categoryId;

    @Column(nullable = false)
    private Integer organizationId;

    @Column(nullable = false)
    private Integer merchantId;

    @Comment("商品图片")
    private String imgPath;

    @Comment("是否启动辅助单位")
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    @Column(nullable = false)
    @ColumnDefault("b'0'")
    private Boolean enableMultiUnit;

    @Comment("辅助单位")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<UnitPrice> multiUnit;

}
