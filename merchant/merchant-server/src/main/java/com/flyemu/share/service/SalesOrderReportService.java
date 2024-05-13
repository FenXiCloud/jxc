package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.blazebit.persistence.PagedList;
import com.flyemu.share.controller.Page;
import com.flyemu.share.controller.PageResults;
import com.flyemu.share.dto.OrderDetailDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.enums.OrderStatus;
import com.flyemu.share.enums.OrderType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @功能描述: 销售出库单明细
 * @创建时间: 2024年05月13日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesOrderReportService extends AbsService {
    private final static QOrder qOrder = QOrder.order;
    private final static QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final static QCustomers qCustomers = QCustomers.customers;
    private final static QProducts qProducts = QProducts.products;
    private final static QWarehouses qWarehouses = QWarehouses.warehouses;
    private final static QProductsCategory qProductsCategory = QProductsCategory.productsCategory;


    public PageResults<OrderDetailDto> query(Page page, Query query) {
        PagedList<Tuple> pagedList = bqf.selectFrom(qOrderDetail).select(qOrderDetail, qCustomers.name,qProducts.code,qProducts.name,qWarehouses.name,qProductsCategory.name,qOrder.billDate,qOrder.code)
                .leftJoin(qOrder).on(qOrder.id.eq(qOrderDetail.orderId))
                .leftJoin(qProducts).on(qProducts.id.eq(qOrderDetail.productsId))
                .leftJoin(qCustomers).on(qCustomers.id.eq(qOrder.customersId))
                .leftJoin(qWarehouses).on(qWarehouses.id.eq(qOrderDetail.warehouseId))
                .leftJoin(qProductsCategory).on(qProductsCategory.id.eq(qProducts.categoryId))
                .where(query.builder.and(qOrder.orderType.eq(OrderType.销售出库单)).and(qOrder.orderStatus.eq(OrderStatus.已审核)))
                .orderBy(qOrderDetail.id.desc())
                .fetchPage(page.getOffset(), page.getOffsetEnd());
        ArrayList<OrderDetailDto> collect = pagedList.stream().collect(ArrayList::new, (list, tuple) -> {
            OrderDetailDto dto = BeanUtil.toBean(tuple.get(qOrderDetail), OrderDetailDto.class);
            dto.setCustomersName(tuple.get(qCustomers.name));
            dto.setProductsName(tuple.get(qProducts.name));
            dto.setProductsCode(tuple.get(qProducts.code));
            dto.setWarehouseName(tuple.get(qWarehouses.name));
            dto.setCategoryName(tuple.get(qProductsCategory.name));
            dto.setOrderCode(tuple.get(qOrder.code));
            dto.setBillDate(tuple.get(qOrder.billDate));
            list.add(dto);
        }, List::addAll);
        return new PageResults<>(collect, page, pagedList.getTotalSize());
    }




    @Data
    public static class Query {
        public final BooleanBuilder builder = new BooleanBuilder();


        public void setMerchantId(Long merchantId) {
            if (merchantId != null) {
                builder.and(qOrderDetail.merchantId.eq(merchantId));
            }
        }

        public void setOrganizationId(Long organizationId) {
            if (organizationId != null) {
                builder.and(qOrderDetail.organizationId.eq(organizationId));
            }
        }

        public void setFilter(String filter) {
            if (filter != null) {
                builder.and(qProducts.name.contains(filter).or(qProducts.code.contains(filter)).or(qProducts.pinyin.contains(filter)));
            }
        }

        public void setCustomersIds(String customersIds) {
            if (StrUtil.isNotEmpty(customersIds)){
                Set<Long> cIds = Stream.of(customersIds.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
                if (cIds.size() > 0) {
                    builder.and(qOrder.customersId.in(cIds));
                }
            }
        }


        public void setStart(String start) {
            if (StrUtil.isNotBlank(start)) {
                LocalDate parse = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                builder.and(qOrder.billDate.goe(parse));
            }
        }

        public void setEnd(String end) {
            if (StrUtil.isNotBlank(end)) {
                LocalDate parse = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                builder.and(qOrder.billDate.loe(parse));
            }
        }
    }

}
