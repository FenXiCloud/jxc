package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.CustomersDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.CustomersRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @功能描述: 客户管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomersService extends AbsService {

    private static final QCustomers qCustomers = QCustomers.customers;
    private static final QCustomersCategory qCustomersCategory = QCustomersCategory.customersCategory;

    private final CustomersRepository customersRepository;

    private final CodeSeedService codeSeedService;

    public PageResults<CustomersDto> query(Page page, Query query) {
        PagedList<Tuple> fetchPage = bqf.selectFrom(qCustomers)
                .select(qCustomers,qCustomersCategory.name)
                .leftJoin(qCustomersCategory).on(qCustomersCategory.id.eq(qCustomers.customersCategoryId))
                .where(query.builder)
                .orderBy(qCustomers.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());

        ArrayList<CustomersDto> collect = fetchPage.stream().collect(ArrayList::new, (list, tuple) -> {
            CustomersDto dto = BeanUtil.toBean(tuple.get(qCustomers), CustomersDto.class);
            dto.setCategoryName(tuple.get(qCustomersCategory.name));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page,fetchPage.getTotalSize());
    }

    @Transactional
    public Customers save(Customers customers,String merchCode) {
        if (customers.getId() != null) {
            //更新
            Customers original = customersRepository.getById(customers.getId());
            if(!original.getCode().equals(customers.getCode())){
                if(StrUtil.isEmpty(customers.getCode())){
                    customers.setCode(original.getCode());
                }else {
                    //手动设置了编码，需要检查重复
                    long count = bqf.selectFrom(qCustomers)
                            .where(qCustomers.merchantId.eq(original.getMerchantId()).and(qCustomers.code.eq(customers.getCode()))
                                    .and(qCustomers.id.ne(customers.getId())).and(qCustomers.organizationId.eq(original.getOrganizationId())))
                            .fetchCount();
                    Assert.isTrue(count == 0, customers.getCode() + "编码已存在~");
                }
            }
            BeanUtil.copyProperties(customers, original, CopyOptions.create().ignoreNullValue());
            return customersRepository.save(original);
        }
        if(StrUtil.isEmpty(customers.getCode())){
            String code = merchCode + codeSeedService.nextNum(1l, "CustomCode" + customers.getMerchantId());
            customers.setCode(code);
        }else {
            //手动设置了编码，需要检查重复
            long count = bqf.selectFrom(qCustomers)
                    .where(qCustomers.merchantId.eq(customers.getMerchantId()).and(qCustomers.organizationId.eq(customers.getOrganizationId())).and(qCustomers.code.eq(customers.getCode())))
                    .fetchCount();
            Assert.isTrue(count == 0, customers.getCode() + "编码已存在~");
        }
        Customers m = customersRepository.save(customers);

        return m;
    }


    @Transactional
    public void delete(Long customersId,Long merchantId, Long organizationId) {
        jqf.delete(qCustomers)
                .where(qCustomers.id.eq(customersId).and(qCustomers.merchantId.eq(merchantId)).and(qCustomers.organizationId.eq(organizationId))).execute();
    }

    /**
     * 查询条件
     */
    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setName(String name) {
            if (StrUtil.isNotEmpty(name)) {
                builder.and(qCustomers.name.contains(name));
            }
        }

        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qCustomers.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qCustomers.organizationId.eq(organizationId));
            }
        }
    }

}
