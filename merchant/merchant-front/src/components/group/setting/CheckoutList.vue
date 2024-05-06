<template>
  <div class="frame-page">
    <div class="h-panel">
      <div class="h-panel-body">
        <div class="table-toolbar">
          <div class="table-toolbar-left">
            <label for="checkYear" class="mr-10px">年度</label>
            <DatePicker id="checkYear" v-model="params.checkYear" type="year" placeholder="请选择年度"/>
            <Button color="primary" icon="fa fa-search" :loading="loading" @click="doSearch">查询</Button>
          </div>
          <div class="table-toolbar-right">
            当前会计期：{{ checkout.checkYear }}-{{ checkout.checkMonth }}期
          </div>
        </div>
        <vxe-table row-id="id"
                   ref="table"
                   :data="dataList"
                   highlight-hover-row
                   show-overflow
                   :loading="loading">
          <vxe-column type="seq" width="60" align="center"/>
          <vxe-column title="操作日期" field="checkDate"/>
          <vxe-column title="年度" field="checkYear"/>
          <vxe-column title="月度" field="checkMonth"/>
          <vxe-column title="结账状态" field="status">
            <template #default="{row:{status}}"><!--0未结账 1 已结账 -->
              <Tag color="green" v-if="status==1">已结账</Tag>
              <Tag color="red" v-else>未结账</Tag>
            </template>
          </vxe-column>
          <vxe-column title="操作" align="center" width="130">
            <template #default="{row}">
              <div class="flex items-center justify-center">
                <!--TODO 结账前需要检查所有单据审核状态，如发现提示 14条未处理 显示明细：单号 单据类型 组织名称  结账时会自动创建下一会计期间，状态未结账-->
                <span v-if="row.status==0" class=" primary-color text-hover ml-10px" @click="checkoutForm(row)"
                      size="s">结账</span>
                <span v-else class="primary-color ml-10px text-hover" @click="unCheckoutForm(row)"
                      size="s">反结账</span>
              </div>
            </template>
          </vxe-column>
        </vxe-table>
        <Pagination align="right" class="mt-16px" v-model="pagination" @change="pageChange" small/>
      </div>
    </div>
  </div>
</template>

<script>
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import CheckoutForm from "@components/group/setting/CheckoutForm";
import Checkout from "@js/api/Checkout";
import {confirm, message} from "heyui.ext";


export default {
  name: "CheckoutList",
  data() {
    return {
      loading: false,
      params: {
        checkYear: null,
      },
      checkedRows: [],
      dataList: [],
      pagination: {
        page: 1,
        size: 20,
        total: 0
      },
    }
  },
  computed: {
    checkout: {
      get() {
        return this.$store.state.checkout;
      }
    },
    queryParams() {
      return Object.assign(this.params, {
        page: this.pagination.page,
        pageSize: this.pagination.size
      })
    }
  },
  methods: {
    checkoutForm(checkout) {
      this.loading = true;
      Checkout.check(checkout).then(({data: {notData, minCheck}}) => {
        if (notData) {
          let layerId = layer.open({
            title: `[${checkout.checkYear}-${checkout.checkMonth}]结账${notData.length}条未审核数据`,
            shadeClose: false,
            closeBtn: false,
            area: ['600px', '600px'],
            content: h(CheckoutForm, {
              notData,
              onClose: () => {
                layer.close(layerId);
              },
              onSuccess: () => {
                layer.close(layerId);
              }
            })
          });
        }
        if (minCheck) {
          this.$store.commit('updateCheckout', minCheck);
          message('操作成功~');
          this.doSearch();
        }
      }).finally(() => this.loading = false);
    },
    unCheckoutForm(checkout) {
      confirm({
        title: "系统提示",
        content: `反月末结转可补录目标月份数据，补录后会影响“反月结会计期”月份报表数据请慎重操作?`,
        onConfirm: () => {
          this.loading = true;
          Checkout.update(checkout).then(({data}) => {
            if (data) {
              this.$store.commit('updateCheckout', data);
              message('操作成功~');
              this.loadList();
            }
          }).finally(() => this.loading = false);
        }
      })
    },
    loadList() {
      this.loading = true;
      Checkout.list(this.queryParams).then(({data}) => {
        this.dataList = data.results;
        this.pagination.total = data.total;
      }).finally(() => this.loading = false);
    },
    pageChange() {
      this.loadList();
    },
    tableCheck() {
      this.checkedRows = this.$refs.table.getCheckboxRecords();
    },
    doSearch() {
      this.pagination.page = 1;
      this.loadList();
    },
  },
  created() {
    this.doSearch();
  }
}
</script>

<style scoped>

</style>
