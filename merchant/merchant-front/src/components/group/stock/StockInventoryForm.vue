<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-toolbar class-name="!size--mini">
        <template #buttons><label class="mr-20px" style="font-size: 16px !important;">盘点日期：</label>
          <DatePicker v-model="form.stockDate" :clearable="false" disabled="false"></DatePicker>
          <label class="ml-10px" style="font-size: 16px !important;">仓库：</label>
          <Select class="w-260px" filterable required :datas="warehousesList" keyName="id" titleName="name"
                  :deletable="false" @change="warehousesChange($event)" v-model="params.warehousesId"
                  placeholder="请选择仓库"/>
        </template>
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
          :edit-config="{trigger: 'click', mode: 'row'}"
          stripe
          :data="productsStockData">
        <vxe-column title="序号" type="seq" width="60" align="center" fixed="left"/>
        <vxe-column title="仓库" field="warehouses" align="center" width="200"/>
        <vxe-column title="商品信息" min-width="300">
          <template #default="{row,rowIndex}">
            <div class="flex">
              <div class="flex1 ml-8px">
                <div>{{ row.productsCode }}--{{ row.productsName }}</div>
              </div>
            </div>
          </template>
        </vxe-column>
        <vxe-column title="规格" field="specification" align="center" width="120"/>
        <vxe-column title="商品类别" field="categoryName" align="center" width="120"/>
        <vxe-column title="基本单位" field="unitName" align="center" width="120"/>
        <vxe-column title="系统库存" field="sysQuantity" align="center" width="120"/>
        <vxe-column title="盘点库存" field="inventoryQuantity" align="center" width="160" :edit-render="{}">
          <template #edit="{row,rowIndex}">
            <vxe-input :id="'r'+rowIndex+''+3" ref="inventoryQuantity" v-model.number="row.inventoryQuantity"
                       type="float" min="0" :controls="false"></vxe-input>
          </template>
        </vxe-column>
        <vxe-column title="盘盈盘亏" width="90">
          <template #default="{row}">
            {{ row.inventoryQuantity ? row.inventoryQuantity - row.sysQuantity : 0 }}
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <div class="flex justify-between items-center pt-5px">
      <div>
      </div>
      <vxe-pager perfect @page-change="loadProducts(false)"
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

import {confirm, loading, message} from "heyui.ext";
import manba from "manba";
import {CopyObj} from "@common/utils";
import Warehouses from "@js/api/Warehouses";
import StockInbound from "@js/api/StockInbound";
import StockInventory from "@js/api/StockInventory";

export default {
  name: "StockInventoryForm",
  props: {
    orderId: [String, Number],
    type: String,
  },
  computed: {
    queryParams() {
      return Object.assign(this.params, {
        page: this.pagination.page,
        pageSize: this.pagination.pageSize,
      })
    },
  },
  data() {
    return {
      params: {
        filter: null,
        warehousesId: null,
      },
      loading: false,
      warehousesList: [],
      pagination: {
        page: 1,
        pageSize: 20,
        total: 0
      },
      form: {
        id: null,
        stockDate: manba().format("YYYY-MM-dd"),
      },
      productsStockData: [],
    }
  },
  methods: {
    //保存订单
    confirm() {
      loading("保存中....");
      let inventoryList = this.productsStockData.filter(c => c.inventoryQuantity && c.inventoryQuantity !== null);
      if (inventoryList === null || inventoryList.length === 0) {
        message.error("未填写盘点数据~");
        loading.close()
        return
      }
      StockInventory.save({
        inventory: Object.assign(this.form),
        type: this.type,
        itemList: inventoryList
      }).then(() => {
        loading.close();
        message("保存成功~");
        this.$emit('success');
      })
    },
    //修改仓库
    warehousesChange(e) {
      if (this.form.vendorsId !== null) {
        confirm({
          title: "系统提示",
          content: `修改客户，所选供应商将被清空，确定修改？`,
          onConfirm: () => {
            this.form.customersId = e.id;
            this.form.vendorsId = null;
          },
          onCancel: () => {
            this.form.customersId = null;
          }
        })
      }
    },
    //选择供货商时加载商品
    loadProducts() {
      this.loading = true;
      // this.loadTotal(type)
      StockInventory.productsSelect(this.queryParams).then(({data: {results, total}}) => {
        this.productsStockData = results || [];
        this.pagination.total = total;
      }).finally(() => this.loading = false);
    },
  },
  created() {
    loading("加载中....");
    this.loadProducts()
    Promise.all([
      Warehouses.select(),
    ]).then((results) => {
      this.warehousesList = results[0].data || [];
      if (this.warehousesList != null) {
        this.warehousesId = this.warehousesList.find(val => val.isDefault).id
      }
      //订单详情/编辑订单
      if (this.orderId) {
        StockInbound.load(this.orderId).then(({data: {order, productsData}}) => {
          if (order) {
            CopyObj(this.form, order);
            this.vendorsId = order.vendorsId
            if ('copy' === this.type) {
              this.form.id = null;
            }
          }
          this.productsData = productsData || [];
          this.productsData.push({isNew: true});
        })
      }
    }).finally(() => loading.close());
  },
}
</script>
