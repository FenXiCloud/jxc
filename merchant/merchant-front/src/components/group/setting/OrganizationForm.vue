<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <Form ref="form" :model="model" :rules="validationRules" :labelWidth="120" mode="twocolumn">
        <blockquote>基本信息</blockquote>
        <FormItem label="门店名称" required prop="name">
          <Input placeholder="请输入门店名称" v-model="model.name"/>
        </FormItem>
        <FormItem label="门店编码" prop="code">
          <Input :readonly="model.id" placeholder="门店编码(不填系统自动生成)" v-model="model.code"/>
        </FormItem>
        <FormItem label="级别" required prop="levelId">
          <Select :datas="levelList" keyName="id" filterable :deletable="false" titleName="name" placeholder="请选择门店级别"
                  v-model="model.levelId"/>
        </FormItem>
        <FormItem label="区域" required prop="areaId">
          <Select :datas="areaList" keyName="id" filterable :deletable="false" titleName="name" placeholder="请选择门店级别"
                  v-model="model.areaId"/>
        </FormItem>
        <FormItem label="默认仓库" required prop="warehouseId">
          <Select :datas="warehouseList" keyName="id" filterable :deletable="false" titleName="name" placeholder="请选择默认仓库"
                  v-model="model.warehouseId"/>
        </FormItem>
        <FormItem label="联系人姓名" required prop="linkman">
          <Input placeholder="请输入门店联系人姓名" v-model="model.linkman"/>
        </FormItem>
        <FormItem label="联系人电话" required prop="phone">
          <Input placeholder="请输入门店联系人电话" v-model="model.phone"/>
        </FormItem>
        <FormItem label="类型" prop="type">
          <Radio v-model="model.type" :datas="{0:'直营',1:'加盟'}"></Radio>
        </FormItem>
        <FormItem label="启用日期" required prop="startDate">
          <DatePicker v-model="model.startDate" format="YYYY-MM" type="month" :clearable="false"/>
        </FormItem>
        <FormItem label="地址" prop="address">
          <Input placeholder="请输入地址" v-model="model.address"/>
        </FormItem>
        <template v-if="!model.id">
          <blockquote>账号信息
            <h-switch class="ml-10px" v-model="model.openAccount" small></h-switch>
            <span class="red-color">开通订货账号 </span>（开通订货账号，客户才能进入易订货系统订货）
          </blockquote>
          <template v-if="model.openAccount">
            <FormItem label="账号" prop="username" required single>
              <Input placeholder="请输入账号" v-model="model.username"/>
            </FormItem>
            <FormItem label="密码" prop="password" required single>
              <Input placeholder="请输入密码" type="password" v-model="model.password"/>
            </FormItem>
          </template>
        </template>
      </Form>
    </div>
    <div class="modal-column-between">
      <Button icon="fa fa-close" @click="$emit('close')" :loading="loading">
        取消
      </Button>
      <Button icon="fa fa-save" color="primary" @click="confirm" :loading="loading">
        保存
      </Button>
    </div>
  </div>
</template>

<script>

import Organization from "@js/api/Organization";
import {message} from "heyui.ext";
import {CopyObj} from "@common/utils";
import manba from "manba";

export default {
  name: "OrganizationForm",
  emits: {
    close: null,
    success: null
  },
  props: {
    organization: Object,
  },
  data() {
    return {
      loading: false,
      merchantList: [],
      areaList: [],
      levelList: [],
      warehouseList: [],
      model: {
        id: null,
        address: null,
        code: null,
        merchantId: null,
        type: 0,
        email: null,
        linkman: null,
        name: null,
        phone: null,
        areaId: null,
        warehouseId: null,
        levelId: null,
        startDate: null,
        openAccount: true,
        username: null,
        password: null,
      },
      validationRules: {
        mobile: ['phone']
      }
    }
  },
  methods: {
    confirm() {
      let validResult = this.$refs.form.valid();
      if (validResult.result) {
        this.loading = true;
        this.model.startDate = manba(this.model.startDate).format("YYYY-MM")
        Organization.save(this.model).then(() => {
          message("保存成功~");
          this.$emit('success');
        }).finally(() => this.loading = false);
      }
    },
    init() {
      this.loading = true;
      Promise.all([
        Area.select(),
        Level.select(),
        Warehouse.select(),
      ]).then((results) => {
        this.areaList = results[0].data
        this.levelList = results[1].data
        this.warehouseList = results[2].data
      }).finally(() => this.loading = false);
    }
  },
  created() {
    this.init();
    CopyObj(this.model, this.organization);
  }
}
</script>
