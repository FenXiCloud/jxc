<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <Form ref="form" :model="model" :rules="validationRules" :labelWidth="160" >
        <FormItem label="进销存组织">
          <Input   v-model="model.name" disabled="true"/>
        </FormItem>
        <FormItem label="是否关联财务系统" required prop="name">
          <Radio v-model="model.isRelation" dict="relationRadios"/>
        </FormItem>
        <FormItem label="关联财务系统帐套" required prop="accountSetsId">
          <Select :datas="accountSetsList" keyName="accountSetsId" v-model="model.accountSetsId"  filterable titleName="companyName" placeholder="关联财务系统帐套"/>
        </FormItem>
      </Form>
    </div>
    <div class="modal-column-right">
      <Button icon="fa fa-save" style="justify-content: right" color="primary" @click="confirm" :loading="loading">
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
import Relation from "@js/api/Relation";

export default {
  name: "RelationForm",
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
      accountSetsList: [],
      model: {
        id: null,
        isRelation:false,
        accountSetsId: null,
        name: '组织名称',
      },
      validationRules: {
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
    toInit(){
      Relation.init().then(({data})=>{
        this.accountSetsList = data||[]
      })
    }
  },
  created() {
    this.toInit()
  }
}
</script>
