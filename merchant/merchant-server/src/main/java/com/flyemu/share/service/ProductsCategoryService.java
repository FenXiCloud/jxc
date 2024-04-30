package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.flyemu.share.entity.ProductsCategory;
import com.flyemu.share.entity.QProducts;
import com.flyemu.share.entity.QProductsCategory;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.ProductsCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @功能描述: 商品分类
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductsCategoryService extends AbsService {

    private static final QProductsCategory qProductsCategory = QProductsCategory.productsCategory;

    private final ProductsCategoryRepository productsCategoryRepository;


    public List<ProductsCategory> query(Query query) {
        return bqf.selectFrom(qProductsCategory)
                .where(query.builder)
                .orderBy(qProductsCategory.sort.desc(), qProductsCategory.code.asc(), qProductsCategory.id.asc()).fetch();
    }

    @Transactional
    public ProductsCategory save(ProductsCategory productsCategory) {
        try {
            String path = "";
            if (productsCategory.getPid() != null) {
                ProductsCategory parent = productsCategoryRepository.getReferenceById(productsCategory.getPid());
                productsCategoryRepository.save(parent);
            }

            if (productsCategory.getId() != null) {
                //更新
                ProductsCategory original = productsCategoryRepository.getById(productsCategory.getId());

                BeanUtil.copyProperties(productsCategory, original, CopyOptions.create().ignoreNullValue());
                if (productsCategory.getPid() == null) {
                    original.setPid(null);
                }
                return productsCategoryRepository.save(original);
            }

            productsCategoryRepository.save(productsCategory);
            return productsCategory;
        } catch (Exception e) {
            log.error("ProductsCategory", e);
            throw new ServiceException("编码已存在~");
        }


    }


    @Transactional
    public void delete(Integer merchantId, Integer productsCategoryId) {
        ProductsCategory productsCategory = productsCategoryRepository.getReferenceById(productsCategoryId);

        List<Integer> ids = bqf.selectFrom(qProductsCategory)
                .select(qProductsCategory.id)
                .where(qProductsCategory.merchantId.eq(merchantId)).fetch();

        QProducts qProducts = QProducts.products;

        jqf.delete(qProductsCategory)
                .where(qProductsCategory.merchantId.eq(merchantId).and(qProductsCategory.id.in(ids)))
                .execute();
    }

    /**
     * 根据ID获取数据
     *
     * @param orgId
     * @return
     */
    public ProductsCategory loadById(Integer merchantId, Integer orgId) {
        return bqf.selectFrom(qProductsCategory)
                .where(qProductsCategory.merchantId.eq(merchantId).and(qProductsCategory.id.eq(orgId)))
                .fetchFirst();
    }

    public List<ProductsCategory> listAll(Integer merchantId) {
        return bqf.selectFrom(qProductsCategory)
                .where(qProductsCategory.merchantId.eq(merchantId))
                .orderBy(qProductsCategory.sort.desc(), qProductsCategory.id.asc())
                .fetch();
    }

    public List<ProductsCategory> select(Integer merchantId) {
        return bqf.selectFrom(qProductsCategory).where(qProductsCategory.merchantId.eq(merchantId)).orderBy(qProductsCategory.sort.desc()).fetch();
    }



    /**
     * 查询条件
     */
    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setName(String name) {
            if (StrUtil.isNotEmpty(name)) {
                builder.and(qProductsCategory.name.contains(name));
            }
        }

        public void setMerchantId(Integer merchantId) {
            if (merchantId != null) {
                builder.and(qProductsCategory.merchantId.eq(merchantId));
            }
        }
    }
}
