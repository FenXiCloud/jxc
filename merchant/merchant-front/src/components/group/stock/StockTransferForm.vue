<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-toolbar class-name="!size--mini">
        <template #buttons> <label class="mr-20px ml-16px" style="font-size: 16px !important;">单据日期：</label>
          <DatePicker v-model="form.billDate" :clearable="false"></DatePicker>
          <label class="ml-10px" style="font-size: 16px !important;">调出仓库：</label>
          <Select class="w-260px" filterable required :datas="warehousesList" keyName="id" titleName="name" :deletable="false" v-model="form.inWarehouseId" placeholder="请选择调出仓库"/>
          <label class="ml-10px" style="font-size: 16px !important;">调入仓库：</label>
          <Select class="w-260px" filterable required :datas="warehousesList" keyName="id" titleName="name" :deletable="false" v-model="form.outWarehouseId" placeholder="请选择调入仓库"/>
        </template>
      </vxe-toolbar>
      <vxe-table
          size="mini"
          ref="xTable"
          border="border"
          :row-config="{height: 40}"
          show-footer
          :footer-method="footerMethod"
          stripe
          :data="productsData">
        <vxe-column title="序号" type="seq" width="60" align="center" fixed="left"/>
        <vxe-column title="操作" field="seq" width="70" align="center" fixed="left">
          <template #default="{row,rowIndex}">
            <div class="fa fa-plus text-hover mr-5px" @click="dac('plus',rowIndex)"></div>
            <div class="fa fa-minus text-hover" v-if="isMinus" @click="dac('minus',rowIndex)"></div>
          </template>
        </vxe-column>
        <vxe-column title="商品信息" width="300">
          <template #default="{row,rowIndex}">
            <div class="h-input-group goodsSelect" v-if="row.isNew" @keyup.stop="void(0)">
              <Select ref="ms" @change="doChange($event,rowIndex)" v-model="products" :datas="productsList" filterable placeholder="输入编码/名称" keyName="productsId" ><template v-slot:item="{ item }">
                <div>{{ item.productsCode }} {{ item.productsName }}</div>
              </template>
              </Select>
            </div>
            <div v-else class="flex">
              <div class="flex1 ml-8px">
                <div>{{ row.productsCode }}--{{ row.productsName }}</div>
              </div>
            </div>
          </template>
        </vxe-column>
        <vxe-column title="调拨单位" field="orderUnitName" align="center" width="120">
          <template #default="{row,rowIndex}">
            <template v-if="!row.isNew">
              <Select v-if="row.unitPrice" :deletable="false" @change="changOrderUnit($event,row)" v-model="row.orderUnitId" :datas="row.unitPrice" filterable placeholder="输入编码/名称" keyName="unitId" titleName="unitName"/>
              <span v-else>{{ row.orderUnitName }}</span>
            </template>
          </template>
        </vxe-column>
        <vxe-column title="调拨数量" field="orderQuantity" width="120">
          <template #default="{row,rowIndex,columnIndex}">
            <vxe-input v-if="!row.isNew" :id="'r'+rowIndex+''+3" @keyup="handleEnter($event,rowIndex,3)" @blur="updateQuantity(row)" ref="inputQuantity" v-model.number="row.orderQuantity" type="float" min="0" :controls="false"></vxe-input>
          </template>
        </vxe-column>
        <vxe-column title="基本单位" field="unitName" align="center" width="120"/>
        <vxe-column title="基本数量" field="sysQuantity" width="160"/>
<!--        <vxe-column title="出库单价" field="orderPrice" width="100">-->
<!--          <template #default="{row,rowIndex}">-->
<!--            <vxe-input v-if="!row.isNew" :id="'r'+rowIndex+''+4" @keyup="handleEnter($event,rowIndex,4)" @blur="updatePrice(row)" v-model.number="row.orderPrice" type="float" min="0" :controls="false"></vxe-input>-->
<!--          </template>-->
<!--        </vxe-column>-->
<!--        <vxe-column title="折扣率(%)" field="discount" width="100">-->
<!--          <template #default="{row,rowIndex}">-->
<!--            <vxe-input v-if="!row.isNew" :id="'r'+rowIndex+''+5" @keyup="handleEnter($event,rowIndex,5)" @blur="updateDiscount(row)" v-model.number="row.discount" type="float" min="0" :controls="false"></vxe-input>-->
<!--          </template>-->
<!--        </vxe-column>-->
<!--        <vxe-column title="折扣额" field="discountAmount" width="100">-->
<!--          <template #default="{row,rowIndex}">-->
<!--            <vxe-input v-if="!row.isNew" :id="'r'+rowIndex+''+6" @keyup="handleEnter($event,rowIndex,6)" @blur="updateDiscountAmount(row)" v-model.number="row.discountAmount" type="float" min="0" :controls="false"></vxe-input>-->
<!--          </template>-->
<!--        </vxe-column>-->
<!--        <vxe-column title="购货金额" field="discountedAmount" width="100">-->
<!--          <template #default="{row,rowIndex}">-->
<!--            <vxe-input v-if="!row.isNew" :id="'r'+rowIndex+''+7" @keyup="handleEnter($event,rowIndex,7)" @blur="updateDiscountedAmount(row)" v-model.number="row.discountedAmount" type="float" min="0" :controls="false"></vxe-input>-->
<!--          </template>-->
<!--        </vxe-column>-->
        <vxe-column title="备注" field="remark">
          <template #default="{row,rowIndex}">
            <vxe-input v-if="!row.isNew" :id="'r'+rowIndex+''+8" @keyup="handleEnter($event,rowIndex,8)" v-model="row.remark" placeholder="输入备注" :controls="false"></vxe-input>
          </template>
        </vxe-column>
      </vxe-table>
      <div class="mt-10px"></div>
      <div class="filler-panel">
        <div class="filler-item" style="flex: 1;margin: 5px 0 !important;">
          <label class="mr-16px  w-80px">备注说明：</label>
          <Input placeholder="请输入备注" maxlength="150" style="width: 90%" v-model="form.remark"/>
        </div>
      </div>
    </div>
    <div class="modal-column-between bg-white-color  border">
      <div>
        <!--      <Button icon="fa fa-close" @click="closePage" :loading="loading">-->
        <!--        取消-->
        <!--      </Button>-->
      </div>
      <Button icon="fa fa-save" color="primary" @click="confirm" :loading="loading">
        保存
      </Button>
    </div>
  </div>
</template>
<script>

import {confirm, loading, message} from "heyui.ext";
import manba from "manba";
import {CopyObj} from "@common/utils";
import Warehouses from "@js/api/Warehouses";
import Products from "@js/api/Products";
import StockTransfer from "@js/api/StockTransfer";

export default {
  name: "StockTransferForm",
  props: {
    orderId: [String, Number],
    type: String,
  },
  computed: {
    discountedAmount() {
      let total = 0;
      this.productsData.forEach(val => {
        if (val.sysQuantity > 0) {
          total += parseFloat(val.discountedAmount);
        }
      });
      return total.toFixed(2);
    },
    isMinus() {
      return this.productsData.length > 1;
    }
  },
  data() {
    return {
      loading: false,
      productsList: [],
      products: null,
      warehousesList: [],
      form: {
        id: null,
        billDate: manba().format("YYYY-MM-dd"),
        OrderType:"调拨单",
        outWarehouseId: null,
        inWarehouseId: null,
        remark: null,
      },
      productsData: [],
    }
  },
  methods: {
    handleEnter(e, index, num) {
      e.$event.stopPropagation();
      if (e.$event.keyCode === 13) {
        //回车
        e.$input.blur()
        if (num === 8) {
          if (index >= this.productsData.length - 2) {
            this.$refs.ms.$el.querySelector('input').click()
            this.$refs.ms.$el.querySelector('input').select()
          } else {
            let str = 'r' + (index + 1) + '' + 3
            document.getElementById(str).querySelector('input').focus()
            document.getElementById(str).querySelector('input').select()
          }
        } else {
          let str = 'r' + index + '' + (num + 1)
          document.getElementById(str).querySelector('input').focus()
          document.getElementById(str).querySelector('input').select()
        }
      } else if (e.$event.keyCode === 38) {
        //按上
        if (index > 0) {
          e.$input.blur()
          let str = 'r' + (index - 1) + '' + num
          document.getElementById(str).querySelector('input').focus()
          document.getElementById(str).querySelector('input').select()
        }
      } else if (e.$event.keyCode === 40) {
        //按下
        e.$input.blur()
        if (index < this.productsData.length - 2) {
          let str = 'r' + (index + 1) + '' + num
          document.getElementById(str).querySelector('input').focus()
          document.getElementById(str).querySelector('input').select()
        } else {
          this.$refs.ms.$el.querySelector('input').click()
          this.$refs.ms.$el.querySelector('input').select()
        }
      }
    }
    ,
    footerMethod({columns, data}) {
      let sums = [];
      columns.forEach((column) => {
        if (column.property && ['orderQuantity','sysQuantity'].includes(column.property)) {
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
    }
    ,
    //选择商品
    doChange(d, index) {
      if (d) {
        let g = {sysQuantity: 1, orderQuantity: 1, orderPrice:  0,warehouseId:0, price:  0, discountAmount: 0.00, discount: 0.00, discountedAmount:  0, num: 1, orderUnitId: d.unitId, orderUnitName: d.unitName, remark: ""};
        this.productsData[index] = Object.assign(Object.assign(g, d), d);
        if (!this.productsData[index + 1]) {
          this.productsData.push({isNew: true});
        }
        this.$refs.xTable.loadData(this.productsData).then(() => {
          this.$nextTick(() => {
            let str = index + '' + 3
            let element = document.querySelector('#r' + str + ' input');
            setTimeout(() => {
              element.focus()
              element.select()
            }, 100);
          })
        });
      }
      this.products = null;
    }
    ,
    //保存订单
    confirm() {
      loading("保存中....");
      let productsData = this.productsData.filter(c => c.sysQuantity > 0);
      if (this.form.outWarehouseId === null) {
        message.error("请选择调出仓库~");
        loading.close()
        return
      }
      if (this.form.inWarehouseId === null) {
        message.error("请选择调入仓库~");
        loading.close()
        return
      }
      StockTransfer.save({order: Object.assign(this.form, {discountedAmount: this.discountedAmount}), type: this.type, detailList: productsData}).then(() => {
        message("保存成功~");
      }).finally(() =>
              this.clear(),
          loading.close());
    },
    clear() {
      this.form = {
        id: null,
        billDate: manba().format("YYYY-MM-dd"),
        vendorsId: null,
        remark: null,
        discountedAmount: null
      }
      this.productsData = [{isNew: true}]
      this.vendorsId = null
    }
    ,
    //+-
    dac(type, index) {
      if (type === 'plus') {
        this.productsData.splice(index + 1, 0, {isNew: true});
      } else {
        this.productsData.splice(index, 1);
      }
    }
    ,
    loadProducts() {
      Products.loadToOrder().then(({data}) => {
        this.productsList = data || [];
        if (!this.form.id) {
          this.productsData = [{isNew: true}];
        }
      }).finally(() =>
          this.$refs.xTable.loadData(this.productsData).then(() => {
            this.$nextTick(() => {
              this.$refs.ms.$el.querySelector('input').click()
              this.$refs.ms.$el.querySelector('input').select()
            })
            this.products = null;
          })
      );
    },
    changOrderUnit(item, row) {
      row.orderUnitName = item.unitName
      row.orderPrice = (item.price || 0).toFixed(2) || 0
      row.num = item.num || 1;
      row.sysQuantity = (row.orderQuantity * row.num).toFixed(2);
      row.discountedAmount = (row.orderQuantity * row.orderPrice).toFixed(2);
    },
    //计算公式
    updateQuantity(item) {
      item.orderQuantity = item.orderQuantity || 1;
      item.discountedAmount = ((item.orderQuantity * item.orderPrice * (100 - item.discount)) / 100).toFixed(2);
      item.discountAmount = (((item.orderQuantity * item.orderPrice) * item.discount) / 100).toFixed(2);
      item.sysQuantity = (item.orderQuantity * (item.num || 1)).toFixed(2);
      this.$refs.xTable.updateFooter();
    },
    updatePrice(item) {
      item.orderPrice = item.orderPrice || 0.00
      item.discountAmount = (item.orderPrice * item.orderQuantity * item.discount / 100).toFixed(2);
      item.discountedAmount = (item.orderPrice * item.orderQuantity - item.discountAmount).toFixed(2);
      this.$refs.xTable.updateFooter();
    }
    ,
    updateDiscount(item) {
      item.discount = item.discount || 0.00;
      item.discountedAmount = ((item.orderQuantity || 0) * item.orderPrice * (100 - item.discount || 0) / 100).toFixed(2);
      item.discountAmount = ((item.orderQuantity || 0) * item.orderPrice - item.discountedAmount).toFixed(2);
      this.$refs.xTable.updateFooter();
    }
    ,

    updateDiscountAmount(item) {
      item.discountAmount = item.discountAmount || 0.00;
      item.discount = (((item.discountAmount / (item.orderPrice * item.orderQuantity)) * 100) || 0).toFixed(2);
      item.discountedAmount = (item.orderPrice * item.orderQuantity - item.discountAmount).toFixed(2);
      this.$refs.xTable.updateFooter();
    }
    ,
    updateDiscountedAmount(item) {
      item.discountedAmount = item.discountedAmount || 0
      item.orderPrice = ((item.discountedAmount) / ((100 - item.discount)) * 100 / item.orderQuantity).toFixed(2);
      item.discoutPrice = (item.orderPrice - item.discountedAmount).toFixed(2);
      this.$refs.xTable.updateFooter();
    }
    ,
    closePage() {
      let cache = localStorage.getItem("SYS_TABS");
      let tagList = cache ? JSON.parse(cache) : [];
      if (tagList) {
        let index = tagList.findIndex(val => val.name === "NewPurchaserOrder")
        tagList.splice(index, 1);
        let newRoute;
        if (tagList.length > 0) {
          newRoute = tagList[index - 1];
        } else {
          this.$router.push({name: 'DashboardMain'});
        }
        if (newRoute) this.$router.replace(newRoute);
        localStorage.setItem("SYS_TABS", JSON.stringify(newRoute))
      }
    }
  },
  beforeDestroy() {
    confirm({
      title: "系统提示",
      content: `确认?`,
      onConfirm: () => {

      }
    })
  },
  created() {
    loading("加载中....");
    Promise.all([
      Warehouses.select(),
    ]).then((results) => {
      this.warehousesList = results[0].data || []
      //订单详情/编辑订单
      if (this.orderId) {
        StockTransfer.load(this.orderId).then(({data: {order, productsData}}) => {
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
    this.loadProducts()
  },
}
</script>
