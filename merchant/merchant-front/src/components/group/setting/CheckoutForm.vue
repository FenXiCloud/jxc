<template>
  <div class="m-16px">
    <div style="height:calc(100vh - 340px);">
      <vxe-table
          ref="table"
          size="mini"
          row-id="id"
          border stripe
          height="auto"
          show-overflow :data="notData">
        <vxe-column title="类型" width="100" field="type" :formatter="({ cellValue })=>{
        return {0:'期初入库',1:'验收入库单',2:'验收退货单',3:'报损出库单',5:'部门调拨单',6:'销售扣减单',7:'外销单',8:'盘点单',9:'门店加工单',10:'门店调拨入库单',11:'门店调拨出库单'}[cellValue]}">
        </vxe-column>
        <vxe-column title="订单号" field="code" width="140"/>
        <vxe-column title="日期" field="billDate" width="90"/>
        <vxe-column title="金额" width="100" field="amount"/>
        <vxe-column title="备注" width="140" field="remark"/>
      </vxe-table>
    </div>
  </div>
  <div class="layui-layer-btn layui-layer-btn-r">
    <Button icon="fa fa-close" @click="$emit('close')" :loading="loading">
      关闭
    </Button>
  </div>
</template>

<script>


export default {
  name: "CheckoutForm",
  props: {
    notData: Array,
  },
  data() {
    return {}
  },
}
</script>

<style scoped>

</style>
