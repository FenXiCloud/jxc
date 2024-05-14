<template>
  <div class="frame-page flex flex-column">
    <vxe-toolbar>
      <template #buttons>
        <!--        <Select v-model="params.type" class="w-100px ml-2px" :datas="{'按客户':'按客户','按机构':'按机构'}" placeholder="对账方式"/>-->
        <div class="h-input-group">
          <span class="h-input-addon">单据日期</span>
          <DateRangePicker v-model="dateRange"></DateRangePicker>
        </div>
        <Input v-model="params.filter" class="w-200px ml-2px" placeholder="按商品名称/编码模糊查询"/>
        <Select class="w-200px ml-2px" :datas="customs" keyName="id" filterable titleName="name" v-model="customId" placeholder="客户" :multiple="true"/>
        <Button @click="loadList()" color="primary" :loading="loadingBut">查询</Button>
      </template>
      <template #tools>
      </template>
    </vxe-toolbar>
    <div class="flex1">
      <vxe-table row-id="id"
                 ref="table"
                 :data="dataList"
                 highlight-hover-row
                 show-footer
                 height="auto"
                 :footer-method="footerMethod"
                 :row-config="{height: 48}"
                 :column-config="{resizable: true}"
                 :sort-config="{remote:true}">
        <vxe-column title="序号" type="seq" width="60" align="center" fixed="left" footer-align="right"/>
        <vxe-column title="单据日期" field="billDate" width="100"/>
        <vxe-column title="单号" field="orderCode" width="220"/>
        <vxe-column title="客户名称" field="customersName" min-width="180"/>
        <vxe-column title="仓库" field="warehouseName" width="100"/>
        <vxe-column title="商品分类" field="categoryName" width="100"/>
        <vxe-column title="商品编码" width="120" field="productsCode"/>
        <vxe-column title="商品名称" min-width="160" field="productsName"/>
        <vxe-column title="基本数量" field="sysQuantity" width="80">
          <template #default="{row}">
            {{row.sysQuantity}}{{row.unitName}}
          </template>
        </vxe-column>
        <vxe-column title="下单数量" field="orderQuantity" width="80">
          <template #default="{row}">
            {{row.orderQuantity}}{{ row.orderUnitName}}
          </template>
        </vxe-column>
        <vxe-column title="下单单价" field="orderPrice" width="80"/>
        <vxe-column title="金额" field="discountedAmount" width="100"/>
        <vxe-column title="备注" field="remarks" min-width="80"/>
      </vxe-table>
    </div>
    <div class="flex justify-between items-center pt-5px">
      <div></div>
      <vxe-pager background @page-change="loadList(false)"
                 v-model:current-page="pagination.page"
                 v-model:page-size="pagination.pageSize"
                 v-model:page-sizes="pagination.pageSizes"
                 :total="pagination.total"
                 :layouts="['PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'Sizes', 'FullJump', 'Total']">
        <template #left>
          <span class="mr-12px text-16px">合计金额：{{ amountTotal }}</span>
          <vxe-button @click="loadList(false)" type="text" size="mini" icon="h-icon-refresh"
                      :loading="loading"></vxe-button>
        </template>
      </vxe-pager>
    </div>
  </div>
</template>

<script>
import manba from "manba";
import {message} from "heyui.ext";
import Customers from "@js/api/Customers";
import Products from "@js/api/Products";
import OrderReport from "@js/api/OrderReport";

const startTime = manba().startOf(manba.MONTH).format("YYYY-MM-dd");
const endTime = manba().format("YYYY-MM-dd");
export default {
  name: "SalesDetail",
  data() {
    return {
      loadingBut: false,
      loading: false,
      dataList: [],
      orgCusList: [],
      customs: [],
      customId: [],
      goodsList: [],
      productsIds: [],
      amountTotal: 0,
      totalParams: {},
      params: {
        orgCusId: null,
        customersIds: null,
        filter: null,
        productsIds: null,
      },
      dateRange: {
        start: manba(startTime).format("YYYY-MM-dd"),
        end: manba(endTime).format("YYYY-MM-dd")
      },
      pagination: {
        page: 1,
        pageSize: 200,
        pageSizes: [200, 500, 1000, 2000],
        total: 0
      },
    }
  },
  computed: {
    queryParams() {
      return Object.assign(this.params, {
        page: this.pagination.page,
        pageSize: this.pagination.pageSize,
        customersIds: this.customId.map(item => item).toString(),
        productsIds: this.productsIds.map(item => item).toString(),
        start: manba(this.dateRange.start).format("yyyy-MM-dd"),
        end: manba(this.dateRange.end).format("yyyy-MM-dd"),
      })
    },
    downParams() {
      return Object.assign(this.params, {
        customersIds: this.customId.map(item => item).toString(),
        productsIds: this.productsIds.map(item => item).toString(),
        start: manba(this.dateRange.start).format("yyyy-MM-dd"),
        end: manba(this.dateRange.end).format("yyyy-MM-dd"),
      })
    },
  },
  methods: {
    footerMethod({columns, data}) {
      let sums = [];
      columns.forEach((column) => {
        if (column.property && ['discountedAmount'].includes(column.property)) {
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
      return [["", "", "", "", "", "", "", "", "", ""].concat(sums)];
    },
    loadList(type = true) {
      if (this.dateRange.start && this.dateRange.end) {
        this.loading = true;
        OrderReport.salesDetail(this.queryParams).then(({data: {results, total}}) => {
          this.dataList = results;
          this.pagination.total = total;
        }).finally(() => this.loading = false);
      } else {
        message.error("请选择时间")
      }
    },
    filter(item, val) {
      if (item) {
        return item?.name.indexOf(val) !== -1 || item?.code.indexOf(val) !== -1 || item?.pinyin.indexOf(val) !== -1;
      }
    },
  },
  created() {
    Promise.all([
      Customers.select(),
      Products.select(),
    ]).then((results) => {
      this.customs = results[0].data;
      this.goodsList = results[1].data;
    });
    this.loadList()
  },
}
</script>
