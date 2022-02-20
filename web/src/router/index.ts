import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/Home.vue'
import Doc from '../views/doc.vue'
import adminEbook from '../views/admin/Ebook.vue'
import adminCategory from '../views/admin/Category.vue'
import adminDoc from '../views/admin/Doc.vue'
import adminUser from '../views/admin/User.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/doc',
    name: 'Doc',
    component: Doc
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/admin/ebook',
    name: 'adminEbook',
    component: adminEbook
  },
  {
    path: '/admin/category',
    name: 'adminCategory',
    component: adminCategory
  },
  {
    path: '/admin/doc',
    name: 'adminDoc',
    component: adminDoc
  },
  {
    path: '/admin/user',
    name: 'adminUser',
    component: adminUser
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
