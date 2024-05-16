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
        <Select class="w-200px ml-2px" :datas="warehousesList" keyName="id" filterable titleName="name" v-model="warehousesId" placeholder="仓库" :multiple="true"/>
        <Button @click="loadList()" color="primary" :loading="loadingBut">查询</Button>
      </template>
      <template #tools>
      </template>
    </vxe-toolbar>
    <div class="flex1">
      <vxe-table row-id="id"
                 ref="table"
                 border
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
        <vxe-column title="仓库" field="warehouseName" width="100"/>
        <vxe-column title="商品分类" field="categoryName" width="100"/>
        <vxe-column title="商品编码" width="120" field="productsCode"/>
        <vxe-column title="商品名称" min-width="160" field="productsName"/>
        <vxe-colgroup title="入库信息" align="center">
          <vxe-column title="入库数量" field="quantity" width="120">
            <template #default="{row}">
              <template v-if="row.inQuantity">
                {{row.inQuantity}}{{row.unitName}}
              </template>
            </template>
          </vxe-column>
          <vxe-column title="入库金额" field="inTotalAmount" width="120"/>
        </vxe-colgroup>
        <vxe-colgroup title="出库信息" align="center">
          <vxe-column title="出库数量" field="quantity" width="120">
            <template #default="{row}">
              <template v-if="row.outQuantity">
              {{row.outQuantity}}{{row.unitName}}
              </template>
            </template>
          </vxe-column>
          <vxe-column title="出库金额" field="outTotalAmount" width="120" />
          <vxe-column title="单位成本" field="unitCost" width="120">
            <template #default="{row}">
              <template v-if="row.outQuantity">
                {{row.unitCost}}
              </template>
            </template>
          </vxe-column>
          <vxe-column title="成本" field="cost" width="120">
            <template #default="{row}">
              <template v-if="row.outQuantity">
                {{row.cost}}
              </template>
            </template>
          </vxe-column>
        </vxe-colgroup>
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
import Products from "@js/api/Products";
import StockItem from "@js/api/StockItem";
import Warehouses from "@js/api/Warehouses";

const startTime = manba().startOf(manba.MONTH).format("YYYY-MM-dd");
const endTime = manba().format("YYYY-MM-dd");
export default {
  name: "StockDetail",
  data() {
    return {
      loadingBut: false,
      loading: false,
      dataList: [],
      warehousesList: [],
      warehousesId: [],
      productsList: [],
      productsIds: [],
      params: {
        warehousesIds: null,
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
        warehousesIds: this.warehousesId.map(item => item).toString(),
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
        if (column.property && ['outTotalAmount','inTotalAmount'].includes(column.property)) {
          let total = 0;
          data.forEach((row) => {
            let rd = row[column.property];
            if (rd) {
              total += Number(rd || 0);
            }
          });
          sums.push(total.toFixed(2));
        }else {
          sums.push("");
        }
      })
      return [sums];
    },
    loadList(type = true) {
      if (this.dateRange.start && this.dateRange.end) {
        this.loading = true;
        StockItem.detail(this.queryParams).then(({data: {results, total}}) => {
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
      Products.select(),
      Warehouses.select(),
    ]).then((results) => {
      this.productsList = results[0].data;
      this.warehousesList = results[1].data;
    });
    this.loadList()
  },
}
</script>
