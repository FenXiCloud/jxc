<template>
  <div class="frame-page flex flex-column">
    <vxe-toolbar>
      <template #buttons>
        <Search v-model.trim="params.filter" search-button-theme="h-btn-default"
                show-search-button class="w-360px"
                placeholder="请输入商品名称" @search="doSearch">
          <i class="h-icon-search"/>
        </Search>
      </template>
    </vxe-toolbar>
    <div class="flex1">
      <vxe-table row-id="id"
                 ref="table"
                 height="auto"
                 :data="dataList"
                 highlight-hover-row
                 show-overflow
                 show-footer
                 :footer-method="footerMethod"
                 :row-config="{height: 48}"
                 :column-config="{resizable: true}"
                 :sort-config="{remote:true}"
                 @sort-change="sortChangeEvent"
                 :loading="loading">
        <vxe-column type="checkbox" width="40" align="center"/>
        <vxe-column type="seq" width="50" title="序号"/>
        <vxe-column title="分类" field="categoryName" width="120"></vxe-column>
        <vxe-column title="商品" field="productsName" min-width="120">
          <template #default="{row}">
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
        <vxe-column title="仓库" field="warehouseName" width="120"></vxe-column>
        <vxe-column title="总成本" field="weightedCost" width="120"></vxe-column>
        <vxe-column title="可出库数量" field="totalQuantity" align="center" width="130">
          <template #default="{row}">
            <div>{{ row.totalQuantity }}{{ row.unitsName }}</div>
          </template>
        </vxe-column>
        <vxe-column title="操作" align="center" width="200">
          <template #default="{row}">
            <span class="primary-color  text-hover ml-10px" @click="toAdjustment(row)">成本调整</span>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <div class="flex justify-between items-center pt-5px">
      <div></div>
      <vxe-pager perfect @page-change="loadList(false)"
                 v-model:current-page="pagination.page"
                 v-model:page-size="pagination.pageSize"
                 :total="pagination.total"
                 :layouts="['PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'Sizes', 'FullJump', 'Total']">

      </vxe-pager>
    </div>
  </div>
</template>
<script>
import manba from "manba";
import Stock from "@js/api/Stock";
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import StockInboundForm from "@components/group/stock/StockInboundForm.vue";
import StockCostAdjustmentForm from "@components/group/stock/StockCostAdjustmentForm.vue";

const startTime = manba().startOf(manba.MONTH).format("YYYY-MM-dd");
const endTime = manba().endOf(manba.DAY).format("YYYY-MM-dd");

export default {
  name: "StockCostAdjustmentView",
  data() {
    return {
      dataList: [],
      loading: false,
      amountTotal: 0,
      totalParams: {},
      pagination: {
        page: 1,
        pageSize: 20,
        total: 0
      },
      params: {
        filter: null,
        state: null,
        sortCol: null,
        sort: null,
      },
      dateRange: {
        start: manba(startTime).format("YYYY-MM-dd"),
        end: manba(endTime).format("YYYY-MM-dd")
      },
    }
  },
  computed: {
    queryParams() {
      return Object.assign(this.params, {
        page: this.pagination.page,
        pageSize: this.pagination.pageSize,
        start: this.dateRange.start,
        end: this.dateRange.end,
      })
    },
  },
  methods: {
    toAdjustment(row){
      let productsId= row.productsId
      let warehouseId= row.warehouseId
      let stockId= row.id
      let layerId = layer.drawer({
        title: row.productsName+"--"+row.warehouseName+"--成本调整单",
        shadeClose: false,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(StockCostAdjustmentForm, {
          stockId,
          productsId,
          warehouseId,
          onClose: () => {
            this.doSearch();
            layer.close(layerId);
          },
          onSuccess: () => {
            this.doSearch();
            layer.close(layerId);
          }
        }),
        onClose: () => {
          this.doSearch();
        }
      });
    },
    sortChangeEvent({field, order}) {
      this.params.sortCol = field;
      this.params.sort = order;
      this.loadList(false);
    },
    footerMethod({columns, data}) {
      let sums = [];
      columns.forEach((column) => {
        if (column.property && ['amount', 'returnAmount'].includes(column.property)) {
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
      return [["", "", "", "", "", ""].concat(sums)];
    },
    doSearch() {
      this.pagination.page = 1;
      this.loadList();
    },
    loadList() {
      this.loading = true;
      Stock.adjustment(this.queryParams).then(({data: {results, total}}) => {
        this.dataList = results || [];
        this.pagination.total = total;
      }).finally(() => this.loading = false);
    },
  },
  created() {
    this.loadList();
  }
}
</script>
