<template>
  <div class="frame-page flex flex-column">
    <vxe-toolbar>
      <template #buttons>
        <Search v-model.trim="params.filter" search-button-theme="h-btn-default"
                show-search-button class="w-360px"
                placeholder="请输入商品名称/单号" @search="doSearch">
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
        <vxe-column title="单号" field="code" width="220"></vxe-column>
        <vxe-column title="调整时间" field="billDate" width="120"></vxe-column>
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
        <vxe-column title="调整人" field="createName" width="120"></vxe-column>
        <vxe-column title="调整金额" field="amount" width="120"></vxe-column>
        <vxe-column title="操作" align="center" width="200">
          <template #default="{row}">
            <span class="primary-color  text-hover ml-10px" @click="toDetail(row)">调整内容</span>
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
import StockCostAdjustmentForm from "@components/group/stock/StockCostAdjustmentForm.vue";
import StockAdjustment from "@js/api/StockAdjustment";
import StockCostAdjustmentDetail from "@components/group/stock/StockCostAdjustmentDetail.vue";

const startTime = manba().startOf(manba.MONTH).format("YYYY-MM-dd");
const endTime = manba().endOf(manba.DAY).format("YYYY-MM-dd");

export default {
  name: "StockCostAdjustmentList",
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
    toDetail(row){
      let adjustmentId = row.id
      let layerId = layer.drawer({
        title: row.productsName+"--"+row.warehouseName+"--成本调整单",
        shadeClose: false,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(StockCostAdjustmentDetail, {
          adjustmentId,
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
      StockAdjustment.list(this.queryParams).then(({data: {results, total}}) => {
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
