<template>
  <div class="modal-column">
    <div class="m-20px ml-50px">
      <div class="border-top pt-20px font-bold">成本参数核算方法</div>
      <Radio v-model="model.costMethod" :datas="param"/>
    </div>
    <!--    <div class="m-20px ml-50px">-->
    <!--      <div class="border-top  pt-20px">异常成本处理（当负库存出库、负结存出库等情形导致商品发出成本小于等于零时会采用本规则）</div>-->
    <!--    </div>-->
    <div class="flex justify-between py-5px px-5px bg-white-color">
      <div></div>
      <Button class="mr-50px" color="primary" @click="confirm" :loading="loading">
        保 存
      </Button>
    </div>
  </div>
</template>

<script>
/**
 * @功能描述: 系统参数
 * @创建时间: 2024年05月22日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Admin from "@js/api/Admin";
import {confirm, message} from "heyui.ext";
import ArgsSetting from "@js/api/ArgsSetting";
import SalesOrder from "@js/api/SalesOrder";

export default {
  name: "ArgsSetting",
  computed: {},
  props: {
    entity: Object,
  },
  data() {
    return {
      loading: false,
      param: [{key: '先', title: '先进先出法'}, {key: '平', title: '移动平均法'}],
      model: {
        costMethod:null
      }
    }
  },
  methods: {
    confirm() {
      confirm({
        title: "系统提示",
        content: `你正在修改系统参数`,
        onConfirm: () => {
          ArgsSetting.update(this.model.costMethod).then(({success}) => {
            if(success){
              message("操作成功~");
              window.location.replace("/");
            }
          })
        }
      })
    },
    loadArgsSetting() {
      ArgsSetting.load().then(({data}) => {
        this.model = data
      })
    }
  },
  created() {
    this.loadArgsSetting()
  }
}
</script>
