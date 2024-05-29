<template>
  <AppFrame v-if="user.merchantId"/>
  <Login v-else/>
</template>
<script>
import AppFrame from "@components/app/AppFrame";
import {mapState} from "vuex";
import Login from "@components/Login";

export default {
  components: {Login, AppFrame},
  computed: {
    ...mapState(['user'])
  }
}
</script>
