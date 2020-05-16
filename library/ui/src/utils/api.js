import Vue from 'vue'
export default {
  // 后台登录接口
  login (p) {
    return Vue.$post('/api/auth/userlogin', p)
  },
  // 获取图书管理
  getBookManage (p) {
    return Vue.$get('/api/cms/book/list/' + p.page + '/' + p.size)
  },
  // 获取图书分类管理
  getCatgoryManage (p) {
    return Vue.$get('/api/cms/cat/list/', p)
  },
  // 获取分类标签
  getTags (p) {
    return Vue.$get('/api/cms/cat/list/', p)
  }
}
