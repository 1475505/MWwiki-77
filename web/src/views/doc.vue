<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :draggable="true"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
              :showLine="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18" disabled="true">
          <div>
            <h2>{{ doc.name }}</h2>
            <div>
              <span>阅读数：{{ doc.viewCount }}</span> &nbsp; &nbsp;
              <span>点赞数：{{ doc.voteCount }}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <!--          <div :innerHTML="html"></div>-->
          <div id="vditor" disabled></div>
          <div class="vote-div">
            <a-button type="primary" shape="round" :size="'large'" @click="vote">
              <template #icon>
                <LikeOutlined/> &nbsp;点赞：{{ doc.voteCount }}
              </template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import Vditor from 'vditor'
import 'vditor/dist/index.css'
import 'vditor/src/assets/scss/index.scss';

const editor = ref();

export default defineComponent({
  name: 'Doc',
  setup() {
    const route = useRoute();
    const docs = ref();
    const html = ref();
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];
    // 当前选中的文档
    const doc = ref();
    doc.value = {};

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];

    //渲染
    // const initOutline = () => {
    //   const headingElements: Element[] = []
    //   Array.from(document?.getElementById('preview').children).forEach((item) => {
    //     if (item.tagName.length === 2 && item.tagName !== 'HR' && item.tagName.indexOf('H') === 0) {
    //       headingElements.push(item)
    //     }
    //   })
    //
    //   let toc: { id: string; }[] = []
    //   window.addEventListener('scroll', () => {
    //     const scrollTop = window.scrollY
    //     toc = []
    //     headingElements.forEach((item) => {
    //       return toc.push({
    //         id: item.id,
    //       });
    //     })
    //
    //     const currentElement = document.querySelector('.vditor-outline__item--current')
    //     for (let i = 0, iMax = toc.length; i < iMax; i++) {
    //       if (scrollTop < toc[i].offsetTop - 30) {
    //         if (currentElement) {
    //           currentElement.classList.remove('vditor-outline__item--current')
    //         }
    //         let index = i > 0 ? i - 1 : 0
    //         document.querySelector('span[data-target-id="' + toc[index].id + '"]')?.classList.add('vditor-outline__item--current')
    //         break
    //       }
    //     }
    //   })
    // }
    // fetch('markdown/zh_CN.md').
    // then(response => response.text()).
    // then(markdown => {
    //   return Vditor.preview(document.getElementById('preview'),
    //       markdown, {
    //         speech: {
    //           enable: true,
    //         },
    //         anchor: 1,
    //         after() {
    //           if (window.innerWidth <= 768) {
    //             return;
    //           }
    //           const outlineElement = document.getElementById('outline');
    //           Vditor.outlineRender(document.getElementById('preview'), outlineElement);
    //           if (outlineElement?.innerText.trim() !== '') {
    //             outlineElement.style.display = 'block';
    //             initOutline();
    //           }
    //         },
    //       });
    // })

    /**
     * 内容查询
     **/
    const handleQueryContent = (id: number) => {
      axios.get("/Doc/find-content/" + id).then(async (response) => {
        const data = response.data;
        editor.value.setValue("");
        if (data.success) {
          editor.value.setValue(data.content);
          editor.value.blur();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/Doc/all/" + route.query.ebookId).then((response) => {
        const data = response.data;
        if (data.success) {
          docs.value = data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);

          if (Tool.isNotEmpty(level1)) {
            defaultSelectedKeys.value = [level1.value[0].id];
            handleQueryContent(level1.value[0].id);
            // 初始显示文档信息
            doc.value = level1.value[0];
          }
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)) {
        // 选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
        // 加载内容
        handleQueryContent(selectedKeys[0]);
      }
    };

    // 点赞
    const vote = () => {
      axios.get('/Doc/vote/' + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          doc.value.voteCount++;
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      editor.value = new Vditor('vditor', {
        theme: 'classic',
        // _lutePath: 'src/js/lute/lute.min.js',
        mode: "ir",
        preview: {
          delay: 1000,
          "hljs": {
            "lineNumber": true,
            "enable": true,
            "style": "emacs",
          }
        },
        toolbarConfig: {
          pin: true,
        },
        cache: {
          enable: false
        },
        toolbar: [
          'bold',
          'italic',
          'strike',
          'undo',
          'redo',
          'export',
          {
            name: 'more',
            toolbar: [
              'fullscreen',
              'both',
              'preview',
              'info',
              'help',
            ],
          }],
        "counter": {
          "enable": true
        },
        "outline": {
          "enable": true,
          "position": "right"
        },
        "value": "LOADING...长时间不响应请刷新",
      })
      handleQuery();
    });

    return {
      level1,
      html,
      onSelect,
      defaultSelectedKeys,
      doc,
      vote
    }
  }
});
</script>

<style>

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.vditor img {
  max-width: 100%;
  height: auto;
}
</style>
