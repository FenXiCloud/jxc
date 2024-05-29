<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <vxe-table
          ref="xTable"
          border="border"
          :row-config="{height: 40}"
          :edit-config="{trigger: 'click', mode: 'row'}"
          stripe
          :data="relationList">
        <vxe-column title="名称" field="name" align="center" width="260"/>
        <vxe-column title="对应科目" field="title" align="center" width="260" :edit-render="{}">
          <template #edit="{row,rowIndex}">
            <vxe-select v-model="row.title" filterable @change="changeSub($event,row)">
              <vxe-option v-for="item in subjectList" :key="item.subjectId" :value="item.subjectId" :label="item.subjectName"> </vxe-option>
            </vxe-select>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <div class="modal-column-right">
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

import {message} from "heyui.ext";
import {CopyObj} from "@common/utils";
import manba from "manba";
import Relation from "@js/api/RelationCw";
import RelationSubject from "@js/api/RelationSubject";

export default {
  name: "RelationSubjectForm",
  emits: {
    close: null,
    success: null
  },
  props: {
    relationCwId: Number,
  },
  data() {
    return {
      loading: false,
      subjectList: [],
      relationList: [],
      model: {
        id: null,
        isRelation: false,
        accountSetsId: null,
        companyName: null,
        organizationName: '',
      },
      validationRules: {}
    }
  },
  methods: {
    confirm() {
      this.loading = true;
      this.model.startDate = manba(this.model.startDate).format("YYYY-MM")
      RelationSubject.save(this.relationList).then(() => {
        message("保存成功~");
        this.$emit('success');
      }).finally(() => this.loading = false);
    },
    changeSub(event, row) {
      const subject = this.subjectList.find(val=> val.subjectId === event.value)
      row.subjectId = subject.subjectId
      row.code = subject.subjectCode
      row.title = subject.subjectName
      console.log(row)
    }
  },
  created() {
    Promise.all([
      RelationSubject.loadSubject(),
      RelationSubject.load(this.relationCwId),
    ]).then((results) => {
      this.subjectList = results[0].data || [];
      this.relationList = results[1].data || [];
    })
  }
}
</script>
