package com.flyemu.share.form;

import com.flyemu.share.entity.Order;
import com.flyemu.share.entity.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private Order order;
    private List<OrderDetail> detailList;

}
