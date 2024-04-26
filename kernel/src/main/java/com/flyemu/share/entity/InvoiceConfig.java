package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

/**
 * @功能描述: 开票设置
 * @创建时间: 2024年04月23日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class InvoiceConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Comment("参数名称")
    private String configName;

    @Comment("参数键名")
    private String configKey;

    @Comment("参数键值")
    private String configValue;

    @Comment("系统内置")
    @Column(nullable = false)
    @ColumnDefault("b'1'")
    private Boolean systemDefault;

    private Integer organizationId;

    private Integer merchantId;



}
