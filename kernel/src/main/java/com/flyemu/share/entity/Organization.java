package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 组织机构
 *
 * @author ____′↘夏悸
 * @since 2022-03-04 22:17:15
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

    private Boolean enabled;

    @Column(nullable = false)
    private Integer merchantId;



    public enum OrgType {
        公司,个体户
    }

}

