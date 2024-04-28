package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table
public class CustomersCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 32, nullable = false)
    private String name;

    private String code;

    @Comment("父id")
    private Integer pid;

    @Comment("组织ID")
    @Column(nullable = false)
    private Integer organizationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Integer merchantId;

}
