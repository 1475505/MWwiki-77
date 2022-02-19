<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <p>
        <a-button type="primary" @click="add()" size="large">
          新增
        </a-button>
      </p>
      <br/>
      <a-input-search
          v-model:value="value"
          placeholder="按名称筛选"
          enter-button
          style="width: 256px"
          @search="onSearch"
      />
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/Doc?ebookId=' + record.id">
              <a-button type="primary">
                文档管理
              </a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="电子书信息"
      v-model:visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
            expand-trigger="hover"
            placeholder="选择分类"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/util/tool';

export default defineComponent({
  name: 'adminEbook',
  setup() {
    const param = ref();
    param.value = {};
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      ebooks.value = [];
      axios.get("/Ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
      });
    };

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
        //级联选择
    const categoryIds = ref();
    let categorys: any;
    const level1 = ref();
    const ebook = ref();
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/Category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    const modalText = ref<string>('Content of the modal');
    const visible = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);

    const showModal = () => {
      visible.value = true;
    };

    const handleOk = () => {
      confirmLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/Ebook/save", ebook.value).then((response) => {
        const data = response.data;// data = common response.
        confirmLoading.value = false;
        if (data.success) {
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      visible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    // 搜索
    const value = ref<string>('');

    const onSearch = (searchValue: string) => {
      param.value.name = value;
      handleQuery({page: 1, size: pagination.value.pageSize, name: value})
    };
    /**
     * 新增
     */
    const add = () => {
      visible.value = true;
      ebook.value = {};
    };

    const handleDelete = (id: number) => {
      axios.delete("/Ebook/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQueryCategory();

    });

    return {
      param,
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,
      handleOk,
      handleDelete,

      level1,
      categorys,
      categoryIds,
      getCategoryName,
      handleQueryCategory,


      edit,
      add,
      value,
      onSearch,

      ebook,
      modalText,
      visible,
      confirmLoading,
      showModal,

    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
