package com.flyemu.share.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @功能描述: 常用商品
 * @创建时间: 2024年04月23日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Commodity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //税收分类编码
    //税收分类名称

    //商品名称
    //简码    用户检索
    //商品编号 用于系统对接
    //享受税收优惠 是/否
    //优惠政策类型 免税/不征税
    //规格型号
    //单位
    //单价
    //是否含税 是 否
    //税率


    private Integer organizationId;

    private Integer merchantId;



}
