package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.common.Constants;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.StockCostAdjustmentDetailDto;
import com.flyemu.share.dto.StockCostAdjustmentDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.form.StockCostAdjustmentForm;
import com.flyemu.share.repository.ArgsSettingRepository;
import com.flyemu.share.repository.StockCostAdjustmentDetailRepository;
import com.flyemu.share.repository.StockCostAdjustmentRepository;
import com.flyemu.share.repository.StockItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @功能描述: 成本调整单
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArgsSettingService extends AbsService {
    private final static QArgsSetting qArgsSetting = QArgsSetting.argsSetting;

    @Transactional
    public void update(String costMethod, Long merchantId, Long organizationId) {
        jqf.update(qArgsSetting)
                .set(qArgsSetting.costMethod,costMethod)
                .where(qArgsSetting.merchantId.eq(merchantId).and(qArgsSetting.organizationId.eq(organizationId))).execute();
    }

    public Dict load(Long merchantId, Long organizationId) {
       ArgsSetting argsSetting = bqf.selectFrom(qArgsSetting)
                .where(qArgsSetting.merchantId.eq(merchantId).and(qArgsSetting.organizationId.eq(organizationId))).fetchFirst();
        return Dict.create().set("costMethod", argsSetting.getCostMethod());
    }


}

