import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import 'vditor/dist/index.css'
// import 'vditor/src/assets/scss/index.scss'
// import 'vditor/src/assets/scss/_content.scss'
// import 'vditor/src/assets/scss/_ir.scss'
// import 'vditor/src/assets/scss/_sv.scss'
// import 'vditor/src/assets/scss/_hint.scss'
// import 'vditor/src/assets/scss/_panel.scss'
// import 'vditor/src/assets/scss/_reset.scss'
// import 'vditor/src/assets/scss/_toolbar.scss'
// import 'vditor/src/assets/scss/_tooltipped.scss'
// import 'vditor/src/assets/scss/_wysiwyg.scss'

axios.defaults.baseURL = process.env.VUE_APP_SERVER
/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    const token = store.state.user.token;
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("请求headers增加token:", token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

console.log('环境：', process.env.NODE_ENV);
console.log('服务端口：', process.env.VUE_APP_SERVER);
