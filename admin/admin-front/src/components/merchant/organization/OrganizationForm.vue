<template>
  <div class="m-16px">
    <Form ref="form" :model="model" :rules="validationRules" :labelWidth="120" mode="twocolumn">
      <FormItem label="名称" required prop="name">
        <Input placeholder="请输入门店名称" v-model="model.name"/>
      </FormItem>
      <FormItem label="编码" prop="code">
        <Input :readonly="model.id" placeholder="门店编码(不填系统自动生成)" v-model="model.code"/>
      </FormItem>
      <FormItem label="统一信用代码" required prop="registrationNumber">
        <Input placeholder="请输入统一信用代码" v-model="model.registrationNumber"/>
      </FormItem>
      <FormItem label="法人" prop="legalPerson">
        <Input placeholder="请输入法人"  v-model="model.legalPerson"/>
      </FormItem>
      <FormItem label="联系人姓名" required prop="linkman">
        <Input placeholder="请输入门店联系人姓名" v-model="model.linkman"/>
      </FormItem>
      <FormItem label="联系人电话" required prop="phone">
        <Input placeholder="请输入门店联系人手机号" v-model="model.phone"/>
      </FormItem>
      <FormItem label="类型" prop="orgType">
        <Radio v-model="model.orgType" :datas="{0:'公司',1:'个体户'}"></Radio>
      </FormItem>
      <FormItem label="邮箱" prop="email">
        <Input placeholder="请输入邮箱" v-model="model.email"/>
      </FormItem>
      <FormItem label="区域" prop="area" >
        <Input placeholder="请输入区域" v-model="model.area"/>
      </FormItem>
      <FormItem label="地址" prop="address" single>
        <Input placeholder="请输入地址" v-model="model.address"/>
      </FormItem>
    </Form>
  </div>
  <div class="layui-layer-btn layui-layer-btn-r">
    <Button icon="fa fa-close" @click="$emit('close')" :loading="loading">
      取消
    </Button>
    <Button icon="fa fa-save" color="primary" @click="confirm" :loading="loading">
      保存
    </Button>
  </div>
</template>

<script>

import Organization from "@js/api/Organization";
import {message} from "heyui.ext";
import {CopyObj} from "@common/utils";

export default {
  name: "OrganizationForm",
  emits: {
    close: null,
    success: null
  },
  props: {
    organization: Object,
    merchantId: [String, Number],
  },
  data() {
    return {
      loading: false,
      merchantList: [],
      model: {
        id: null,
        address: null,
        code: null,
        merchantId: null,
        legalPerson: null,
        registrationNumber: null,
        orgType: 0,
        email: null,
        linkman: null,
        name: null,
        phone: null,
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
        Organization.save(this.model).then(() => {
          message("保存成功~");
          this.$emit('success');
        }).finally(() => this.loading = false);
      }
    },
  },
  created() {
    CopyObj(this.model, this.organization);
    this.model.merchantId = this.merchantId;
  }
}
</script>
