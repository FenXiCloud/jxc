package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @功能描述: 月度结账
 * @创建时间: 2024年04月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"checkYear", "checkMonth", "organizationId", "merchantId"})})
public class Checkout implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer checkYear;

    private Integer checkMonth;

    private Status status;

    private LocalDate checkDate;

    @Column(nullable = false)
    private Integer organizationId;

    @Column(nullable = false)
    private Integer merchantId;

    public enum Status {
        未结账,已结账
    }
}
