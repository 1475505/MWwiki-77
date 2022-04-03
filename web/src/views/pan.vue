<template>
  <a-upload action="http://localhost:8880/pan/upload" v-model:file-list="fileList">
    <a-button>
      <upload-outlined></upload-outlined>
      上传
    </a-button>
  </a-upload>
  <h3 :style="{ margin: '16px 0' }">已有文件列表</h3>
  <a-list size="small" bordered :data-source="data">
    <template #renderItem="{ item }">
      <a-list-item>{{ item }}</a-list-item>
    </template>
    <template #header>
      <div>通过：service.070077.xyz/files/ 接文件名下载</div>
    </template>
    <template #footer>
      <div>刷新以更新</div>
    </template>
  </a-list>
</template>
<script>
import {defineComponent, ref} from 'vue';
import {UploadOutlined} from "@ant-design/icons-vue";
import axios from "axios";

export default defineComponent({
  components: {
    UploadOutlined,
  },

  setup() {
    const fileList = ref();
    const handleChange = ({file, fileList}) => {
      if (file.status !== 'uploading') {
        console.log(file, fileList);
      }
    };

    const data = ref()

    axios.get("/pan/list").then((response) => {
      data.value = response.data
    })

    return {
      fileList,
      handleChange,
      data
    };
  },
})
</script>