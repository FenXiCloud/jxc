package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * @功能描述: 组织机构
 * @创建时间: 2024年04月28日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "merchantId"})})
public class Organization implements Serializable {
    private static final long serialVersionUID = -24843859909377092L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //编码
    @Column(length = 32, nullable = false)
    private String code;

    //名称
    @Column(length = 64, nullable = false)
    private String name;

    //纳税人识别号
    private String registrationNumber;

    //法定代表人姓名
    private String legalPerson;

    //机构类型
    private OrgType orgType;

    //所属区域
    private String area;

    //企业经营地址
    private String address;

    //联系人姓名
    private String linkman;

    //联系人邮箱
    private String email;

    //联系人电话号码
    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;


    @CreationTimestamp
    private LocalDate startDate;

    private Boolean enabled;

    @Column(nullable = false)
    private Integer merchantId;


    @Comment("是否是当前组织")
    @ColumnDefault("b'0'")
    @Column(nullable = false)
    private Boolean current;


    public enum OrgType {
        公司,个体户
    }

}

