<template>
  <div class="frame-page flex flex-column">
    <vxe-toolbar>
      <template #buttons>
        <Search
            v-model.trim="params.filter"
            search-button-theme="h-btn-default"
            show-search-button
            class="w-360px"
            placeholder="请输入名称/编码/联系人/电话"
            @search="doSearch">
          <i class="h-icon-search"/>
        </Search>
      </template>
      <template #tools>
        <Button @click="showForm()" color="green">添加</Button>
      </template>
    </vxe-toolbar>
    <div class="flex-1">
      <vxe-table row-id="id"
                 ref="table"
                 height="auto"
                 :data="dataList"
                 highlight-hover-row
                 show-overflow
                 :row-config="{height: 48}"
                 :column-config="{resizable: true}"
                 :loading="loading">
        <vxe-column title="序号" type="seq" width="60" align="center" />
        <vxe-column title="分类" field="categoryName" width="100"/>
        <vxe-column title="编码" field="code" width="80"/>
        <vxe-column title="名称" field="name" min-width="150"/>
        <vxe-column title="登录账号" field="username" width="100"/>
        <vxe-column title="联系人" field="contacts" width="100"/>
        <vxe-column title="电话" field="phone" width="100"/>
        <vxe-column title="归属区域" field="areaName" width="100"/>
        <vxe-column title="地址" field="address" min-width="100"/>
        <vxe-column title="创建时间" field="createDate" width="140"/>
        <vxe-column title="状态" field="enabled" width="60">
          <template #default="{row}">
            <Tag color="green" v-if="row.enabled">启用</Tag>
            <Tag color="red" v-else>禁用</Tag>
          </template>
        </vxe-column>
        <vxe-column title="操作" align="center" width="150" fixed="right">
          <template #default="{row}">
            <span class="primary-color text-hover  ml-10px" @click="supplierGoods(row)">供货商品</span>
            <span class="primary-color h-icon-edit text-hover ml-10px" @click="showForm(row)"></span>
            <span class="primary-color h-icon-trash text-hover ml-10px" @click="doRemove(row)"></span>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
    <vxe-pager perfect @page-change="loadList"
               v-model:current-page="pagination.page"
               v-model:page-size="pagination.pageSize"
               :total="pagination.total"
               :layouts="['PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'Sizes', 'FullJump', 'Total']">
      <template #left>
        <vxe-button @click="loadList()" type="text" size="mini" icon="fa fa-refresh"
                    :loading="loading"></vxe-button>
      </template>
    </vxe-pager>
  </div>
</template>

<script>
import Vendors from "@js/api/Vendors";
import VendorsForm from "./VendorsForm.vue";
import {confirm, message} from "heyui.ext";
import {layer} from "@layui/layer-vue";
import {h} from "vue";

export default {
  name: "VendorsList",
  data() {
    return {
      loading: false,
      dataList: [],
      params: {
        filter: null,
      },
      pagination: {
        page: 1,
        pageSize: 20,
        total: 0
      },
    }
  },
  computed: {
    queryParams() {
      return Object.assign(this.params, {
        page: this.pagination.page,
        pageSize: this.pagination.pageSize,
      })
    }
  },
  methods: {
    showForm(entity) {
      let layerId = layer.open({
        title: "供货商信息",
        shadeClose: false,
        area: ['800px', 'auto'],
        content: h(VendorsForm, {
          entity,
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
    doSearch() {
      this.loadList();
    },
    loadList() {
      this.loading = true;
      Vendors.list(this.queryParams).then(({data: {results, total}}) => {
        this.dataList = results;
        this.pagination.total = total;
      }).finally(() => this.loading = false);
    },
    doRemove(row) {
      confirm({
        title: "系统提示",
        content: `确认删除单位：${row.name}?`,
        onConfirm: () => {
          Vendors.remove(row.id).then(() => {
            message("删除成功~");
            this.loadList();
          })
        }
      })
    }
  },
  created() {
    this.loadList();
  }
}
</script>
