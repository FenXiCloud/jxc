package com.flyemu.share.form;

import com.alibaba.fastjson.JSONObject;
import com.flyemu.share.entity.Products;
import lombok.Data;

import java.util.List;

@Data
public class ProductsForm {

    private Products products;

    private List<JSONObject> levelPriceList;
}
