package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.entity.QUnits;
import com.flyemu.share.entity.Units;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.UnitsRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @功能描述: 单位管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnitsService extends AbsService {

    private final static QUnits qUnits = QUnits.units;

    private final UnitsRepository unitsRepository;


    public List<Units> query(Query query) {
        List<Units> units = bqf.selectFrom(qUnits)
                .where(query.builder)
                .orderBy(qUnits.id.desc())
                .fetch();
        return units;
    }


    @Transactional
    public Units save(Units units) {
        try {
            if (units.getId() != null) {
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("merchantId", units.getMerchantId());
                params.put("unitsId", units.getId());
                params.put("unitStr", "{\"unitId\": " + units.getId() + "}");
                Assert.isFalse(lazyDao.getCount("unitUp", params) > 0, "单位已使用,不能更新~");

                Units original = unitsRepository.getById(units.getId());
                BeanUtil.copyProperties(units, original, CopyOptions.create().ignoreNullValue());
                return unitsRepository.save(original);
            }
            return unitsRepository.save(units);
        } catch (Exception e) {
            log.error("Unit", e);
            throw new ServiceException(e.getMessage());
        }
    }


    @Transactional
    public void delete(Integer unitsId, Integer merchantId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("merchantId", merchantId);
        params.put("unitsId", unitsId);
        params.put("unitsStr", "{\"unitsId\": " + unitsId + "}");
        jqf.delete(qUnits)
                .where(qUnits.id.eq(unitsId).and(qUnits.merchantId.eq(merchantId)))
                .execute();
    }

    public List<Units> select(Integer merchantId) {
        return bqf.selectFrom(qUnits).where(qUnits.merchantId.eq(merchantId)).fetch();
    }


    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();

        public void setMerchantId(Integer merchantId) {
            if (merchantId != null) {
                builder.and(qUnits.merchantId.eq(merchantId));
            }
        }
    }
}