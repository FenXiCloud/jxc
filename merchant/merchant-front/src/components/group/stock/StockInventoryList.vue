<template>
  <div class="frame-page flex flex-column">
    <vxe-toolbar>
      <template #buttons>
        <div class="h-input-group">
          <span class="h-input-addon">盘点日期</span>
          <DateRangePicker v-model="dateRange"></DateRangePicker>
        </div>
        <Search v-model.trim="params.filter" search-button-theme="h-btn-default"
                show-search-button class="w-360px ml-8px"
                placeholder="请输入盘点单号" @search="doSearch">
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
                 :row-config="{height: 48}"
                 :column-config="{resizable: true}"
                 :loading="loading">
        <vxe-column type="checkbox" width="40" align="center"/>
        <vxe-column type="seq" width="50" title="序号"/>
        <vxe-column title="盘点时间" field="stockDate" width="200"/>
        <vxe-column title="盘点单号" field="customersName" min-width="120">
          <template #default="{row}">
            <div class="text-hover" @click="showOrderView(row.id)">
              <span>{{ row.code }}</span>
            </div>
          </template>
        </vxe-column>
        <vxe-column title="盘盈单" field="inOrderCode" align="center" width="200">
          <template #default="{row}">
            <div class="text-hover" @click="showInOrder(row.inOrderId)">
              <span>{{ row.inOrderCode }}</span>
            </div>
          </template>
        </vxe-column>
        <vxe-column title="盘亏单" field="outOrderCode" align="center" width="200">
          <template #default="{row}">
            <div class="text-hover" @click="showOutOrder(row.outOrderId)">
              <span>{{ row.outOrderCode }}</span>
            </div>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <div class="flex justify-between items-center pt-5px">
      <vxe-pager perfect @page-change="loadList(false)"
                 v-model:current-page="pagination.page"
                 v-model:page-size="pagination.pageSize"
                 :total="pagination.total"
                 :layouts="['PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'Sizes', 'FullJump', 'Total']">
        <template #left>
          <vxe-button @click="loadList(false)" type="text" size="mini" icon="fa fa-refresh"
                      :loading="loading"></vxe-button>
        </template>
      </vxe-pager>
    </div>
  </div>
</template>
<script>
import {confirm, message} from "heyui.ext";
import manba from "manba";
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import StockInventoryForm from "@components/group/stock/StockInventoryForm.vue";
import StockInventoryView from "@components/group/stock/StockInventoryView.vue";
import StockInventory from "@js/api/StockInventory";
import StockInboundView from "@components/group/stock/StockInboundView.vue";
import StockOutboundView from "@components/group/stock/StockOutboundView.vue";

const startTime = manba().startOf(manba.MONTH).format("YYYY-MM-dd");
const endTime = manba().endOf(manba.DAY).format("YYYY-MM-dd");

export default {
  name: "StockInventoryList",
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
    showForm(type = 'add', orderId = null) {
      let layerId = layer.drawer({
        title: "盘点单内容",
        shadeClose: false,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(StockInventoryForm, {
          type,
          orderId,
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
    showOrderView(orderId = null, state) {
      let layerId = layer.drawer({
        title: "盘点单内容",
        shadeClose: false,
        closeBtn: 1,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(StockInventoryView, {
          orderId,
          onClose: () => {
            layer.close(layerId);
          },
          onSuccess: () => {
            layer.close(layerId);
          },
        }),
        onClose: () => {
          this.doSearch();
        }
      });
    },
    showOutOrder(orderId = null, state) {
      let layerId = layer.drawer({
        title: "盘亏单内容",
        shadeClose: false,
        closeBtn: 1,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(StockOutboundView, {
          orderId,
          onClose: () => {
            layer.close(layerId);
          },
          onSuccess: () => {
            layer.close(layerId);
          },
        }),
        onClose: () => {
          this.loadList();
        }
      });
    },
    showInOrder(orderId = null, state) {
      let layerId = layer.drawer({
        title: "盘盈单内容",
        shadeClose: false,
        closeBtn: 1,
        ZIndex: 100,
        area: ['90vw', '100vh'],
        content: h(StockInboundView, {
          orderId,
          onClose: () => {
            layer.close(layerId);
          },
          onSuccess: () => {
            layer.close(layerId);
          },
        }),
        onClose: () => {
          this.loadList();
        }
      });
    },
    doSearch() {
      this.pagination.page = 1;
      this.loadList();
    },
    loadList(type = true) {
      this.loading = true;
      // this.loadTotal(type)
      StockInventory.list(this.queryParams).then(({data: {results, total}}) => {
        this.dataList = results || [];
        this.pagination.total = total;
      }).finally(() => this.loading = false);
    },
    doRemove(row) {
      confirm({
        title: "系统提示",
        content: `确认删除：${row.vendorsName}-单号：${row.code}?`,
        onConfirm: () => {
          StockInventory.remove(row.id).then(() => {
            message("删除成功~");
            this.loadList();
          })
        }
      })
    }
  },
  created() {
    this.loadList();
  }
}
</script>
