<template>
  <div class="home">
    <div>
      <a-button type="primary" danger>Primary</a-button>
      <a-button danger>Default</a-button>
      <a-button type="dashed" danger>Dashed</a-button>
      <a-button type="text" danger>Text</a-button>
      <a-button type="link" danger>Link</a-button>
    </div>


    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src

export default defineComponent({
  name: 'Home',
  components: {
    HelloWorld,
  },
});
</script>
