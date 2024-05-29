<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <Form ref="form" :model="model" :rules="validationRules" :labelWidth="160" >
        <FormItem label="进销存组织">
          <Input   v-model="model.organizationName" disabled="true"/>
        </FormItem>
        <FormItem label="是否关联财务系统" prop="name">
          <Radio v-model="model.isRelation" dict="relationRadios"/>
        </FormItem>
        <FormItem label="关联财务系统帐套" prop="accountSetsId" v-if="model.isRelation">
          <Select :datas="accountSetsList" keyName="accountSetsId" v-model="model.accountSetsId"  filterable titleName="companyName" placeholder="关联财务系统帐套" @change="changeSets($event)"/>
        </FormItem>
      </Form>
    </div>
    <div class="modal-column-right">
      <Button icon="fa fa-close" @click="$emit('close')" :loading="loading">
        取消
      </Button>
      <Button icon="fa fa-save"  color="primary" @click="confirm" :loading="loading">
        保存
      </Button>
    </div>
  </div>
</template>

<script>

import {message} from "heyui.ext";
import {CopyObj} from "@common/utils";
import manba from "manba";
import Relation from "@js/api/RelationCw";

export default {
  name: "RelationForm",
  emits: {
    close: null,
    success: null
  },
  props: {
    cwRelation: Object,
  },
  data() {
    return {
      loading: false,
      accountSetsList: [],
      model: {
        id: null,
        isRelation:false,
        accountSetsId: null,
        companyName: null,
        organizationName: '',
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
        Relation.save(this.model).then(() => {
          message("保存成功~");
          this.$emit('success');
        }).finally(() => this.loading = false);
      }
    },
    changeSets(item){
      this.model.companyName = item.companyName
    },
    toInit(){
    }
  },
  created() {
    CopyObj(this.model, this.cwRelation);
    Relation.loadAccountSets().then(({data})=>{
      this.accountSetsList = data||[]
    })
  }
}
</script>
