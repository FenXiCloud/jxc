<template>
  <div class="frame-page flex flex-column">
    <vxe-toolbar>
      <template #buttons>
        <Select v-model="params.state" class="w-120px" :datas="{待审核:'待审核',已审核:'已审核'}"
                placeholder="全部订单"/>
        <div class="h-input-group">
          <span class="h-input-addon ml-8px">单据日期</span>
          <DateRangePicker v-model="dateRange"></DateRangePicker>
        </div>
        <Search v-model.trim="params.filter" search-button-theme="h-btn-default"
                show-search-button class="w-360px ml-8px"
                placeholder="请输入订单号/客户名称" @search="doSearch">
          <i class="h-icon-search"/>
        </Search>
      </template>
      <template #tools>
        <Button @click="showForm()" color="primary">新 增</Button>
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
        <vxe-column title="单号" field="code" width="200"/>
        <vxe-column title="客户名称" field="customersName" min-width="120"/>
        <vxe-column title="创建时间" field="createDate" align="center" width="200" />
        <vxe-column title="单据日期" field="billDate" align="center" width="130" />
        <vxe-column title="销售金额" field="discountedAmount" width="120"/>
        <vxe-column title="销售成本" field="cost" width="120"/>
        <vxe-column title="销售毛利" width="120">
          <template #default="{row}">
            {{row.discountedAmount - row.cost}}
          </template>
        </vxe-column>
        <vxe-column title="操作" align="center" width="200">
          <template #default="{row}">
            <span class="primary-color  text-hover ml-10px" @click="showOrderView(row.id,row.purchaserOrderState)">详情</span>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <div class="flex justify-between items-center pt-5px">
      <div>
      </div>
      <vxe-pager perfect @page-change="loadList(false)"
                 v-model:current-page="pagination.page"
                 v-model:page-size="pagination.pageSize"
                 :total="pagination.total"
                 :layouts="['PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'Sizes', 'FullJump', 'Total']">
        <template #left>
          <!--          <span class="mr-12px text-16px">总金额：{{ amountTotal }}元</span>-->
          <vxe-button @click="loadList(false)" type="text" size="mini" icon="fa fa-refresh"
                      :loading="loading"></vxe-button>
        </template>
      </vxe-pager>
    </div>
  </div>
</template>
<script>
import manba from "manba";
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import SalesOrder from "@js/api/SalesOrder";
import SalesProfitView from "@components/group/sales/SalesProfitView.vue";

const startTime = manba().startOf(manba.MONTH).format("YYYY-MM-dd");
const endTime = manba().endOf(manba.DAY).format("YYYY-MM-dd");

export default {
  name: "SalesProfitList",
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
        orderType: '采购订单',
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
    downParams() {
      return Object.assign({
        filter: this.params.filter,
        state: this.params.state,
        orderType: this.params.orderType,
        start: this.dateRange.start,
        end: this.dateRange.end,
      })
    }
  },
  methods: {
    sortChangeEvent({field, order}) {
      this.params.sortCol = field;
      this.params.sort = order;
      this.loadList(false);
    },
    footerMethod({columns, data}) {
      let sums = [];
      columns.forEach((column) => {
        if (column.property && ['discountedAmount','cost'].includes(column.property)) {
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
    showOrderView(orderId = null, state) {
      let layerId = layer.drawer({
        title: "销售出库单信息",
        shadeClose: false,
        closeBtn: 1,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(SalesProfitView, {
          orderId,
          onClose: () => {
            layer.close(layerId);
          },
          onSuccess: () => {
            layer.close(layerId);
          },
        }),
        onClose: () => {
          if (state === '待审核') {
            this.loadList();
          }
        }
      });
    },
    doSearch() {
      this.pagination.page = 1;
      this.loadList();
    },
    loadList(type = true) {
      this.loading = true;
      this.loadTotal(type)
      SalesOrder.profit(this.queryParams).then(({data: {results, total}}) => {
        this.dataList = results || [];
        this.pagination.total = total;
      }).finally(() => this.loading = false);
    },
    loadTotal(type) {
    },
  },
  created() {
    this.loadList();
  }
}
</script>
