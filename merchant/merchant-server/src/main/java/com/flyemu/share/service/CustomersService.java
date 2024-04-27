package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.digest.DigestUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.AdminRepository;
import com.flyemu.share.repository.CustomersRepository;
import com.flyemu.share.repository.MerchantRepository;
import com.flyemu.share.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    private final QCustomers qCustomers = QCustomers.customers;

    private final CustomersRepository customersRepository;


    public PageResults<Customers> query(Page page) {
        PagedList<Customers> fetchPage = bqf.selectFrom(qCustomers)
                .orderBy(qCustomers.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        return new PageResults<>(fetchPage, page);
    }

    @Transactional
    public Customers save(Customers customers) {
        if (customers.getId() != null) {
            //更新
            Customers original = customersRepository.getById(customers.getId());
            BeanUtil.copyProperties(customers, original, CopyOptions.create().ignoreNullValue());
            return customersRepository.save(original);
        }
        Customers m = customersRepository.save(customers);

        return m;
    }


    @Transactional
    public void delete(Integer customersId) {
        customersRepository.deleteById(customersId);
    }

}
