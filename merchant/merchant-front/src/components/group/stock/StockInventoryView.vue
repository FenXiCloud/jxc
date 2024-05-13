<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-toolbar class-name="!p-0px">
        <template #buttons>
          <span style="font-size: 15px!important;">订单号: {{ order.code }}</span>
          <span class="ml-16px" style="font-size: 15px!important;" v-if="order.inOrderCode">盘盈单: {{ order.inOrderCode }}</span>
          <span class="ml-16px" style="font-size: 15px!important;" v-if="order.outOrderCode">盘亏单: {{ order.outOrderCode }}</span>
        </template>
        <template #tools>
          <Button @click="toOrder" color="primary">生成盘点单据</Button>
        </template>
      </vxe-toolbar>
      <div class="pt-13px">
        <vxe-table
            size="mini"
            ref="xTable"
            :row-config="{height: 48}"
            border highlight-hover-row show-overflow
            :data="productsData">
          <vxe-column title="序号" type="seq" width="60" align="center" fixed="left"/>
          <vxe-column title="仓库" field="warehousesName" align="center" width="200"/>
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
          <vxe-column title="盘点库存" field="inventoryQuantity" align="center" width="160" :edit-render="{}"/>
          <vxe-column title="盘盈盘亏" width="90">
            <template #default="{row}">
              {{ row.inventoryQuantity ? row.inventoryQuantity - row.sysQuantity : 0 }}
            </template>
          </vxe-column>
        </vxe-table>
      </div>
      <vxe-toolbar class-name="before-table">
        <template #tools>
        </template>
      </vxe-toolbar>
      <div class="mt-10px"></div>
      <div class="filler-panel">
      </div>
    </div>

    <Modal v-model="opened">
      <template #header>
        生成盘点单据
      </template>
      <div  style="text-align: center">
        <Button class="w-160px" @click="toInOrder('盘盈',inList)" color="primary" v-if="isIn">生成盘盈单</Button>
      </div>
      <div class="mt-8px"  style="text-align: center">
        <Button class="w-160px" @click="toOutOrder('盘亏',outList)" color="primary" v-if="isOut">生成盘亏单</Button>
      </div>
      <template #footer>
        <Button @click="opened =false">关闭</Button>
      </template>
    </Modal>
  </div>
</template>

<script>
import {confirm, loading, message} from "heyui.ext";
import StockInventory from "@js/api/StockInventory";
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import StockInboundForm from "@components/group/stock/StockInboundForm.vue";
import StockOutboundForm from "@components/group/stock/StockOutboundForm.vue";

export default {
  name: "StockInventoryView",
  props: {
    orderId: Number,
  },
  data() {
    return {
      order: {},
      productsData: [],
      showTimeLines: false,
      opened: false,
      isIn: false,
      isOut: false,
      inList: [],
      outList: [],
    }
  },
  methods: {
    loadOrder() {
      loading("加载中....");
      StockInventory.load(this.orderId).then(({data: {inventory, itemList}}) => {
        this.order = inventory || {};
        this.productsData = itemList || [];
      }).finally(() => loading.close());
    },
    toOrder() {
      this.inList = this.productsData.filter(c => c.sysQuantity < c.inventoryQuantity);
      this.outList = this.productsData.filter(c => c.sysQuantity > c.inventoryQuantity);
      if (this.inList && this.inList.length > 0 &&!this.order.inOrderId) {
        this.isIn = true
        this.opened = true
      }
      if (this.outList && this.outList.length > 0 && !this.order.outOrderId) {
        this.isOut = true
        this.opened = true
      }
      if(!this.opened){
        message('无可生成数据');
      }
    },
    toInOrder(bType,pList) {
      this.opened = false
      let inventoryId = this.orderId
        let layerId = layer.drawer({
          title: "其他入库单",
          shadeClose: false,
          ZIndex: 100,
          area: ['90vw', '100vh'],
          content: h(StockInboundForm, {
            bType,
            pList,
            inventoryId,
            onClose: () => {
              this.loadOrder();
              layer.close(layerId);
            },
            onSuccess: () => {
              this.loadOrder();
              layer.close(layerId);
            }
          }),
          onClose: () => {
            this.loadOrder();
          }
        });
    },
    toOutOrder(bType,pList) {
      this.opened = false
      let inventoryId = this.orderId
        let layerId = layer.drawer({
          title: "其他出库单",
          shadeClose: false,
          ZIndex: 100,
          area: ['90vw', '100vh'],
          content: h(StockOutboundForm, {
            bType,
            pList,
            inventoryId,
            onClose: () => {
              this.loadOrder();
              layer.close(layerId);
            },
            onSuccess: () => {
              this.loadOrder();
              layer.close(layerId);
            }
          }),
          onClose: () => {
            this.loadOrder();
          }
        });
    },
  },
  created() {
    this.loadOrder();
  },
}
</script>

<style scoped>

</style>