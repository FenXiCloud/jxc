<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-toolbar class-name="!p-0px">
        <template #buttons>
          <span class=" red-color" style="font-size: 20px!important;"> {{ order.orderStatus }}</span>
          <span class="ml-16px" style="font-size: 15px!important;">订单号: {{ order.code }}</span>
          <span class="ml-16px" style="font-size: 15px!important;">客户名称: {{ order.customersName }}</span>
        </template>
        <template #tools>
        </template>
      </vxe-toolbar>
      <div class="pt-13px">
        <vxe-table
            size="mini"
            ref="xTable"
            :row-config="{height: 48}"
            border highlight-hover-row show-overflow
            show-footer
            :footer-method="footerMethod"
            :data="productsData">
          <vxe-column title="序号" type="seq" width="60" align="center" fixed="left" footer-align="right"/>
          <vxe-column title="商品信息" min-width="350">
            <template #default="{row,rowIndex}">
              <div class="flex">
                <div class="flex">
                  <img v-if="row.imgPath" :src="row.imgPath" style="width: 40px;height: 40px;">
                  <img v-else src="../../../assets/good-img-bg.png" style="height: 40px;width: 40px"/>
                </div>
                <div class="flex1 ml-8px">
                  <div>{{ row.productsName }}</div>
                  <div>编码 {{ row.productsCode }}</div>
                </div>
              </div>
            </template>
          </vxe-column>
          <vxe-column title="采购单位" field="orderUnitName" align="center" width="80"/>
          <vxe-column title="采购数量" field="orderQuantity" align="center" width="80"/>
          <vxe-column title="购货单价" field="orderPrice" width="80"/>
          <vxe-column title="基本单位" field="unitName" align="center" width="80"/>
          <vxe-column title="基本数量" field="sysQuantity" width="80"/>
          <vxe-column title="折扣率(%)" field="discount" width="90"/>
          <vxe-column title="折扣额" field="discountAmount" width="80"/>
          <vxe-column title="购货金额" field="discountedAmount" width="80"/>
          <vxe-column title="备注" field="remarks"/>
        </vxe-table>
      </div>
      <vxe-toolbar class-name="before-table">
        <template #tools>
          <div class="text-14px">
            <span class="ml-5px">合计金额： ¥<span class="red-color">{{ order.discountedAmount || 0 }}</span></span>
          </div>
        </template>
      </vxe-toolbar>
      <div class="mt-10px"></div>
      <div class="filler-panel">
        <div class="filler-item" style="flex: 1;margin: 5px 0px!important;">
          <label class="mr-16px  w-80px">备注说明：</label>
          {{ order.remarks || '无' }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {confirm, loading, message} from "heyui.ext";
import SalesOrder from "@js/api/SalesOrder";

export default {
  name: "SalesOrderView",
  props: {
    orderId: Number,
  },
  data() {
    return {
      order: {},
      productsData: [],
      showTimeLines: false,
    }
  },
  methods: {
    footerMethod({columns, data}) {
      let sums = [];
      columns.forEach((column) => {
        if (column.property && ['discountPrice', 'amount','returnQuantity','returnAmount'].includes(column.property)) {
          let total = 0;
          data.forEach((row) => {
            let rd = row[column.property];
            if (rd) {
              total += Number(rd || 0);
            }
          });

          sums.push(total.toFixed(2));
        }
      })
      return [["", "", "", "", "", "", "", ""].concat(sums)];
    },
    loadOrder() {
      loading("加载中....");
      SalesOrder.load(this.orderId).then(({data: {order, productsData}}) => {
        this.order = order || {};
        this.productsData = productsData || [];
      }).finally(() => loading.close());
    }
  },
  created() {
    this.loadOrder();
  },
}
</script>

<style scoped>

</style>