<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-table
          size="mini"
          ref="xTable"
          border="border"
          :row-config="{height: 40}"
          show-footer
          stripe
          :data="detailList">
        <vxe-column title="序号" type="seq" width="60" align="center" fixed="left"/>
        <vxe-column title="单号" field="orderCode" align="center" min-width="200"/>
        <vxe-column title="供货商" field="vendorsName" align="center" min-width="200"/>
        <vxe-column title="总数量" field="quantity" align="center" width="200">
          <template #default="{row}">
            <div>
              {{row.quantity}}{{row.unitName}}
            </div>
          </template>
        </vxe-column>
        <vxe-column title="调整时剩余出库数量" field="unitName" align="center" width="200">
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
        <vxe-column title="调整单位成本" field="afterUnitCost" align="center" width="160">
          <template #edit="{row,rowIndex}">
            <vxe-input :id="'r'+rowIndex+''+3" v-model.number="row.afterUnitCost"
                       type="float" :controls="false"></vxe-input>
          </template>
        </vxe-column>
        <vxe-column title="调整待出库总成本" width="160">
          <template #default="{row}">
            {{ row.afterUnitCost ? row.afterUnitCost * row.availableQuantity : 0 }}
          </template>
        </vxe-column>
        <vxe-column title="调整成本" field="adjustmentAmount" width="120"/>
      </vxe-table>
    </div>
    <vxe-toolbar class-name="before-table">
      <template #tools>
        <div class="text-14px">
          <span class="ml-5px">调整总金额： ¥<span class="red-color">{{ adjustment.amount || 0 }}</span></span>
        </div>
      </template>
    </vxe-toolbar>
  </div>
</template>
<script>
import StockAdjustment from "@js/api/StockAdjustment";

export default {
  name: "StockCostAdjustmentDetail",
  props: {
    adjustmentId: [String, Number],
  },
  data() {
    return {
      loading: false,
      detailList: [],
      adjustment:{},
    }
  },
  methods: {
    //选择供货商时加载商品
    loadAdjustment() {
      StockAdjustment.load(this.adjustmentId).then(({data}) => {
        this.detailList = data.detailList || [];
        this.adjustment = data.adjustment || [];
      })
    },
  },
  created() {
    this.loadAdjustment()
  },
}
</script>
