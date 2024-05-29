<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-toolbar class-name="!size--mini">
        <template #tools>
          <Button icon="fa fa-save" color="primary" @click="confirm" :loading="loading">
            保存
          </Button>
        </template>
      </vxe-toolbar>
      <vxe-table
          size="mini"
          ref="xTable"
          border="border"
          :row-config="{height: 40}"
          show-footer
          :footer-method="footerMethod"
          :edit-config="{trigger: 'click', mode: 'row'}"
          stripe
          :data="productsList">
        <vxe-column title="序号" type="seq" width="60" align="center" fixed="left"/>
        <vxe-column title="单号" field="orderCode" align="center" min-width="200"/>
        <vxe-column title="供货商" field="vendorsName" align="center" min-width="200"/>
        <vxe-column title="总数量" field="unitName" align="center" width="200">
          <template #default="{row}">
            <div>
              {{row.quantity}}{{row.unitName}}
            </div>
          </template>
        </vxe-column>
        <vxe-column title="剩余出库数量" field="unitName" align="center" width="200">
          <template #default="{row}">
            <div>
              {{row.availableQuantity}}{{row.unitName}}
            </div>
          </template>
        </vxe-column>
        <vxe-column title="待出库原总成本" width="120">
          <template #default="{row}">
            {{ row.availableQuantity  * row.beforeUnitCost}}
          </template>
        </vxe-column>
        <vxe-column title="原单位成本" field="beforeUnitCost" align="center" width="120"/>
        <vxe-column title="单位成本调整" field="afterUnitCost" align="center" width="160" :edit-render="{}">
          <template #edit="{row,rowIndex}">
            <vxe-input :id="'r'+rowIndex+''+3" v-model.number="row.afterUnitCost"
                       type="float" :controls="false" @blur="updateAdjustmentAmount(row)"></vxe-input>
          </template>
        </vxe-column>
        <vxe-column title="待出库总成本" width="120">
          <template #default="{row}">
            {{ row.afterUnitCost ? row.afterUnitCost * row.availableQuantity : 0 }}
          </template>
        </vxe-column>
        <vxe-column title="调整成本" field="adjustmentAmount" width="120"/>
      </vxe-table>
    </div>
  </div>
</template>
<script>

import {confirm, loading, message} from "heyui.ext";
import StockItem from "@js/api/StockItem";
import StockAdjustment from "@js/api/StockAdjustment";

export default {
  name: "StockCostAdjustmentForm",
  props: {
    stockId: [String, Number],
    productsId: [String, Number],
    warehouseId: [String, Number],
  },
  data() {
    return {
      loading: false,
      amount:0,
      productsList: [],
    }
  },
  methods: {
    footerMethod({columns, data}) {
      let sums = [];
      columns.forEach((column) => {
        if (column.property && [ 'adjustmentAmount'].includes(column.property)) {
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
      this.amount = sums[0]
      return [["", "", "", "","","","", "", ""].concat(sums)];
    },
    //计算公式
    updateAdjustmentAmount(item) {
      item.adjustmentAmount = (item.afterUnitCost - item.beforeUnitCost) *  item.availableQuantity|| 0;
      console.log(item.adjustmentAmount)
      this.$refs.xTable.updateFooter();
    },
    //保存订单
    confirm() {
      loading("保存中....");
      let detailList = this.productsList.filter(c => c.afterUnitCost && c.afterUnitCost !== null);
      if (detailList === null || detailList.length === 0) {
        message.error("未填写成本调整数据~");
        loading.close()
        return
      }
      StockAdjustment.save({
        stockId:this.stockId,
        productsId:this.productsId,
        warehouseId:this.warehouseId,
        amount:this.amount,
        detailList: detailList
      }).then((success) => {
        if(success){
          message("保存成功~");
          this.$emit('success');
        }
      }).finally(()=>{
        loading.close();
      })
    },
    //选择供货商时加载商品
    loadAdjustment() {
      StockItem.adjustmentDetail({productsId:this.productsId,warehouseId:this.warehouseId}).then(({data}) => {
        this.productsList = data || [];
      })
    },
  },
  created() {
    this.loadAdjustment()
  },
}
</script>
