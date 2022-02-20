<template>
  <a-layout-footer style="text-align: center">
    Hi,{{ user.name }} this site is sourced by 070077.xyz<br>
    <a href="https://beian.miit.gov.cn/">桂ICP备2021008883号</a>
  </a-layout-footer>
</template>

<script lang="ts">
import store from '@/store';
import {computed, defineComponent} from 'vue';

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);
    return {
      user,
    }
  }
});
</script>


<style scoped>

</style>