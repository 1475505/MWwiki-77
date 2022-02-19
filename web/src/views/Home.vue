<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <div>
        <a-menu
            style="width: 200px"
            :style="{ height: '100%', borderRight: 0 }"
            v-model:openKeys="openKeys"
            mode="inline"
            :theme="theme"
            @click="handleClick"
            :inline-collapsed="collapsed"
        >
          <a-switch
              :checked="theme === 'dark'"
              checked-children="Dark"
              un-checked-children="Light"
              @change="changeTheme"
          />
          <br/>
          <br/>
          <a-menu-item key="welcome">
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template v-slot:title>
              <span><user-outlined/>{{ item.name }}</span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <span>{{ child.name }}</span>
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </div>
    </a-layout-sider>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-list item-layout="vertical" size="large" :grid="{column: 2}" :pagination="pagination" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
           <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRefs} from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import axios from 'axios';
import {message} from 'ant-design-vue';
import {LikeOutlined, MessageOutlined, StarOutlined} from '@ant-design/icons-vue';
import {Tool} from '@/util/tool';


export default defineComponent({
  name: 'Home',
  components: {
    HelloWorld,
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {

    const state = reactive({
      theme: 'dark',
      selectedKeys: ['1'],
      openKeys: ['sub1'],
      collapsed: true
    });

    const changeTheme = (checked: boolean) => {
      state.theme = checked ? 'dark' : 'light';
    };
    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];
    const ebooks = ref();
    const openKeys = ref();

    const level1 = ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/Category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          // 加载完分类后，将侧边栏全部展开
          openKeys.value = [];
          for (let i = 0; i < categorys.length; i++) {
            openKeys.value.push(categorys[i].id)
          }

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };

    const isShowWelcome = ref(true);
    let categoryId2 = 0;

    const handleQueryEbook = () => {
      axios.get("/Ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    };
    const handleClick = (value: any) => {
      // console.log("menu click", value)
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
      // isShowWelcome.value = value.key === 'welcome';
    };

    onMounted(() => {
      handleQueryCategory();
      // handleQueryEbook();
    });

    return {
      ...toRefs(state),

      level1,
      handleClick,
      changeTheme,
      isShowWelcome,
      openKeys,
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions,
      ebooks,
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
}
</style>
