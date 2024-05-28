package com.flyemu.share.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

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
public class RelationSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("系统名称")
    private String name;

    @Comment("财务系统科目ID")
    private Long subjectId;

    @Comment("财务系统帐套ID")
    private Long accountSetsId;

    /**
     * 科目名称
     */
    @Comment("财务系统科目名称")
    private String title;
    /**
     * 科目类型
     */
    @Comment("财务系统科目类型")
    private String type;

    /**
     * 科目编码
     */
    @Comment("财务系统科目编码")
    private String code;

    @Comment("组织ID")
    @Column(nullable = false)
    private Long organizationId;

    @Comment("关联系统信息ID")
    @Column(nullable = false)
    private Long cwRelationId;

    @Comment("商户ID")
    @Column(nullable = false)
    private Long merchantId;
}
