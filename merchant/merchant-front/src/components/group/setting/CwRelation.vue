<template>
  <div class="frame-page flex flex-column">
    <div class="flex1">
      <vxe-table row-id="id"
                 ref="table"
                 :data="dataList"
                 highlight-hover-row
                 show-overflow
                 :loading="loading">
        <vxe-column title="关联状态" width="100">
          <template #default="row">
            <div>已关联</div>
          </template>
        </vxe-column>
        <vxe-column title="进销存组织编码" field="code" width="150"/>
        <vxe-column title="进销存组织名称" field="name" min-width="150"/>
        <vxe-column title="关联财务软件帐套" field="name" min-width="150"/>
        <vxe-column title="操作" align="center" width="120" fixed="right">
          <template #default="{row}">
            <div class="flex items-center justify-center">
              <span class=" primary-color text-hover ml-10px" @click="showForm(row)" size="s">编辑</span>
            </div>
          </template>
        </vxe-column>
      </vxe-table>
      <div class="mt-10px">
        <div class="mb-10px" style="font-weight: bold">操作指引:</div>
        <div class="flex w-900px" style="text-align: center;margin: 0 auto">
          <div class="w-150px p-10px bg-gray4-color br" @click="showForm">
            <div class="pt-20px">
              <Icon type="h-icon-setting" :size="40"/>
            </div>
            <div class="p-10px" style="font-weight: bold">关联进销存帐套</div>
            <div class="pb-20px" style="text-align: left;color: gray;font-size: small">
              只有同时拥有财务软件和进销存的账套管理员才有权限设置关联
            </div>
          </div>
          <div class="m-10px icon-center">
            <Icon type="h-icon-right" :size="50" color="gainsboro"/>
          </div>
          <div class="w-150px p-10px bg-gray4-color br">
            <div class="pt-20px">
              <Icon type="h-icon-setting" :size="40"/>
            </div>
            <div class="p-10px" style="font-weight: bold">设置进销存参数</div>
            <div class="pb-20px" style="text-align: left;color: gray;font-size: small">检查确定进销存核算参数的设置
            </div>
          </div>
          <div class="m-10px icon-center ">
            <Icon type="h-icon-right" :size="50" color="gainsboro"/>
          </div>
          <div class="w-150px p-10px bg-gray4-color br">
            <div class="pt-20px">
              <Icon type="h-icon-setting" :size="40"/>
            </div>
            <div class="p-10px" style="font-weight: bold">完善进销存资料</div>
            <div class="pb-20px" style="text-align: left;color: gray;font-size: small">
              设置进销存基础资料与财务软件账套的会计科目之间的对应关系
            </div>
          </div>
          <div class="m-10px icon-center">
            <Icon type="h-icon-right" :size="50" color="gainsboro"/>
          </div>
          <div class="w-150px p-10px bg-gray4-color br">
            <div class="pt-20px">
              <Icon type="h-icon-setting" :size="40"/>
            </div>
            <div class="p-10px" style="font-weight: bold">自动生成凭证</div>
            <div class="pb-20px" style="text-align: left;color: gray;font-size: small">选择进销存单据生成凭证</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Organization from "@js/api/Organization";
import {confirm, message} from "heyui.ext";
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import RelationForm from "@components/group/setting/RelationForm.vue";
import Relation from "@js/api/Relation";


export default {
  name: "CwRelation",
  props: {
    merchant: Object,
  },
  data() {
    return {
      opened: true,
      loading: false,
      params: {
        areaId: null,
        name: null,
        merchantId: null,
      },
      checkedRows: [],
      dataList: [],
      pagination: {
        page: 1,
        size: 20,
        total: 0
      },
      param: [
        {title: '启用', key: 'enabled'},
        {title: '禁用', key: 'disabled'},
      ]

    }
  },
  computed: {
    queryParams() {
      return Object.assign(this.params, {
        page: this.pagination.page,
        pageSize: this.pagination.size
      })
    }
  },
  methods: {
    showForm() {
      let layerId = layer.open({
        title: "关联财务系统",
        shadeClose: false,
        area: ['50vw', 'auto'],
        content: h(RelationForm, {
          onClose: () => {
            layer.close(layerId);
          },
          onSuccess: () => {
            this.doSearch();
            layer.close(layerId);
          }
        })
      });
    },
    loadList() {
      this.loading = true;
      Organization.list(this.queryParams).then(({data}) => {
        this.dataList = data.results;
        this.pagination.total = data.total;
      }).finally(() => this.loading = false);
    },
  },
  created() {
    this.loadList();
  }
}
</script>
<style>
.icon-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.cw-content :hover {
  border: 1px solid #3d74ff;
}

.br {
  border-radius: 10px
}
</style>
