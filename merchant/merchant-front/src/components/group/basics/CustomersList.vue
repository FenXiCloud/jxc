<template>
  <div class="frame-page" style="margin: 0">
    <div class="h-panel">
      <div class="h-panel-body">
        <div class="table-toolbar">
          <div class="table-toolbar-left">
            <div class="h-input-group">
              <Input id="name" v-model="params.name" class="flex-1" placeholder="请输入用户名"/>
              <span class="h-input-addon" @click="doSearch" :loading="loading"><i class="h-icon-search"></i></span>
            </div>
          </div>
          <div class="table-toolbar-right">
            <Button @click="showForm()" color="primary">新 增</Button>
            <Button @click="addCategoryForm()" color="primary">新增分类</Button>
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
          <vxe-column title="编码" field="code" width="120"/>
          <vxe-column title="客户名称" field="name" min-width="200"/>
          <vxe-column title="联系人" field="linkman" width="120"/>
          <vxe-column title="电话" field="phone" width="120"/>
          <vxe-column title="分类" field="categoryName" width="120"/>
          <vxe-column title="等级" field="levelName" width="120"/>
          <vxe-column title="备注" field="remark" min-width="120"/>
          <vxe-column title="操作" align="center" width="160">
            <template #default="{row}">
                <i class="primary-color h-icon-edit ml-10px" @click="showForm(row)"></i>
                <i class="primary-color h-icon-trash ml-10px" @click="doRemove(row)"></i>
            </template>
          </vxe-column>
        </vxe-table>
      </div>
    </div>
  </div>
</template>

<script>
import CustomersForm from "./CustomersForm.vue";
import Customers from "@js/api/Customers";
import {confirm, message} from "heyui.ext";
import {layer} from "@layui/layer-vue";
import {h} from "vue";
import CustomersCategoryForm from "@components/group/basics/CustomersCategoryForm.vue";

/**
 * @功能描述: 客户管理
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
export default {
  name: "CustomersList",
  components: {CustomersForm},
  data() {
    return {
      loading: false,
      params: {
        name: null,
      },
      dataList: [],
      pagination: {
        page: 1,
        size: 20,
        total: 0
      },
    }
  },
  computed: {
    queryParams() {
      return Object.assign(this.params, {})
    }
  },
  methods: {
    addCategoryForm(entity) {
      let type = 0;
      let layerId = layer.open({
        title: "客户分类",
        shadeClose: false,
        closeBtn: false,
        area: ['400px', '230px'],
        content: h(CustomersCategoryForm, {
          entity, type,
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
    showForm(entity) {
      let layerId = layer.open({
        title: "客户信息",
        shadeClose: false,
        closeBtn: false,
        area: ['700px', '500px'],
        content: h(CustomersForm, {
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
    loadList() {
      this.loading = true;
      Customers.list(this.queryParams).then(({data}) => {
        this.dataList = data.results;
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
        content: `确认删除用户：${row.name}?`,
        onConfirm: () => {
          Customers.remove(row.id).then(() => {
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
