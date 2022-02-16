<template>
  <a-layout>
    <the-header></the-header>
    <router-view/>
    <a-layout-footer style="text-align: center">
      <a href="https://beian.miit.gov.cn/">桂ICP备2021008883号</a>
    </a-layout-footer>
  </a-layout>
</template>

<script lang="ts">
import {LaptopOutlined, NotificationOutlined, UserOutlined} from '@ant-design/icons-vue';
import {defineComponent, ref} from 'vue';
import theHeader from "@/components/the-header.vue";

export default defineComponent({
  name: 'app',
  components: {
    UserOutlined,
    LaptopOutlined,
    NotificationOutlined,
    theHeader
  },
  setup() {
    return {
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      collapsed: ref<boolean>(false),
      openKeys: ref<string[]>(['sub1']),
    };
  },
});
</script>
<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
