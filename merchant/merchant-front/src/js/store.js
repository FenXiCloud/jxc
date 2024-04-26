import {createStore} from 'vuex'
import {toArrayTree} from 'xe-utils'
import {Init} from "@js/api/App";

export default createStore({
  state: {
    siderCollapsed: false,
    user: {},
    menus: [],
    granted: [],
    currentTab:  'DashboardMain',
  },
  mutations: {
    updateMenus(state, data) {
      state.menus = data;
    },
    updateAccount(state, { account}) {
      state.user = account;
      state.granted = account.granted || [];
    },
    updateSiderCollapse(state, isShow) {
      setTimeout(() => {
        state.pageResizeCount += 1;
      }, 600);
      state.siderCollapsed = isShow;
    },
    newTab(state, key) {
      state.currentTab = key;
    },
  },
  actions: {
    init({commit}) {
      return new Promise((resolve, reject) => {
        Init().then(({success, data}) => {
          if (success) {
            commit('updateAccount', data);
            commit('updateMenus', toArrayTree(data.menus, {strict: true}));
            resolve(data.account);
          } else {
            reject();
          }
        }).catch(() => {
          reject();
        })
      })
    },
    updateSiderCollapse({commit}, data) {
      commit('updateSiderCollapse', data);
    }
  },
  getters: {
    account: state => {
      return state.user;
    },
    siderCollapsed: state => {
      return state.siderCollapsed;
    }
  }
})
