<template>
  <div class="frame-page" style="margin: 0">
    <div class="h-panel">
      <div class="h-panel-body">
        <div class="table-toolbar">
          <div class="table-toolbar-left">
            <div class="h-input-group">
              <Input id="name" v-model="params.name" class="flex-1" placeholder="请输入角色名称"/>
              <span class="h-input-addon" @click="doSearch" :loading="loading"><i class="h-icon-search"></i></span>
            </div>
          </div>
          <div class="table-toolbar-right">
            <Button @click="showForm()" color="primary">新 增</Button>
          </div>
        </div>
        <vxe-table row-id="id"
                   ref="table"
                   :data="dataList"
                   highlight-hover-row
                   show-overflow
                   :row-config="{height: 48}"
                   :column-config="{resizable: true}"
                   :loading="loading">
          <vxe-column type="seq" width="40" title="#"/>
          <vxe-column title="名称" field="name"/>
          <vxe-column title="是否默认" field="systemDefault" width="100" align="center">
            <template #default="{row:{systemDefault}}">
              <Tag color="primary" v-if="systemDefault">是</Tag>
              <Tag color="gray" v-else>否</Tag>
            </template>
          </vxe-column>
          <vxe-column title="操作" align="center" width="300">
            <template #default="{row}">
              <div class="flex items-center justify-center" v-if="!row.systemDefault">
                <span class="primary-color text-hover" @click="showGrantMenu(row)">可用菜单</span>
                <i class="primary-color h-icon-edit ml-10px" @click="showForm(row)"></i>
                <i class="primary-color h-icon-trash ml-10px" @click="doRemove(row)"></i>
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
import Invoice from "@js/api/Invoice";
import {confirm, message} from "heyui";
import {layer} from "@layui/layer-vue";
import {h} from "vue";


export default {
  name: "RoleList",
  data() {
    return {
      loading: false,
      params: {
        type: 0
      },
      checkedRows: [],
      dataList: [],
      pagination: {
        page: 1,
        size: 20,
        total: 0
      }
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
    loadList() {
      this.loading = true;
      Invoice.list(this.queryParams).then(({data}) => {
        this.dataList = data.results;
        this.pagination.total = data.total;
      }).finally(() => this.loading = false);
    },
    pageChange() {
      this.loadList();
    },
    tableCheck() {
      this.checkedRows = this.$refs.table.getCheckboxRecords();
    },
    doSearch() {
      this.pagination.page = 1;
      this.loadList();
    },
    doRemove(row) {
      confirm({
        title: "系统提示",
        content: `确认删除角色：${row.name}?`,
        onConfirm: () => {
          Invoice.remove(row.id).then(() => {
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
