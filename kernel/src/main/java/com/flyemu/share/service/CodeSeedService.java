package com.flyemu.share.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.flyemu.share.common.RedisLock;
import com.flyemu.share.entity.CodeSeed;
import com.flyemu.share.entity.QCodeSeed;
import com.flyemu.share.exception.ServiceException;
import com.flyemu.share.repository.CodeSeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

/**
 * @功能描述: 自动生成code编码
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@RequiredArgsConstructor
public class CodeSeedService extends AbsService {

    private final CodeSeedRepository codeSeedRepository;

    private final QCodeSeed qCodeSeed = QCodeSeed.codeSeed;

    private final StringRedisTemplate redisTemplate;

    /**
     * 获取连续编号
     *
     * @param merchantId
     * @param type
     * @return
     */
    public Integer next(Integer merchantId, String type) {
        String lockName = type + ":" + merchantId;
        RedisLock rLock = new RedisLock(redisTemplate, lockName);
        try {
            //获取订单锁
            boolean res = rLock.lock();
            Assert.isTrue(res, "系统繁忙，请稍后~");

            CodeSeed codeSeed = bqf.selectFrom(qCodeSeed).where(qCodeSeed.merchantId.eq(merchantId).and(qCodeSeed.type.eq(type))).fetchFirst();

            if (codeSeed == null) {
                codeSeed = new CodeSeed();
                codeSeed.setMerchantId(merchantId);
                codeSeed.setCode(1);
                codeSeed.setType(type);
            } else {
                codeSeed.increase();
            }
            codeSeedRepository.save(codeSeed);
            return codeSeed.getCode();
        } catch (InterruptedException e) {
            throw new ServiceException("编码生成错误~", e);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 按年份递增的编码：例如:2021001
     *
     * @param merchantId
     * @param type
     * @return
     * @throws InterruptedException
     */
    public String yearIncrease(Integer merchantId, String type) {
        long year = LocalDate.now().getYear();
        Integer next = this.next(merchantId, type + ":" + year);
        long code = year * 1000 + next;
        return String.valueOf(code);
    }

    /**
     * 按月份递增的编码：例如:202101001
     *
     * @param merchantId
     * @param type
     * @return
     * @throws InterruptedException
     */
    public String monthIncrease(Integer merchantId, String type) {
        long yearMonth = Long.parseLong(DateUtil.format(new Date(), "yyyyMM"));
        Integer next = this.next(merchantId, type + ":" + yearMonth);
        long code = yearMonth * 1000 + next;
        return String.valueOf(code);
    }

    /**
     * 按日递增的编码：例如:20210101001
     *
     * @param merchantId
     * @param type
     * @return
     * @throws InterruptedException
     */
    public String dayIncrease(Integer merchantId, String type) {
        long day = Long.parseLong(DateUtil.format(new Date(), "yyyyMMdd"));
        Integer next = this.next(merchantId, type + ":" + day);
        long code = day * 1000 + next;
        return String.valueOf(code);
    }
}
