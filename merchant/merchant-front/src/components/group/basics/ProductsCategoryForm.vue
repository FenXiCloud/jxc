<template>
  <div class="modal-column">
    <div class="modal-column-full-body">
      <Form ref="form" :model="model" :rules="validationRules">
        <FormItem label="编码" required prop="code">
          <Input placeholder="请输入分类编码" :disabled="model.id" v-model="model.code"/>
        </FormItem>
        <FormItem label="名称" required prop="name">
          <Input placeholder="请输入分类名称" v-model="model.name"/>
        </FormItem>
        <FormItem label="排序" required prop="sort">
          <Input placeholder="请输入排序" v-model="model.sort"/>
        </FormItem>
        <FormItem label="商品图片" prop="imgPath" single>
          <div class="h-uploader-image-empty h-uploader-browse-button" @click="showCropperImage">
            <div class="h-uploader-image" v-if="model.imgPath">
              <img :src="model.imgPath" v-if="model.imgPath" style="height: 70px;width: 70px"/>
            </div>
            <i class="h-icon-plus" v-else style="font-size: 25px; position: absolute;  top: 50%;  left: 50%; transform: translate(-50%,-50%);"></i>
          </div>
        </FormItem>
        <!--        <FormItem label="上级分类" prop="parent">-->
        <!--          <CategoryPicker :option="categoryOption" type="key" v-model="model.parentId"></CategoryPicker>-->
        <!--        </FormItem>-->
      </Form>
    </div>
    <div class="modal-column-between">
      <Button @click="$emit('close')" :loading="loading">
        取消
      </Button>
      <Button color="green" @click="confirm" :loading="loading">
        保存
      </Button>
    </div>
  </div>
</template>

<script>
/**
 * @功能描述: 组织机构Form
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import ProductsCategory from "@js/api/ProductsCategory";
import {message} from "heyui.ext";
import {CopyObj} from "@common/utils";
import {layer} from "@layui/layer-vue";
import {h} from "vue";

export default {
  name: "ProductsCategoryForm",
  emits: {
    close: null,
    success: null
  },
  props: {
    productsCategory: Object,
    parent: Object,
    list: Array,
  },
  data() {
    return {
      loading: false,
      categoryOption: {
        keyName: 'id',
        titleName: 'name',
        dataMode: 'list',
        parentName: 'parentId',
        datas: []
      },
      model: {
        id: null,
        code: null,
        parentId: null,
        name: null,
        imgPath: null,
        path: null,
        sort: 1,
      },
      validationRules: {}
    }
  },
  methods: {
    confirm() {
      let validResult  = this.$refs.form.valid();
      if (validResult.result) {

        if (this.model.parentId) {
          let parent = this.list.find(c => c.id === this.model.parentId);
          this.model.path = (parent.path || '') + '/' + parent.id;
        } else {
          this.model.path = null;
        }
        if (this.model.id) {
          if (this.model.id === this.model.parentId) {
            message.error("不能引用当前分类")
            return
          }
        }
        this.loading = true;
        ProductsCategory.save(this.model).then(() => {
          message("保存成功~");
          this.$emit('success');
        }).finally(() => this.loading = false);
      }
    },
    showCropperImage() {
      let layerId = layer.open({
        title: "添加商品图片",
        closeBtn: 1,
        area: ['1000px', 'auto'],
        content: h(CropperImage, {
          imgPath: this.model.imgPath,
          onClose: () => {
            layer.close(layerId);
          },
          onSuccess: (path) => {
            this.model.imgPath = path;
            layer.close(layerId);
          }
        })
      });
    },
  },
  created() {
    this.categoryOption.datas = this.list || [];
    CopyObj(this.model, this.productsCategory);
    if (this.parent) {
      this.model.parentId = this.parent.id;
    }
  }
}
</script>
