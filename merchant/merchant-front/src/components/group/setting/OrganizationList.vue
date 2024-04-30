<template>
  <div class="frame-page">
    <div class="h-panel">
      <div class="h-panel-body">
        <Row :space-x="10">
          <Cell width="6" class="flex items-center">
            <label for="name" class="mr-10px">名称</label>
            <Input id="name" v-model="params.name" class="flex-1" placeholder="请输入名称"/>
          </Cell>
          <Cell width="6">
            <Button color="primary" icon="fa fa-search" :loading="loading" @click="doSearch">查询</Button>
          </Cell>
        </Row>
      </div>
    </div>
    <div class="h-panel mt-10px">
      <div class="h-panel-body">
        <div class="table-toolbar">
          <div class="table-toolbar-left">
            <Button @click="showForm()" color="primary" icon="fa fa-plus">添加</Button>
          </div>
          <div class="table-toolbar-right">
            <i @click="loadList" class="table-toolbar-right-icon fa fa-refresh"></i>
          </div>
        </div>
        <vxe-table row-id="id"
                   ref="table"
                   :data="dataList"
                   highlight-hover-row
                   show-overflow
                   :loading="loading">
          <vxe-column type="seq" width="60" align="center"/>
          <vxe-column title="编码" field="code" width="100"/>
          <vxe-column title="名称" field="name" width="150"/>
          <vxe-column title="联系人" field="linkman" width="100"/>
          <vxe-column title="联系人电话" field="phone" width="100"/>
          <vxe-column title="联系人邮箱" field="email" width="100"/>
          <vxe-column title="启用日期" field="startDate" width="120" formatter="formatMonth"/>
          <vxe-column title="地址" field="address"/>
          <vxe-column title="创建时间" field="createDate" width="140"/>
          <vxe-column title="状态" field="enabled" width="80" align="center" fixed="right">
            <template #default="{row}">
              <Tag color="green" v-if="row.enabled" @click="trigger(row)" class="cursor-pointer">启用</Tag>
              <Tag color="red" v-else @click="trigger(row)" class="cursor-pointer">禁用</Tag>
            </template>
          </vxe-column>
          <vxe-column title="操作" align="center" width="120" fixed="right">
            <template #default="{row}">
              <div class="flex items-center justify-center">
                <span class=" primary-color text-hover ml-10px" @click="showForm(row)" size="s">编辑</span>
              </div>
            </template>
          </vxe-column>
        </vxe-table>
        <Pagination align="right" class="mt-16px" v-model="pagination" @change="pageChange" small/>
      </div>
    </div>
  </div>
</template>

<script>
import Organization from "@js/api/Organization";
import {confirm, message} from "heyui";
import OrganizationForm from "./OrganizationForm.vue";
import {layer} from "@layui/layer-vue";
import {h} from "vue";


export default {
  name: "OrganizationList",
  props: {
    merchant: Object,
  },
  components: {OrganizationForm},
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
      areaList: [],
      merchantList: [],
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
    showForm(organization) {
      let layerId = layer.open({
        title: "组织信息",
        shadeClose: false,
        closeBtn: false,
        area: ['50vw', 'auto'],
        content: h(OrganizationForm, {
          organization,
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
    pageChange() {
      this.loadList();
    },
    doSearch() {
      this.pagination.page = 1;
      this.loadList();
    },
    doRemove(row) {
      confirm({
        title: "系统提示",
        content: `确认删除门店：${row.name}?`,
        onConfirm: () => {
          Organization.remove(row.id).then(() => {
            message("删除成功~");
            this.doSearch();
          })
        }
      })
    },
    trigger(row) {
      let enabled = !row.enabled;
      confirm({
        title: "系统提示",
        content: `确认要「${enabled ? "启用" : "禁用"}」门店：${row.name}?`,
        onConfirm: () => {
          Organization.save({id: row.id, enabled}).then(() => {
            message("操作成功~");
            this.loadList();
          })
        }
      })
    }
  },
  created() {
    this.doSearch();
  }
}
</script>
