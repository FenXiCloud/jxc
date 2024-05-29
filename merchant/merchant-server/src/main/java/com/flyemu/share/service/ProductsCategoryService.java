package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
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
                parent.setLeaf(false);
                path = parent.getPath();
                productsCategoryRepository.save(parent);
            }

            if (productsCategory.getId() != null) {
                //更新
                ProductsCategory original = productsCategoryRepository.getById(productsCategory.getId());

                BeanUtil.copyProperties(productsCategory, original, CopyOptions.create().ignoreNullValue());
                if (productsCategory.getPid() == null) {
                    original.setPid(null);
                    original.setLeaf(true);
                } else {
                    original.setPath(path + original.getId() + "/");
                }
                return productsCategoryRepository.save(original);
            }

            productsCategory.setLeaf(true);
            productsCategoryRepository.save(productsCategory);
            jqf.update(qProductsCategory).set(qProductsCategory.path, path + productsCategory.getId() + "/").where(qProductsCategory.id.eq(productsCategory.getId())).execute();
            return productsCategory;
        } catch (Exception e) {
            log.error("ProductsCategory", e);
            throw new ServiceException("编码已存在~");
        }


    }


    @Transactional
    public void delete(Long merchantId, Long productsCategoryId, Long organizationId) {
        ProductsCategory productsCategory = productsCategoryRepository.getReferenceById(productsCategoryId);

        List<Long> ids = bqf.selectFrom(qProductsCategory)
                .select(qProductsCategory.id)
                .where(qProductsCategory.path.like(productsCategory.getPath() + "%").and(qProductsCategory.merchantId.eq(merchantId)).and(qProductsCategory.organizationId.eq(organizationId))).fetch();

        QProducts qProducts = QProducts.products;
        Assert.isFalse(bqf.selectFrom(qProducts).where(qProducts.categoryId.in(ids)
                .and(qProducts.merchantId.eq(merchantId)).and(qProducts.organizationId.eq(organizationId))).fetchCount() > 0, "商品已使用，不能删除");

        if (productsCategory.getPid() != null) {
            long count = bqf.selectFrom(qProductsCategory)
                    .where(qProductsCategory.pid.eq(productsCategory.getPid()).and(qProductsCategory.merchantId.eq(merchantId)).and(qProductsCategory.organizationId.eq(organizationId))).fetchCount();
            if (count == 1) {
                jqf.update(qProductsCategory)
                        .set(qProductsCategory.leaf, true)
                        .where(qProductsCategory.id.eq(productsCategory.getPid())
                                .and(qProductsCategory.merchantId.eq(merchantId))
                                .and(qProductsCategory.organizationId.eq(organizationId))).execute();
            }
        }
        jqf.delete(qProductsCategory)
                .where(qProductsCategory.merchantId.eq(merchantId).and(qProductsCategory.organizationId.eq(organizationId)).and(qProductsCategory.id.in(ids)))
                .execute();
    }

    /**
     * 根据ID获取数据
     *
     * @param orgId
     * @return
     */
    public ProductsCategory loadById(Long merchantId, Long orgId) {
        return bqf.selectFrom(qProductsCategory)
                .where(qProductsCategory.merchantId.eq(merchantId).and(qProductsCategory.id.eq(orgId)))
                .fetchFirst();
    }

    public List<ProductsCategory> listAll(Long merchantId, Long organizationId) {
        return bqf.selectFrom(qProductsCategory)
                .where(qProductsCategory.merchantId.eq(merchantId).and(qProductsCategory.organizationId.eq(organizationId)))
                .orderBy(qProductsCategory.sort.desc(), qProductsCategory.id.asc())
                .fetch();
    }

    public List<ProductsCategory> select(Long merchantId, Long organizationId) {
        return bqf.selectFrom(qProductsCategory).where(qProductsCategory.merchantId.eq(merchantId).and(qProductsCategory.organizationId.eq(organizationId))).orderBy(qProductsCategory.sort.desc()).fetch();
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

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qProductsCategory.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qProductsCategory.organizationId.eq(organizationId));
            }
        }
    }
}
