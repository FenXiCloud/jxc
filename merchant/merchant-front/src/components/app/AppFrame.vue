<template>
  <Layout class="app-frame" v-if="!loading" :siderCollapsed="siderCollapsed" siderFixed>
    <Sider theme="dark" style="overflow: unset;!important;">
      <AppMenu theme="dark"/>
    </Sider>
    <Layout headerFixed>
      <HHeader theme="white">
        <AppHead/>
      </HHeader>
      <SysTabs :homePage="currentTab"/>
      <Content>
        <div class="app-frame-content h-full p-10px pt-10px">
          <router-view  class="h-full bg-white-color"/>
        </div>
        <HFooter>
          <AppFooter/>
        </HFooter>
      </Content>
    </Layout>
  </Layout>
</template>

<script>
import AppHead from "@components/app/AppHead";
import AppMenu from "@components/app/AppMenu";
import AppFooter from "@components/app/AppFooter";
import SysTabs from "@components/common/sys-tabs";
import {mapMutations, mapState} from "vuex";

/**
 * @功能描述: FRAME
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
export default {
  name: "AppFrame",
  components: {SysTabs, AppFooter, AppMenu, AppHead},
  computed: {
    ...mapState(['siderCollapsed', 'currentTab'])
  },
  methods: {
    ...mapMutations(['newTab']),
  },
  watch: {
    currentTab(val) {
      localStorage.setItem("currentTab", val);
    }
  },
  data() {
    return {
      loading: false
    }
  },
  created() {
    const currentTab = localStorage.getItem("currentTab")
    if (currentTab) {
      this.newTab(currentTab);
    }
  }
}
</script>
